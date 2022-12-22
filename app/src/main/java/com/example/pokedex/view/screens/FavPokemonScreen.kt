package com.example.pokedex.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokedex.R
import com.example.pokedex.ui.theme.CustomPurpleBold
import com.example.pokedex.view.ui_components.PokeListItem
import com.example.pokedex.viewmodel.PokeViewModel


@Composable
fun FavPokemonScreen(pokeViewModel: PokeViewModel, navController: NavController) {
    val savedPokeState = pokeViewModel.savedPokemonState.value

    LaunchedEffect(key1 = Unit) {
        pokeViewModel.getSavedPokemons()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD8DEE9))
    ) {
        if (savedPokeState.data!!.isEmpty()) {
            Row(modifier = Modifier
                .align(Alignment.Center)) {
                Text(text = "You haven't captured any pokemons yet!")
            }
        } else {
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp), columns = GridCells.Fixed(2)) {
                header {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Captured Pokemons", color = CustomPurpleBold,
                            fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                            fontSize = 35.sp,
                        )
                    }
                }
                items(savedPokeState.data) {
                    PokeListItem(
                        pokemonName = it.pokemonName,
                        pokemonFormattedNumber = it.formattedNumber,
                        pokemonImageUrl = it.imageUrl,
                        calculateDominantColor = null,
                        getDominantColorAndDrawable = null,
                        navController = navController
                    )
                }
            }
        }
    }
}