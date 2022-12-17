package com.example.pokedex.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeViewModel @Inject constructor(
    private val pokeRepository: PokeRepository
) : ViewModel() {
    data class UIState<T : Any>(
        val isLoading: Boolean = true, val data: T? = null, val error: String? = null
    )

    fun getPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemons(20,0).onEach {
                Log.d("apiResponse", it.data.toString())
            }.launchIn(viewModelScope)
        }
    }
}