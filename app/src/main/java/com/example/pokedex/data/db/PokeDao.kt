package com.example.pokedex.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.data.models.PokemonEntity

@Dao
interface PokeDao {

    @Insert
    suspend fun savePokemon(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_table")
    suspend fun getSavedPokemons(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_table WHERE pokemonName == :pokeName")
    suspend fun isPokemonSaved(pokeName: String): PokemonEntity?
}