package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonStatsResponse(
    @SerializedName("base_stat") val statValue: Int,
    @SerializedName("stat") val stat: PokeListResult
)
