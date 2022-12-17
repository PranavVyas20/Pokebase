package com.example.pokedex.ui_components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import com.example.pokedex.screens.header

@Composable
fun PokeList() {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        header {
            PokeListHeader()
        }
        items(37) {
            PokeListItem()
        }
    }
}