package com.example.pokedex.ui_components

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.example.pokedex.models.PokeListEntry
import com.example.pokedex.screens.header

@Composable
fun PokeList(
    pokemonListEntries: List<PokeListEntry>,
    calculateDominantColor: (drawable: Drawable, onFinish: (Color) -> Unit) -> Unit,
    getPokemonsPaginated: () -> Unit,
    onSearchPressed: (query: String) -> Unit,
    clearSearchBar: () -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        header {
            PokeListHeader(onSearchPressed, clearSearchBar)
        }
        items(pokemonListEntries) { pokeListEntry ->
            PokeListItem(
                pokeListEntry.pokemonName,
                pokeListEntry.number,
                pokeListEntry.imageUrl,
                calculateDominantColor
            )
        }
        item {
            LaunchedEffect(true) {
                if(pokemonListEntries.size > 1) {
                    Log.d("pokemonsPaginatedEol", "offsetidk")
                    getPokemonsPaginated()
                }
            }
        }
    }
}