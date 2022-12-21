package com.example.pokedex.ui_components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.navigation.Screen
import com.example.pokedex.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun PokeListItem(
    pokemonName: String,
    pokemonFormattedNumber: String,
    pokemonNumber: Int,
    pokemonImageUrl: String,
    calculateDominantColor: ((drawable: Drawable, onFinish: (Color) -> Unit) -> Unit?)?,
    getDominantColorAndDrawable: ((Color?, Drawable?) -> Unit)?,
    getPokemonDetails: (String, Boolean) -> Unit,
    navController: NavController?
) {
    val showShimmer = remember { mutableStateOf(true) }
    val pokemonDominantColor = remember { mutableStateOf(Color.White)}

    val loadedPokemonDrawable = remember {
        mutableStateOf<Drawable?>(null)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .defaultMinSize(40.dp, 60.dp)
            .clickable {
                // Make api call
                if (!showShimmer.value) {
//                    getPokemonDetails(pokemonName.replaceFirstChar { it.lowercaseChar() }, true)
                    if(getDominantColorAndDrawable != null) {
                        getDominantColorAndDrawable(pokemonDominantColor.value,loadedPokemonDrawable.value)
                    }
                    navController?.navigate(Screen.PokemonDetailScreen.route + "/$pokemonName")
                }
            }
            .placeholder(
                visible = showShimmer.value,
                color = shimmerColor,
                shape = RoundedCornerShape(7.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            ),
        backgroundColor = pokemonDominantColor.value,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemonImageUrl)
                    .build(),
                onSuccess = {
                    if(calculateDominantColor != null) {
                        calculateDominantColor(it.result.drawable) { color ->
                            pokemonDominantColor.value = color
                            loadedPokemonDrawable.value = it.result.drawable
                        }
                    }
                    showShimmer.value = false
                },
                onLoading = {
                },
                contentDescription = "",
            )
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pokemonName.replaceFirstChar { it.uppercase() },
                    fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                    color = Color(0xFF442a60)
                )
                Text(
                    text = pokemonFormattedNumber, color = Color(0xFF442a60)
                )
            }
        }
    }
}
