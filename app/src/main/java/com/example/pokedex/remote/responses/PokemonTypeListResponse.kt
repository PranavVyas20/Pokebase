package com.example.pokedex.remote.responses

import com.google.gson.annotations.SerializedName

data class PokemonTypeListResponse(
    @SerializedName("pokemon")val pokemonType: List<PokemonTypeResponse>
)
