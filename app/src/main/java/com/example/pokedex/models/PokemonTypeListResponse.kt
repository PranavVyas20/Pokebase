package com.example.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonTypeListResponse(
    @SerializedName("type")val pokemonType: PokemonTypeResponse
)
