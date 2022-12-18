package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonResonse(
    @SerializedName("name") val pokemonName: String,
    @SerializedName("id") val pokemonId: Int,
    @SerializedName("weight") val pokemonWeight: Int,
    @SerializedName("height") val pokemonHeight: Int,
    @SerializedName("types") val pokemonTypes: List<PokemonTypesListPropertyResponse>,
)
