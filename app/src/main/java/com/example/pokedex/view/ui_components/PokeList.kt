package com.example.pokedex.view.ui_components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.pokedex.data.models.PokeListEntry
import com.example.pokedex.view.screens.header
import com.example.pokedex.util.Constants

@Composable
fun PokeList(
    searchBarText: String,
    pokemonListEntries: List<PokeListEntry>,
    calculateDominantColor: (drawable: Drawable, onFinish: (Color) -> Unit) -> Unit,
    getPokemonsPaginated: () -> Unit,
    onSearchPressed: (query: String, Boolean) -> Unit,
    onClickDismissed: () -> Unit,
    clearSearchBox: () -> Unit,
    updateSearchBox: (String) -> Unit,
    getDominantColorAndDrawable: (Color?, Drawable?) -> Unit,
    getPokemonDetails: (String, Boolean) -> Unit,
    navController: NavController
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        header {
            PokeListHeader(
                searchBoxText = searchBarText,
                onSearchPressed = onSearchPressed,
                onClickDismissed = onClickDismissed,
                navController = navController,
                clearSearchBox = clearSearchBox,
                updateSearchBox = updateSearchBox
            )
        }
        items(pokemonListEntries) { pokeListEntry ->
            PokeListItem(
                pokemonName = pokeListEntry.pokemonName,
                pokemonFormattedNumber = pokeListEntry.formattedNumber,
                pokemonImageUrl = pokeListEntry.imageUrl,
                calculateDominantColor = calculateDominantColor,
                getDominantColorAndDrawable = getDominantColorAndDrawable,
                navController = navController
            )
        }
        item {
            LaunchedEffect(true) {
                if (pokemonListEntries.size >= Constants.PAGE_SIZE) {
                    getPokemonsPaginated()
                }
            }
        }
    }
}