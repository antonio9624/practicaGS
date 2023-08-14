package com.example.mvvmpokemon.model

data class PokemonResponse(
    //vamos a obtener la lista de datos de POKEMONDATAMODEL
    val pokemon:MutableList<PokemonDataModel> = mutableListOf()
)