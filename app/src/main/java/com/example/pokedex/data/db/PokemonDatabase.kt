package com.example.pokedex.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.data.db.typeConverter.Converter
import com.example.pokedex.remote.responses.PokemonResonse

@Database(entities = [(PokemonResonse::class)], version = 1)
@TypeConverters(Converter::class)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun getPokeDao(): PokeDao

    companion object {
        fun create(context: Context): PokemonDatabase {

            return Room.databaseBuilder(
                context,
                PokemonDatabase::class.java,
                "pokemon_db"
            ).build()
        }
    }

}