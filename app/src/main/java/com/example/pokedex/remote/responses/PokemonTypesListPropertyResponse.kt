package com.example.pokedex.remote.responses

import com.google.gson.annotations.SerializedName

data class PokemonTypesListPropertyResponse(
    @SerializedName("type") val pokemonTypes: PokeListResult
)
