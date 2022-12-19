package com.example.pokedex.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    private var lastResponseType = "pokemonsList"

    private var _filterScreenVisibility by mutableStateOf(false)
    val filterScreenVisibility get() = _filterScreenVisibility

    var searchBoxText by mutableStateOf("")

    private var _blackScreenVisibility by mutableStateOf(false)
    val blackScreenVisibility get() = _blackScreenVisibility

    private var containsPokemonTypes = false

    private var currentPage = 0

    private val singlePokemonList = mutableListOf<PokeListEntry>()

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
    private val _singlePokemonState =
        mutableStateOf<UIState<List<PokeListEntry>>>(UIState(data = listOf()))
    val singlePokemonState get() = _singlePokemonState

    private val pokemonList = mutableStateOf<List<PokeListEntry>>(listOf())

    private val _pokemonsState =
        mutableStateOf<UIState<List<PokeListEntry>>>(UIState(data = dummyPokemonList))
    val pokemonsState get() = _pokemonsState

    fun getPokemonsPaginated() {
        _filterScreenVisibility = false
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("pokemonsPaginated", "offset: ${currentPage * 20}")
            pokeRepository.getPokemonsPaginated(PAGE_SIZE, currentPage * PAGE_SIZE)
                .onEach { apiResponse ->
                    when (apiResponse) {

                        is ApiResponse.Success -> {
                            lastResponseType = "pokemonsList"
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
                            if (containsPokemonTypes) {
                                pokemonList.value = listOf()
                            }
                            pokemonList.value += pokeEntries

                            _pokemonsState.value = UIState(
                                isLoading = false, data = pokemonList.value
                            )
                            currentPage++
                            Log.d("pokemonState", _pokemonsState.toString())
                        }
                        is ApiResponse.Error -> {
                            _pokemonsState.value =
                                UIState(isLoading = false, error = apiResponse.message)
                        }
                        is ApiResponse.Loading -> {
                            if (currentPage < 1) {
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
            pokeRepository.getPokemon(name).onEach { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        lastResponseType = "singlePokemonList"
                        val pokemonResonse = apiResponse.data
                        val pokeImageUrl =
                            Constants.BASE_ICON_URL + pokemonResonse!!.pokemonId + ".png"
                        val singlePokeEntry =
                            PokeListEntry(
                                pokemonName = apiResponse.data.pokemonName,
                                imageUrl = pokeImageUrl,
                                number = pokemonResonse.pokemonId
                            )

                        singlePokemonList.clear()
                        singlePokemonList.add(singlePokeEntry)
                        _pokemonsState.value = UIState(false, data = singlePokemonList)
                    }
                    is ApiResponse.Error -> {
                        _singlePokemonState.value = UIState(false, error = apiResponse.message)
                    }
                    is ApiResponse.Loading -> {
                        _pokemonsState.value = UIState(true, data = listOf<PokeListEntry>())
                    }
                }
                Log.d("apiResponse_Pokemon", apiResponse.data.toString())
            }.launchIn(viewModelScope)
        }
    }

    fun getPokemonByType(pokemonType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonbyType(pokemonType).onEach { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        lastResponseType = "pokemonsTypeList"
                        val response = apiResponse.data!!.pokemonType
                        val pokemonTypesList: MutableList<PokeListEntry> = mutableListOf()

                        response.forEach { pokemonTypeResponse ->
                            val pokemonListResponse = pokemonTypeResponse.pokemon
                            val pokeNumber = if (pokemonListResponse.url.endsWith("/")) {
                                pokemonListResponse.url.dropLast(1)
                                    .takeLastWhile { it.isDigit() }
                            } else {
                                pokemonListResponse.url.takeLastWhile { it.isDigit() }
                            }
                            val pokeImageUrl = Constants.BASE_ICON_URL + pokeNumber + ".png"
                            val pokeListEntry = PokeListEntry(
                                pokemonName = pokemonListResponse.name.replaceFirstChar { it.uppercase() },
                                imageUrl = pokeImageUrl,
                                number = pokeNumber.toInt()
                            )
                            pokemonTypesList.add(pokeListEntry)
                        }
                        _pokemonsState.value = UIState(false, data = pokemonTypesList.toList())
                    }
                    is ApiResponse.Error -> {
                        _pokemonsState.value =
                            UIState(isLoading = false, error = apiResponse.message)
                    }
                    is ApiResponse.Loading -> {
                            _pokemonsState.value =
                                UIState(isLoading = true, data = dummyPokemonList, error = null)
                    }
                }
                Log.d("apiResponse_pokemonType", apiResponse.data.toString())
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

    fun resetUIState(resetFilterApplied: Boolean = false) {
        if(lastResponseType == "pokemonsList" || lastResponseType == "singlePokemonList") {
            if (pokemonList.value.isNotEmpty()) {
                _pokemonsState.value = UIState(isLoading = false, data = pokemonList.value)
            } else {
                getPokemonsPaginated()
            }
        } else if(resetFilterApplied) {
            _pokemonsState.value = UIState(isLoading = false, data = pokemonList.value)
        }
    }

    fun changeFilterScreenVisibilty() {
        _filterScreenVisibility = !_filterScreenVisibility
        _blackScreenVisibility = !_blackScreenVisibility
    }

    fun clearSearchBox() {
        searchBoxText = ""
    }
    fun updateSearchBox(text: String) {
        searchBoxText = text
    }

}