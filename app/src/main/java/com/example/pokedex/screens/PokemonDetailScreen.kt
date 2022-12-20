package com.example.pokedex.screens

import android.graphics.drawable.Drawable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.models.PokemonResonse
import com.example.pokedex.models.PokemonStatsResponse
import com.example.pokedex.ui.theme.CustomPurpleBold
import com.example.pokedex.ui.theme.CustomPurpleLight
import com.example.pokedex.ui.theme.CustomPurpleSemiBold
import com.example.pokedex.viewmodels.PokeViewModel

@Composable
fun PokemonDetailScreen(
    pokeViewModel: PokeViewModel, navController: NavController
) {
    BackHandler() {
        navController.popBackStack()
        pokeViewModel.returnedBackFromPokeDetail.value = true
    }
    val searchPokemonState = pokeViewModel.searchedPokemonState.value

    if (!searchPokemonState.isLoading && searchPokemonState.data != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pokeViewModel.dominantColor ?: Color.Red)
        ) {
            PokemonDetailTop(
                searchPokemonState.data,
                modifier = Modifier.weight(1f),
                pokeViewModel.loadedPokemonImage as Drawable,
                pokeViewModel::getPokemonNumberFormatted
            )
            PokemonDetailBottom(
                pokemonDetails = searchPokemonState.data,
                modifier = Modifier.weight(1.5f),
                searchPokemonState.data.pokemonStats,
                pokeViewModel.dominantColor
            )
        }
    }
}

@Composable
fun PokemonDetailTop(
    pokemonDetails: PokemonResonse,
    modifier: Modifier,
    loadedPokemonDrawable: Drawable?,
    getPokeIdFormatted: (Int) -> String
) {
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
                    TextWithShadow(
                        text = getPokeIdFormatted(pokemonDetails.pokemonId),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                        modifier = Modifier
                    )
                }
            }
            AsyncImage(
                modifier = Modifier
                    .wrapContentSize()
                    .scale(1.3f),
                model = ImageRequest.Builder(LocalContext.current).data(loadedPokemonDrawable)
                    .build(),

                contentDescription = "",
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextWithShadow(
                text = pokemonDetails.pokemonName.replaceFirstChar { it.uppercase() },
                fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pokemonDetails.pokemonTypes.forEach {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            text = it.pokemonTypes.name,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun PokemonDetailBottom(
    pokemonDetails: PokemonResonse,
    modifier: Modifier,
    pokemonStats: List<PokemonStatsResponse>,
    dominantColor: Color?
) {
    val maxBaseStat = remember {
        pokemonStats.maxOf { it.statValue }
    }
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
                    Text(
                        text = "Stats",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomPurpleSemiBold,
                        fontFamily = FontFamily(
                            Font(
                                R.font.tt_interfaces_bold
                            )
                        )
                    )
                    Icon(imageVector = Icons.Default.BarChart, contentDescription = "")
                }

                LazyColumn() {
                    items(pokemonStats) {
                        PokemonStat(
                            statName = it.stat.name,
                            statValue = it.statValue.toString(),
                            statMaxValue = maxBaseStat,
                            statColor = dominantColor?.copy(alpha = 0.6f) ?: Color.Green,
                        )
                    }

                }


            }
        }
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: String,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue.toInt() / statMaxValue.toFloat()
        } else 0f, animationSpec = tween(
            animDuration, animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.replaceFirstChar { it.uppercase() },
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
            color = CustomPurpleLight,
            modifier = Modifier.weight(0.6f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
                .weight(1f)
                .height(height)
                .clip(CircleShape)
                .background(
                    Color.LightGray
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(curPercent.value)
                    .clip(CircleShape)
                    .background(statColor)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = (curPercent.value * statMaxValue).toInt().toString(),
                    color = CustomPurpleBold,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TextWithShadow(
    text: String,
    fontSize: TextUnit = 40.sp,
    fontFamily: FontFamily,
    modifier: Modifier
) {
    Box() {
        Text(
            text = text,
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = fontSize,
            modifier = modifier
                .offset(
                    x = 2.dp,
                    y = 2.dp
                )
                .alpha(0.75f)
        )
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = fontSize,
            color = Color.White,
            modifier = modifier
        )
    }
}

