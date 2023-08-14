package com.example.mvvmpokemon.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmpokemon.databinding.FragmentPokemonDetailBinding
import com.example.mvvmpokemon.model.PokemonDataModel
import com.example.mvvmpokemon.viewmodel.RecyclerPokemonViewModel

class PokemonDetailFragment : Fragment() {


    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding

    lateinit var viewModel: RecyclerPokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            activity?.let {
                ViewModelProvider(it).get(RecyclerPokemonViewModel::class.java)
            }!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.itemDataSelected?.let { data ->
            Glide.with(requireContext()).load(data.img).into(binding!!.img)
            binding!!.tvName.text = "Nombre: " + data.name
            binding!!.tvaltura.text = "Altura: " + data.height
            binding!!.tvpeso.text = "Peso: " + data.weight
            if (data.type.size == 1)
                binding!!.tvtipo.text = "Tipo: " + data.type[0]
            else
                binding!!.tvtipo.text = "Tipo: " + data.type[0] + ", " + data.type[1]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
        ) = PokemonDetailFragment()
    }

}