package com.example.pokedex.remote.responses

import com.example.pokedex.data.models.PokeListEntry
import com.example.pokedex.util.Constants
import com.google.gson.annotations.SerializedName

data class PokeListResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

internal fun PokeListResult.toPokeListEntry(): PokeListEntry {
    val pokeNumber = if (this.url.endsWith("/")) {
        this.url.dropLast(1)
            .takeLastWhile { it.isDigit() }
    } else {
        this.url.takeLastWhile { it.isDigit() }
    }
    val pokeImageUrl = Constants.BASE_ICON_URL + pokeNumber + ".png"

    return PokeListEntry(
        this.name,
        pokeImageUrl,
        getPokemonNumberFormatted(pokeNumber.toInt()),
        number = pokeNumber.toInt()
    )
}

internal fun List<PokeListResult>.toPokeListEntries(): List<PokeListEntry> {
    return this.map { it.toPokeListEntry() }
}

fun getPokemonNumberFormatted(pokeNumber: Int): String {
    val maxLength = 3
    var additionalZeros = ""
    val pokeNoFormatted = pokeNumber.toString()
    repeat(maxLength - pokeNoFormatted.length) {
        additionalZeros += '0'
    }
    return "#$additionalZeros$pokeNoFormatted"
}


