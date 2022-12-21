package com.example.pokedex


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokedex.navigation.Navigation
import com.example.pokedex.screens.PokeListScreen
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodels.PokeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            PokedexTheme {
                val pokeViewModel = hiltViewModel<PokeViewModel>()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = Color.Red
                ) {
                    Navigation(pokeViewModel = pokeViewModel)
                }
            }
        }
    }
}
