package com.example.pokedex.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.models.PokeListEntry
import com.example.pokedex.repository.PokeRepository
import com.example.pokedex.util.ApiResponse
import com.example.pokedex.util.Constants
import com.example.pokedex.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private var currentPage = 0

    private val dummyPokemonList = listOf<PokeListEntry>(
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
        PokeListEntry("", "", 1),
    )
    private val pokemonList = mutableStateOf<List<PokeListEntry>>(listOf())

    private val _pokemonsState =
        mutableStateOf<UIState<List<PokeListEntry>>>(UIState(data = dummyPokemonList))
    val pokemonsState get() = _pokemonsState

    fun getPokemonsPaginated() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("pokemonsPaginated", "offset: ${currentPage * 20}")
            pokeRepository.getPokemonsPaginated(PAGE_SIZE, currentPage * PAGE_SIZE)
                .onEach { apiResponse ->
                    when (apiResponse) {
                        is ApiResponse.Success -> {
                            val pokeEntries: MutableList<PokeListEntry> = mutableListOf()

                            // create list of pokeListEntry with icon url
                            apiResponse.data!!.results.forEach { pokeListResultItem ->
                                val pokeNumber = if (pokeListResultItem.url.endsWith("/")) {
                                    pokeListResultItem.url.dropLast(1)
                                        .takeLastWhile { it.isDigit() }
                                } else {
                                    pokeListResultItem.url.takeLastWhile { it.isDigit() }
                                }
                                val pokeImageUrl = Constants.BASE_ICON_URL + pokeNumber + ".png"
                                val pokeListEntry = PokeListEntry(
                                    pokeListResultItem.name.replaceFirstChar { it.uppercase() },
                                    pokeImageUrl,
                                    pokeNumber.toInt()
                                )
                                pokeEntries.add(pokeListEntry)
                            }
                            pokemonList.value += pokeEntries

                            _pokemonsState.value = UIState(
                                isLoading = false,
                                data = pokemonList.value
                            )
                            currentPage++
                            Log.d("pokemonState", _pokemonsState.toString())
                        }
                        is ApiResponse.Error -> {
                            _pokemonsState.value =
                                UIState(isLoading = false, error = apiResponse.message)
                        }
                        is ApiResponse.Loading -> {
                            if(currentPage < 1) {
                                _pokemonsState.value =
                                    UIState(isLoading = true, data = dummyPokemonList, error = null)
                            }
                        }
                    }
                    Log.d("apiResponse_pokemons", apiResponse.data.toString())
                }.launchIn(viewModelScope)
        }
    }

    fun getPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemon(name).onEach {
                Log.d("apiResponse_Pokemon", it.data.toString())
            }.launchIn(viewModelScope)
        }
    }

    fun getPokemonByType(pokemonType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonbyType(pokemonType).onEach {
                Log.d("apiResponse_pokemonType", it.data.toString())
            }.launchIn(viewModelScope)
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}