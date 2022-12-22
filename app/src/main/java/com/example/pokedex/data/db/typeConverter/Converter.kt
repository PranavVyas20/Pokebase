package com.example.pokedex.data.db.typeConverter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.pokedex.remote.responses.PokemonStatsResponse
import com.example.pokedex.remote.responses.PokemonTypesListPropertyResponse
import com.google.gson.Gson
import java.io.ByteArrayOutputStream

class Converter {
    @TypeConverter
    fun convertToByteArray(btmp: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        btmp.compress(Bitmap.CompressFormat.PNG,50, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun convertToBitmap(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    @TypeConverter
    fun listToJsonStringI(value: List<PokemonTypesListPropertyResponse>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToListI(value: String) = Gson().fromJson(value, Array<PokemonTypesListPropertyResponse>::class.java).toList()

    @TypeConverter
    fun listToJsonString(value: List<PokemonStatsResponse>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<PokemonStatsResponse>::class.java).toList()
}