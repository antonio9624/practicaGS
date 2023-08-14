package com.example.mvvmpokemon.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mvvmpokemon.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mvvmpokemon.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

class Maps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mfused: FusedLocationProviderClient
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        checkPermision(googleMap)
    }

    private fun checkPermision(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permiso no aceptado por el momento
            requestPermision()
        } else {
            //permiso aceptado

            mfused = LocationServices.getFusedLocationProviderClient(this)
            mfused.getLastLocation()
                .addOnSuccessListener(this, OnSuccessListener<Location>() {
                    if (it != null) {

                        mMap = googleMap

                        // Add a marker in Sydney and move the camera
                        val my = LatLng(it.latitude, it.longitude)
                        mMap.addMarker(MarkerOptions().position(my).title("my"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(my))

                        var mdatabase = FirebaseFirestore.getInstance()
                        Log.d("LOCATION",   "latitud:" + it.latitude + ", Longitud: ${it.longitude}")
                         var location= com.example.mvvmpokemon.model.Location(
                             it.latitude,
                             it.longitude
                         )

                        mdatabase.collection("usuario")
                            .add(location)
                            .addOnSuccessListener {
                                Toast.makeText(this, "agregacion Correcta", Toast.LENGTH_SHORT).show()
                                Log.d("firebase","firebase"+ it.id.toString())
                            }
                            .addOnFailureListener {
                                var er= it.cause.toString()
                                var r = it.message.toString()
                                var e =it.localizedMessage.toString()
                                println(er)
                                println(r)
                                println(e)
                                Toast.makeText(this, "Ha ocurrio un error"+ it.toString().trim(), Toast.LENGTH_SHORT).show()
                                Log.d("DATA","firebase"+it.toString().trim())
                                Log.d("Firebase","firebase"+it.message.toString().trim())
                                Log.d("Firebase","firebase"+it.cause.toString().trim())
                            }
                            .addOnCanceledListener {
                                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
                                it
                            }
                            .addOnCompleteListener {
                                //en caso de mandar un dialogo o alguna pantalla secuandaria se podria cerrar
                            }
                    }

                })
            // openMap()
        }
    }

    private fun openMap() {
        Toast.makeText(this, "permisos aceptados", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermision() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            //permiso rechazado
            Toast.makeText(this, "permiso rechazado", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
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
                this,
                "permisos rechazados por primera vez",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
}