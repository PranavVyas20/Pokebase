package com.example.pokedex.di

import androidx.compose.ui.unit.Constraints
import com.example.pokedex.remote.PokeAPi
import com.example.pokedex.repository.PokeRepository
import com.example.pokedex.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePokeApi(): PokeAPi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeAPi::class.java)
    }

    @Provides
    @Singleton
     fun providePokeRepository(pokeApi: PokeAPi): PokeRepository{
        return PokeRepository(pokeApi)
    }
}