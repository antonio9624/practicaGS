package com.example.mvvmpokemon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.mvvmpokemon.model.PokemonDataModel
import com.example.mvvmpokemon.repository.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RecyclerPokemonViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    /**
     * esto es un view Model del recyclerview
     * en los view model se crean los live data
     */
    private val _itemSelected = MutableLiveData<PokemonDataModel?>()
    var itemDataSelected: PokemonDataModel? = null

    //las listas son cambiantes gracias al mutable list
    private val _listState = MutableLiveData<MutableList<PokemonDataModel>>()

    //livedata es una lista fija no cambia sus datos
    var listState: LiveData<MutableList<PokemonDataModel>> = _listState


    private val _progressState = MutableLiveData<Boolean>()
    var progressState: LiveData<Boolean> = _progressState

    //vamos a hacer una instancia de repository
    private val viewModelJob = Job()
    private val repository = PokemonRepository()
    lateinit var observerOnCategorySelected: Observer<PokemonDataModel>

    override val coroutineContext: CoroutineContext
        get() =  viewModelJob + Dispatchers.Default

    init {
        initObserver()
    }

    private fun initObserver() {
        observerOnCategorySelected = Observer { value ->
            value.let {
                _itemSelected.value = it
            }
        }
    }

    fun clearSelection() {
        _itemSelected.value = null
    }

    fun setItemSelection(item: PokemonDataModel) {
        itemDataSelected = item
    }

    fun fetchPokemonData() {
        _progressState.value = true
        viewModelScope.launch {
            val response = repository.getPokemon()
            response?.body()?.pokemon.let { list->
                _listState.value = list
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Memory leak
}