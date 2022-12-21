package com.example.pokedex.repository

import android.util.Log
import com.example.pokedex.db.PokeDao
import com.example.pokedex.models.LocalPokemon
import com.example.pokedex.models.PokemonListResponse
import com.example.pokedex.models.PokemonResonse
import com.example.pokedex.models.PokemonTypeListResponse
import com.example.pokedex.remote.PokeAPi
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xdroid.toaster.Toaster

class PokeRepository(private val pokeApi: PokeAPi, private val pokeDao: PokeDao) {

    suspend fun getPokemonsPaginated(limit: Int, offset: Int): Flow<Resource<PokemonListResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = pokeApi.getPokemons(limit, offset)
                if (response.code() == 200) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error("Some error occured"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("${e.message}"))
            }
        }
    }

    suspend fun getPokemon(name: String): Flow<Resource<PokemonResonse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = pokeApi.getPokemon(name)
                if (response.code() == 200) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    val errorMsg = if(response.code() == 404) {
                        "No matching results"
                    } else {
                        "Some error occured"
                    }
                    emit(Resource.Error(errorMsg))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Some error occured"))
            }
        }
    }

    suspend fun getPokemonbyType(pokemonType: String): Flow<Resource<PokemonTypeListResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = pokeApi.getPokemonsByType(pokemonType)
                if (response.code() == 200) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error("Some error occured"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("${e.message}"))
            }
        }
    }

    suspend fun savePokemon(pokemon: PokemonResonse): Flow<Resource<Boolean>> {
        Log.d("pokeDb","repo")

        return flow {
            Log.d("pokeDb","flow")

            try {
                pokeDao.savePokemon(pokemon)
                emit(Resource.Success(data = true))
                Toaster.toast("Pokemon captured!")
                Log.d("pokeDb","saved succesfully")

            } catch (e: Exception) {
                Log.d("pokeDb","saved succesfully")
                emit(Resource.Error(data = false, message = "${e.message}"))
            }
        }
    }

    suspend fun getPokemonFromDb(pokemonName: String): PokemonResonse? {
        return pokeDao.isPokemonSaved(pokemonName.replaceFirstChar { it.lowercaseChar() })
    }

    suspend fun deletePokemon(pokemon: PokemonResonse): Flow<Resource<Boolean>> {
        return flow {
            try {
                pokeDao.delete(pokemon)
                emit(Resource.Success(data = true))
            } catch (e: Exception) {
                emit(Resource.Error(data = false, message = "${e.message}"))
            }
        }
    }

    suspend fun getSavedPokemons(): Flow<Resource<List<PokemonResonse>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val savedPokemons = pokeDao.getSavedPokemons()
                emit(Resource.Success(data = savedPokemons))
            } catch (e: Exception) {
                emit(Resource.Error("${e.message}"))
            }
        }
    }
}




