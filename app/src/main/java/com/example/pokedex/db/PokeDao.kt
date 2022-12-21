package com.example.pokedex.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.models.LocalPokemon
import com.example.pokedex.models.PokemonResonse
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeDao {

    @Insert
    suspend fun savePokemon(pokemon: PokemonResonse)

    @Delete
    suspend fun delete(pokemon: PokemonResonse)

    @Query("SELECT * FROM pokemon_table")
    suspend fun getSavedPokemons(): List<PokemonResonse>

    @Query("SELECT * FROM pokemon_table WHERE pokemonName == :pokeName")
    suspend fun isPokemonSaved(pokeName: String): PokemonResonse?
}