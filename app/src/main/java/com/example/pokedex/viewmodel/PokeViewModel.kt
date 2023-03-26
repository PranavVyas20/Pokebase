package com.example.pokedex.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.data.models.PokeListEntry
import com.example.pokedex.remote.responses.PokemonResonse
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
        mutableStateOf<UIState<PokemonResonse>>(UIState(currentScreenState = Constants.LastResponseType.POKE_DETAIL))
    val detailPokemonState get() = _detailPokemonState

    fun getPokemonsPaginated() {
        _filterScreenVisibility = false
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonsPaginated(PAGE_SIZE, currentPage * PAGE_SIZE)
                .onEach { apiResponse ->
                    when (apiResponse) {
                        is Resource.Success -> {

                            apiResponse.data!!.results.forEach { pokeListResultItem ->

                                val pokeNumber = if (pokeListResultItem.url.endsWith("/")) {
                                    pokeListResultItem.url.dropLast(1)
                                        .takeLastWhile { it.isDigit() }
                                } else {
                                    pokeListResultItem.url.takeLastWhile { it.isDigit() }
                                }
                                val pokeImageUrl = Constants.BASE_ICON_URL + pokeNumber + ".png"

                                val pokeListEntry = PokeListEntry(
                                    pokeListResultItem.name,
                                    pokeImageUrl,
                                    getPokemonNumberFormatted(pokeNumber.toInt()),
                                    number = pokeNumber.toInt()
                                )
                                normalPokemonList.add(pokeListEntry)
                            }

                            _pokemonState.value = UIState(
                                false,
                                data = normalPokemonList.toList(),
                                currentScreenState = Constants.LastResponseType.NORMAL_POKE_LIST
                            )
                            currentPage++
                        }
                        is Resource.Error -> {
                            _pokemonState.value = UIState(
                                isLoading = false,
                                error = apiResponse.message,
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
                }.launchIn(viewModelScope)
        }
    }

    fun getPokemon(name: String, getPokemonDetails: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemon(name).onEach { apiResponse ->
                delay(10000)
                Log.d("threadUsed", Thread.currentThread().name)
                when (apiResponse) {
                    is Resource.Success -> {

                        val pokemonDetailResponse = apiResponse.data
                        val pokeImageUrl =
                            Constants.BASE_ICON_URL + pokemonDetailResponse!!.pokemonId + ".png"
                        val singlePokeEntry = PokeListEntry(
                            pokemonName = apiResponse.data.pokemonName,
                            imageUrl = pokeImageUrl,
                            formattedNumber = getPokemonNumberFormatted(pokemonDetailResponse.pokemonId),
                            number = pokemonDetailResponse.pokemonId
                        )

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
                                data = listOf(singlePokeEntry),
                                currentScreenState = Constants.LastResponseType.SEARCHED_POKE_LIST
                            )
                        } else {
                            _detailPokemonState.value = UIState(
                                isLoading = false,
                                pokemonDetailResponse,
                                currentScreenState = Constants.LastResponseType.POKE_DETAIL
                            )
                        }
                    }
                    is Resource.Error -> {

                        if (!getPokemonDetails) {
                            _pokemonState.value = UIState(
                                isLoading = false,
                                null,
                                error = apiResponse.message,
                                currentScreenState = Constants.LastResponseType.SEARCHED_POKE_LIST
                            )
                        } else {
                            _detailPokemonState.value = UIState(
                                false,
                                null,
                                apiResponse.message,
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
            }.launchIn(viewModelScope)
        }
    }

    fun getPokemonByType(pokemonType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.getPokemonbyType(pokemonType).onEach { apiResponse ->
                when (apiResponse) {
                    is Resource.Success -> {
                        lastListType = Constants.LastResponseType.FILTERED_POKE_LIST
                        val response = apiResponse.data!!.pokemonType

                        filteredPokemonList.clear()

                        response.forEach { pokemonTypeResponse ->
                            val pokemonListResponse = pokemonTypeResponse.pokemon
                            val pokeNumber = if (pokemonListResponse.url.endsWith("/")) {
                                pokemonListResponse.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                pokemonListResponse.url.takeLastWhile { it.isDigit() }
                            }
                            val pokeImageUrl = Constants.BASE_ICON_URL + pokeNumber + ".png"
                            val pokeListEntry = PokeListEntry(
                                pokemonName = pokemonListResponse.name,
                                imageUrl = pokeImageUrl,
                                formattedNumber = getPokemonNumberFormatted(pokeNumber.toInt()),
                                number = pokeNumber.toInt()
                            )
                            filteredPokemonList.add(pokeListEntry)
                        }
                        _pokemonState.value = UIState(
                            false,
                            data = filteredPokemonList.toList(),
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
            }.launchIn(viewModelScope)
        }
    }

    fun savePokemon(pokemon: PokemonResonse, pokeDrawable: Bitmap) {
        viewModelScope.launch {
            val deferred = viewModelScope.async {
                pokeRepository.getPokemonFromDb(pokemon.pokemonName)
            }
            if (deferred.await() != null) {
                deletePokemon(pokemon)
            } else {
                pokemon.pokemonImage = pokeDrawable
                pokeRepository.savePokemon(pokemon).onEach {
                    when (it) {
                        is Resource.Success -> {
                            toast(Constants.ToastMessage.SAVE_SUCCESS)
                        }
                        is Resource.Error -> {
                            toast(Constants.ToastMessage.SAVE_ERROR)
                        }
                        is Resource.Loading -> {

                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun deletePokemon(pokemon: PokemonResonse) {
        viewModelScope.launch {
            pokeRepository.deletePokemon(pokemon).onEach {
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
            }.launchIn(viewModelScope)
        }
    }

    fun getSavedPokemons() {
        viewModelScope.launch {
            pokeRepository.getSavedPokemons().onEach { pokemonResponse ->
                when (pokemonResponse) {

                    is Resource.Success -> {
                        val savedPokemons = pokemonResponse.data
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
                            error = pokemonResponse.message
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
        val deferred = viewModelScope.async {
            pokeRepository.getPokemonFromDb(pokemonName)
        }
        if (deferred.await() != null) {
            _detailPokemonState.value = UIState(
                isLoading = false,
                data = deferred.await(),
                currentScreenState = Constants.LastResponseType.POKE_DETAIL
            )
        } else {
            getPokemon(pokemonName, true)
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