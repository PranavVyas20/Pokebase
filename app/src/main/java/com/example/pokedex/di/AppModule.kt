package com.example.pokedex.di

import android.content.Context
import com.example.pokedex.db.PokeDao
import com.example.pokedex.db.PokemonDatabase
import com.example.pokedex.remote.PokeAPi
import com.example.pokedex.repository.PokeRepository
import com.example.pokedex.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePokeApi(): PokeAPi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeAPi::class.java)
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase =
        PokemonDatabase.create(context)

    @Provides
    @Singleton
    fun provideDao(database: PokemonDatabase): PokeDao {
        return database.getPokeDao()
    }

    @Provides
    @Singleton
    fun providePokeRepository(pokeApi: PokeAPi, pokeDao: PokeDao): PokeRepository {
        return PokeRepository(pokeApi, pokeDao)
    }
}