package com.example.pokedex.view.screens


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokedex.navigation.Navigation
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodel.PokeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            PokedexTheme {
                val pokeViewModel = hiltViewModel<PokeViewModel>()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Navigation(pokeViewModel = pokeViewModel)
                }
            }
        }
    }
}
