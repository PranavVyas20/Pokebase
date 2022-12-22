package com.example.pokedex.remote.responses

import com.example.pokedex.data.models.PokeListResult
import com.google.gson.annotations.SerializedName

data class PokemonStatsResponse(
    @SerializedName("base_stat") val statValue: Int,
    @SerializedName("stat") val stat: PokeListResult
)
