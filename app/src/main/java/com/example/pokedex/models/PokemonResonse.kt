package com.example.pokedex.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon_table")
data class PokemonResonse(
    @SerializedName("name") val pokemonName: String,

    @PrimaryKey
    @SerializedName("id") val pokemonId: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var pokemonImage:Bitmap?,

    @SerializedName("weight") val pokemonWeight: Int,

    @SerializedName("height") val pokemonHeight: Int,

    @ColumnInfo(name = "pokemonTypes")
    @SerializedName("types") val pokemonTypes: List<PokemonTypesListPropertyResponse>,

    @ColumnInfo(name = "pokemonStats")
    @SerializedName("stats") val pokemonStats: List<PokemonStatsResponse>
)
