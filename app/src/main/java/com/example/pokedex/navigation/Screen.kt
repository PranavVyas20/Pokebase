package com.example.pokedex.navigation

sealed class Screen(val route: String) {
    object PokeListScreen: Screen("pokelist_screen")
    object PokemonDetailScreen: Screen("pokemon_detail_screen")
    object FavPokemonSceen: Screen("fav_pokemon_screen")
}
