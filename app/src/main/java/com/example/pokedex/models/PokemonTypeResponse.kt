package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonTypeResponse(
    @SerializedName("slot") val pokemonTypeSlot: Int,
    @SerializedName("pokemon") val pokemon: PokeListResult
)
