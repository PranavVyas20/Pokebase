package com.example.pokedex.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.data.models.PokeListEntry
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.toPokemonEntity
import com.example.pokedex.remote.responses.toPokeListEntries
import com.example.pokedex.remote.responses.toPokeListEntry
import com.example.pokedex.remote.responses.toPokemon
import com.example.pokedex.repository.PokeRepository
import com.example.pokedex.util.Constants
import com.example.pokedex.util.Constants.PAGE_SIZE
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import xdroid.toaster.Toaster.toast
import javax.inject.Inject

@HiltViewModel
class PokeViewModel @Inject constructor(
    private val pokeRepository: PokeRepository
) : ViewModel() {

    data class UIState<T : Any>(
        val isLoading: Boolean = true,
        val data: T? = null,
        val error: String? = null,
        val currentScreenState: String
    )

    private val dummyPokemonList = createDummyList()
    private val normalPokemonList = mutableListOf<PokeListEntry>()
    private val filteredPokemonList = mutableListOf<PokeListEntry>()

    private val _pokemonState = mutableStateOf<UIState<List<PokeListEntry>>>(
        UIState(
            data = dummyPokemonList,
            currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
        )
    )
    val pokemonState get() = _pokemonState

    private val _savedPokemonState = mutableStateOf<UIState<List<PokeListEntry>>>(
        UIState(
            data = dummyPokemonList,
            currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
        )
    )
    val savedPokemonState get() = _savedPokemonState

    var returnedBackFromPokeDetail = mutableStateOf(false)

    var lastListType = Constants.LastResponseType.NORMAL_POKE_LIST

    var savedPokemonListItemIdx = 0

    private var searchedFromState = Constants.LastResponseType.NORMAL_POKE_LIST

    var dominantColor by mutableStateOf<Color?>(null)
    var loadedPokemonImage by mutableStateOf<Drawable?>(null)

    private var _filterScreenVisibility by mutableStateOf(false)
    val filterScreenVisibility get() = _filterScreenVisibility

    var searchBoxText by mutableStateOf("")

    private var _blackScreenVisibility by mutableStateOf(false)
    val blackScreenVisibility get() = _blackScreenVisibility


    private var currentPage = 0

    private val _detailPokemonState =
        mutableStateOf<UIState<Pokemon>>(UIState(currentScreenState = Constants.LastResponseType.POKE_DETAIL))
    val detailPokemonState get() = _detailPokemonState

    fun getPokemonsPaginated() {
        _filterScreenVisibility = false
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonsPaginated(PAGE_SIZE, currentPage * PAGE_SIZE)
                .collect { pokemonsApiResponse ->
                    when (pokemonsApiResponse) {
                        is Resource.Success -> {
                            normalPokemonList.addAll(pokemonsApiResponse.data?.results!!.toPokeListEntries())
                            _pokemonState.value = UIState(
                                isLoading = false,
                                data = normalPokemonList.toList(),
                                currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
                            )
                            currentPage++
                        }
                        is Resource.Error -> {
                            _pokemonState.value = UIState(
                                isLoading = false,
                                error = pokemonsApiResponse.message,
                                currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
                            )
                        }
                        is Resource.Loading -> {
                            if (currentPage < 1) {
                                _pokemonState.value = UIState(
                                    isLoading = true,
                                    data = dummyPokemonList,
                                    error = null,
                                    currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
                                )
                            }
                        }
                    }
                }
        }
    }

    fun getPokemon(name: String, getPokemonDetails: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemon(name).collect { pokemonApiResponse ->
                when (pokemonApiResponse) {
                    is Resource.Success -> {
                        if (!getPokemonDetails) {

                            when (_pokemonState.value.currentScreenState) {
                                Constants.LastResponseType.NORMAL_POKE_LIST -> {
                                    searchedFromState =
                                        Constants.LastResponseType.NORMAL_POKE_LIST
                                }
                                Constants.LastResponseType.FILTERED_POKE_LIST -> {
                                    searchedFromState =
                                        Constants.LastResponseType.FILTERED_POKE_LIST
                                }
                            }

                            _pokemonState.value = UIState(
                                false,
                                data = listOf(pokemonApiResponse.data!!.toPokeListEntry()),
                                currentScreenState = Constants.LastResponseType.SEARCHED_POKE_LIST
                            )
                        } else {
                            _detailPokemonState.value = UIState(
                                isLoading = false,
                                pokemonApiResponse.data?.toPokemon(),
                                currentScreenState = Constants.LastResponseType.POKE_DETAIL
                            )
                        }
                    }
                    is Resource.Error -> {

                        if (!getPokemonDetails) {
                            _pokemonState.value = UIState(
                                isLoading = false,
                                null,
                                error = pokemonApiResponse.message,
                                currentScreenState = Constants.LastResponseType.SEARCHED_POKE_LIST
                            )
                        } else {
                            _detailPokemonState.value = UIState(
                                false,
                                null,
                                pokemonApiResponse.message,
                                currentScreenState = Constants.LastResponseType.POKE_DETAIL
                            )
                        }
                    }
                    is Resource.Loading -> {

                        if (!getPokemonDetails) {
                            _pokemonState.value = UIState(
                                true,
                                data = listOf<PokeListEntry>(),
                                currentScreenState = Constants.LastResponseType.SEARCHED_POKE_LIST
                            )
                        } else {
                            _detailPokemonState.value = UIState(
                                isLoading = false,
                                currentScreenState = Constants.LastResponseType.POKE_DETAIL
                            )
                        }
                    }
                }
            }
        }
    }

    fun getPokemonByType(pokemonType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonbyType(pokemonType).collect { apiResponse ->
                when (apiResponse) {
                    is Resource.Success -> {
                        lastListType = Constants.LastResponseType.FILTERED_POKE_LIST
                        val response = apiResponse.data!!.pokemonType
                        filteredPokemonList.clear()
                        response.forEach {
                            filteredPokemonList.add(it.pokemon.toPokeListEntry())
                        }
                        _pokemonState.value = UIState(
                            isLoading = false,
                            data = filteredPokemonList,
                            currentScreenState = Constants.LastResponseType.FILTERED_POKE_LIST
                        )

                    }
                    is Resource.Error -> {
                        _pokemonState.value = UIState(
                            isLoading = false,
                            error = apiResponse.message,
                            currentScreenState = Constants.LastResponseType.FILTERED_POKE_LIST
                        )
                    }
                    is Resource.Loading -> {
                        _pokemonState.value = UIState(
                            isLoading = true,
                            data = dummyPokemonList,
                            error = null,
                            currentScreenState = Constants.LastResponseType.FILTERED_POKE_LIST
                        )
                    }
                }
            }
        }
    }

    fun savePokemon(pokemon: Pokemon, pokeDrawable: Bitmap) {
        viewModelScope.launch {
                pokeRepository.getPokemonFromDb(pokemon.pokemonName).collect {
                    if (it is Resource.Success) {
                        if (it.data != null) {
                            deletePokemon(pokemon)
                        } else {
                            pokemon.pokemonImage = pokeDrawable
                            pokeRepository.savePokemon(pokemon.toPokemonEntity()).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        toast(Constants.ToastMessage.SAVE_SUCCESS)
                                    }
                                    is Resource.Error -> {
                                        toast(Constants.ToastMessage.SAVE_ERROR)
                                    }
                                    is Resource.Loading -> {}
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun deletePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokeRepository.deletePokemon(pokemon.toPokemonEntity()).collect {
                when (it) {
                    is Resource.Success -> {
                        toast(Constants.ToastMessage.DELETE_SUCCESS)
                    }
                    is Resource.Error -> {
                        toast(Constants.ToastMessage.DELETE_ERROR)
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    fun getSavedPokemons() {
        viewModelScope.launch {
            pokeRepository.getSavedPokemons().onEach { savedPokemonResponse ->
                when (savedPokemonResponse) {

                    is Resource.Success -> {
                        val savedPokemons = savedPokemonResponse.data
                        val newPokeListEntries = mutableListOf<PokeListEntry>()

                        savedPokemons?.forEach {
                            val pokeImageUrl = Constants.BASE_ICON_URL + it.pokemonId + ".png"
                            val singlePokeEntry = PokeListEntry(
                                pokemonName = it.pokemonName,
                                imageUrl = pokeImageUrl,
                                formattedNumber = getPokemonNumberFormatted(it.pokemonId),
                                number = it.pokemonId,
                                savedPokeImage = it.pokemonImage
                            )
                            newPokeListEntries.add(singlePokeEntry)
                        }
                        _savedPokemonState.value = UIState(
                            isLoading = false,
                            data = newPokeListEntries,
                            currentScreenState = Constants.LastResponseType.SAVED_POKE_LIST
                        )
                    }

                    is Resource.Error -> {
                        _savedPokemonState.value = UIState(
                            isLoading = false,
                            data = null,
                            currentScreenState = Constants.LastResponseType.SAVED_POKE_LIST,
                            error = savedPokemonResponse.message
                        )
                    }

                    is Resource.Loading -> {
                        _savedPokemonState.value = UIState(
                            isLoading = false,
                            data = listOf(),
                            currentScreenState = Constants.LastResponseType.SAVED_POKE_LIST
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    suspend fun getPokemonDetails(pokemonName: String) {
        pokeRepository.getPokemon(pokemonName).collect {
            when (it) {
                is Resource.Loading -> {
                    _detailPokemonState.value = UIState(
                        isLoading = true,
                        currentScreenState = Constants.LastResponseType.POKE_DETAIL
                    )
                }
                is Resource.Success -> {
                    _detailPokemonState.value = UIState(
                        isLoading = false,
                        data = it.data?.toPokemon(),
                        currentScreenState = Constants.LastResponseType.POKE_DETAIL
                    )
                }

                else -> {}
            }
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
    }

    fun resetUIState(responseType: String) {
        when (responseType) {
            Constants.LastResponseType.NORMAL_POKE_LIST -> {
                lastListType = Constants.LastResponseType.NORMAL_POKE_LIST
                _pokemonState.value = UIState(
                    false,
                    normalPokemonList.toList(),
                    currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
                )
            }
            Constants.LastResponseType.FILTERED_POKE_LIST -> {
                _pokemonState.value = UIState(
                    false,
                    filteredPokemonList.toList(),
                    currentScreenState = Constants.LastResponseType.FILTERED_POKE_LIST
                )
            }
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

    fun getPokemonNumberFormatted(pokeNumber: Int): String {
        val maxLength = 3
        var additionalZeros = ""
        val pokeNoFormatted = pokeNumber.toString()
        repeat(maxLength - pokeNoFormatted.length) {
            additionalZeros += '0'
        }
        return "#$additionalZeros$pokeNoFormatted"
    }

    private fun createDummyList(): List<PokeListEntry> {
        val dummyList = mutableListOf<PokeListEntry>()
        for (i in 1..12) {
            dummyList.add(PokeListEntry("", "", i.toString(), i))
        }
        return dummyList.toList()
    }

    fun getDominantColorAndDrawable(color: Color?, drawable: Drawable?) {
        dominantColor = color
        loadedPokemonImage = drawable
    }

    fun two() {
        getUserByIdFromNetwork() {

        }
    }

    private fun getUserByIdFromNetwork(onUserReady: (Unit) -> Unit) {
        viewModelScope.launch {
            val savedLatLong: Deferred<Unit> = async {
                delay(1000)
            }
            onUserReady(savedLatLong.await())
        }
    }
}