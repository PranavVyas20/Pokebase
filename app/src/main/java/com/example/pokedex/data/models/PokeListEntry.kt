package com.example.pokedex.data.models

import android.graphics.Bitmap

data class PokeListEntry(
    val pokemonName: String,
    val imageUrl: String,
    val formattedNumber: String,
    val number: Int,
    var savedPokeImage: Bitmap? = null
)
