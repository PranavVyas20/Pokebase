package com.example.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.view.screens.FavPokemonScreen
import com.example.pokedex.view.screens.PokeListScreen
import com.example.pokedex.view.screens.PokemonDetailScreen
import com.example.pokedex.viewmodel.PokeViewModel

@Composable
fun Navigation(pokeViewModel: PokeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.PokeListScreen.route) {
        composable(route = Screen.PokeListScreen.route) {
            PokeListScreen(pokeViewModel = pokeViewModel, navController)
        }
        composable(
            route = Screen.PokemonDetailScreen.route + "/{pokemon_name}",
            arguments = listOf(
                navArgument("pokemon_name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            PokemonDetailScreen(
                pokeViewModel = pokeViewModel,
                navController = navController,
                pokemonName = entry.arguments?.getString("pokemon_name")
            )
        }
        composable(route = Screen.FavPokemonSceen.route) {
            FavPokemonScreen(pokeViewModel = pokeViewModel, navController = navController)
        }
    }
}