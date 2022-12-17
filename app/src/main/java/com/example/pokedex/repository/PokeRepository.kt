package com.example.pokedex.repository

import com.example.pokedex.models.PokemonListResponse
import com.example.pokedex.models.PokemonResonse
import com.example.pokedex.models.PokemonTypeListResponse
import com.example.pokedex.remote.PokeAPi
import com.example.pokedex.util.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokeRepository(private val pokeApi: PokeAPi) {

    suspend fun getPokemons(limit: Int, offset: Int): Flow<ApiResponse<PokemonListResponse>> {
        return flow {
            emit(ApiResponse.Loading())
            try {
                val response = pokeApi.getPokemons(limit, offset)
                if (response.code() == 200) {
                    emit(ApiResponse.Success(response.body()!!))
                } else {
                    emit(ApiResponse.Error("Some error occured"))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error("Some error occured"))
            }
        }
    }

    suspend fun getPokemon(name: String): Flow<ApiResponse<PokemonResonse>> {
        return flow {
            emit(ApiResponse.Loading())
            try {
                val response = pokeApi.getPokemon(name)
                if (response.code() == 200) {
                    emit(ApiResponse.Success(response.body()!!))
                } else {
                    emit(ApiResponse.Error("Some error occured"))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error("Some error occured"))
            }
        }
    }

    suspend fun getPokemonbyType(pokemonType:String): Flow<ApiResponse<PokemonTypeListResponse>> {
        return flow {
            emit(ApiResponse.Loading())
            try {
                val response = pokeApi.getPokemonsByType(pokemonType)
                if (response.code() == 200) {
                    emit(ApiResponse.Success(response.body()!!))
                } else {
                    emit(ApiResponse.Error("Some error occured"))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error("Some error occured"))
            }
        }
    }
}
