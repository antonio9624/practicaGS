package com.example.mvvmpokemon.view.fragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Database
import com.example.mvvmpokemon.databinding.FrgMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects


class FrgMap : Fragment() {

    lateinit var binding: FrgMapBinding
    private lateinit var mfused: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrgMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermision()
    }

    private fun checkPermision() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permiso no aceptado por el momento
            requestPermision()
        } else {
            //permiso aceptado

            mfused = LocationServices.getFusedLocationProviderClient(requireContext())
            mfused.getLastLocation()
                .addOnSuccessListener(requireContext() as Activity, OnSuccessListener<Location>() {
                    if (it != null) {
                        var mdatabase = FirebaseDatabase.getInstance().getReference("usuario")
                        Log.d("LOCATION",   "latitud:" + it.latitude + ", Longitud: ${it.longitude}")
                        val latLng: MutableMap<String, Any> = HashMap()
                        latLng.put("latitud", it.latitude)
                        latLng.put("longitud", it.longitude)
                        mdatabase.push().setValue(latLng)
                    }

                })
            // openMap()
        }
    }

    private fun openMap() {
        Toast.makeText(requireContext(), "permisos aceptados", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermision() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireContext() as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            //permiso rechazado
            Toast.makeText(requireContext(), "permiso rechazado", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 777
            )


        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777) {
            //listo
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //el permiso no a sido aceptado
                openMap()
            }
        } else {
            requestPermision()
            Toast.makeText(
                requireContext(),
                "permisos rechazados por primera vez",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
}