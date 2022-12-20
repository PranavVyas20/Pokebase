package com.example.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.screens.PokeListScreen
import com.example.pokedex.screens.PokemonDetailScreen
import com.example.pokedex.viewmodels.PokeViewModel

@Composable
fun Navigation(pokeViewModel: PokeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.PokeListScreen.route) {
        composable(
            route = Screen.PokeListScreen.route
        ) {
            PokeListScreen(pokeViewModel = pokeViewModel, navController)
        }
        composable(route = Screen.PokemonDetailScreen.route) {
            PokemonDetailScreen(pokeViewModel = pokeViewModel, navController = navController)
        }
    }
}