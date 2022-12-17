package com.example.pokedex.remote

import com.example.pokedex.models.PokemonListResponse
import com.example.pokedex.models.PokemonResonse
import com.example.pokedex.models.PokemonTypeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAPi {
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offet: Int,
    ): Response<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Response<PokemonResonse>

    @GET("type/{name}")
    suspend fun getPokemonsByType(
        @Path("name") pokemonType: String
    ): Response<PokemonTypeListResponse>
}