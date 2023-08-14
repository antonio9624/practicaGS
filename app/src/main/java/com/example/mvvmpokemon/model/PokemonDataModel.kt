package com.example.mvvmpokemon.model

data class PokemonDataModel(
    //son los datos que recivimos de la llamada
    val id:Long,
    val name:String="",
    val img:String,
    val height:String,
    val weight:String,
    val type:ArrayList<String>
)
