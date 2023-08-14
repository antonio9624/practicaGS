package com.example.mvvmpokemon.repository

import com.example.mvvmpokemon.services.RetrofitClient
import com.example.mvvmpokemon.services.WebService
import retrofit2.create

class PokemonRepository {

    //declaramos la variable
    private var apiservice: WebService? = null

    init {
        //obtiene el cliente
        apiservice = RetrofitClient.getClient?.create(WebService::class.java)
    }

    //suspend fun son aquellas que se ejecutan en el background fuera del hilo principal
    suspend fun getPokemon() = apiservice?.getPokemons()

}