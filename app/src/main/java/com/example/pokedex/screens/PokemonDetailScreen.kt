package com.example.pokedex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

val dominantColor = Color.Red

@Composable
@Preview
// need a dominant color here
fun PokemonDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
    ) {
        PokemonDetailTop(modifier = Modifier.weight(1f))
        PokemonDetailBottom(modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun PokemonDetailTop(modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    Text(text = "#003")
                }
            }
            AsyncImage(
                modifier = Modifier.wrapContentSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png")
                    .build(),
                onSuccess = {
                    println(it.result.drawable.toString())
                },
                onLoading = {

                },
                contentDescription = "",
            )
            Text(text = "{pokemon_name}")
            Row() {
                Text(text = "{pokemon_type}")
                Text(text = "{pokemon_type}")
            }
        }
    }

}

@Composable
fun PokemonDetailBottom(modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    Color.White, RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                )
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Stats")
                    Icon(imageVector = Icons.Default.BarChart, contentDescription = "")
                }
                LazyColumn() {
                    items(6) {
                        PokemonStats()
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PokemonStats() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "{stat_name}")
        Text(text = "{stat_amnt}")
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Blue)
                .fillMaxWidth()
                .height(5.dp)
        )
    }
}