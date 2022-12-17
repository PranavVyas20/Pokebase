package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonTypeResponse(
    @SerializedName("name") val pokemonType: String
)
