package com.example.pokedex.remote.responses

import android.graphics.Bitmap
import com.example.pokedex.data.models.PokeListEntry
import com.example.pokedex.data.models.Pokemon
import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("name") val pokemonName: String,

    @SerializedName("id") val pokemonId: Int,

    var pokemonImage: Bitmap?,

    @SerializedName("weight") val pokemonWeight: Int,

    @SerializedName("height") val pokemonHeight: Int,

    @SerializedName("types") val pokemonTypes: List<PokemonTypesListPropertyResponse>,

    @SerializedName("stats") val pokemonStats: List<PokemonStatsResponse>
)

internal fun PokemonResponse.toPokeListEntry(): PokeListEntry =
    PokeListEntry(
        pokemonName = pokemonName,
        imageUrl = pokemonName,
        formattedNumber = getPokemonNumberFormatted(pokemonId),
        number = pokemonId
    )

internal fun PokemonResponse.toPokemon(): Pokemon =
    Pokemon(
        pokemonName = pokemonName,
        pokemonId = pokemonId,
        pokemonImage = pokemonImage,
        pokemonWeight = pokemonWeight,
        pokemonHeight = pokemonHeight,
        pokemonTypes = pokemonTypes,
        pokemonStats = pokemonStats
    )
