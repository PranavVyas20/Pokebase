package com.example.pokedex.data.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.remote.responses.PokemonStatsResponse
import com.example.pokedex.remote.responses.PokemonTypesListPropertyResponse

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    val pokemonName: String,

    @PrimaryKey
    val pokemonId: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var pokemonImage: Bitmap?,

    val pokemonWeight: Int,

    val pokemonHeight: Int,

    @ColumnInfo(name = "pokemonTypes")
    val pokemonTypes: List<PokemonTypesListPropertyResponse>,

    @ColumnInfo(name = "pokemonStats")
    val pokemonStats: List<PokemonStatsResponse>
)

internal fun PokemonEntity.toPokemon(): Pokemon =
    Pokemon(
        pokemonName = pokemonName,
        pokemonId = pokemonId,
        pokemonImage = pokemonImage,
        pokemonWeight = pokemonWeight,
        pokemonHeight = pokemonHeight,
        pokemonTypes = pokemonTypes,
        pokemonStats = pokemonStats
    )