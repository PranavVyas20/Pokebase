package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonTypesListPropertyResponse(
    @SerializedName("type") val pokemonTypes: PokeListResult
)
