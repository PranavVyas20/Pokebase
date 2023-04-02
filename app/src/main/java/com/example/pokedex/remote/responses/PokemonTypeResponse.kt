package com.example.pokedex.remote.responses

import com.google.gson.annotations.SerializedName

data class PokemonTypeResponse(
    @SerializedName("slot") val pokemonTypeSlot: Int,
    @SerializedName("pokemon") val pokemon: PokeListResult
)
