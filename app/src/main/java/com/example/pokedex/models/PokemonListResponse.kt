package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val nextPageResponseUrl: String?,
    @SerializedName("previous") val prevPageResponseUrl: String?,
    @SerializedName("results") val results: List<PokeListResult>
)
