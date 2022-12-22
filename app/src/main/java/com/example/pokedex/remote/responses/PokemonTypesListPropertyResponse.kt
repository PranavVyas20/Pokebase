package com.example.pokedex.remote.responses

import com.example.pokedex.data.models.PokeListResult
import com.google.gson.annotations.SerializedName

data class PokemonTypesListPropertyResponse(
    @SerializedName("type") val pokemonTypes: PokeListResult
)
