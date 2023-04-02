package com.example.pokedex.data.models

import android.graphics.Bitmap
import com.example.pokedex.remote.responses.PokemonStatsResponse
import com.example.pokedex.remote.responses.PokemonTypesListPropertyResponse

data class Pokemon(
    val pokemonName: String,
    val pokemonId: Int,
    var pokemonImage: Bitmap?,
    val pokemonWeight: Int,
    val pokemonHeight: Int,
    val pokemonTypes: List<PokemonTypesListPropertyResponse>,
    val pokemonStats: List<PokemonStatsResponse>
)

fun Pokemon.toPokemonEntity(): PokemonEntity =
    PokemonEntity(
        pokemonName = pokemonName,
        pokemonId = pokemonId,
        pokemonImage = pokemonImage,
        pokemonWeight = pokemonWeight,
        pokemonHeight = pokemonHeight,
        pokemonTypes = pokemonTypes,
        pokemonStats = pokemonStats
    )
