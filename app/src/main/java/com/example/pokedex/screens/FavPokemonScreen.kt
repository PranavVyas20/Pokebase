package com.example.pokedex.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pokedex.ui_components.PokeListItem
import com.example.pokedex.viewmodels.PokeViewModel


@Composable
fun FavPokemonScreen(pokeViewModel: PokeViewModel, navController: NavController) {
    val savedPokeState = pokeViewModel.savedPokemonState.value
    Log.d("savedPokeState", savedPokeState.toString())

    LaunchedEffect(key1 = Unit) {
        pokeViewModel.getSavedPokemons()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (savedPokeState.data!!.isEmpty()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)) {
                Text(text = "No saved data available")
            }
        } else {
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth(), columns = GridCells.Fixed(2)) {
                items(savedPokeState.data) {
                    PokeListItem(
                        pokemonName = it.pokemonName,
                        pokemonFormattedNumber = it.formattedNumber,
                        pokemonNumber = it.number,
                        pokemonImageUrl = it.imageUrl,
                        calculateDominantColor = null,
                        getDominantColorAndDrawable = null,
                        getPokemonDetails = pokeViewModel::getPokemon,
                        navController = navController
                    )
                }
            }
        }
    }
}