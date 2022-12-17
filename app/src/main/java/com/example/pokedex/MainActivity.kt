package com.example.pokedex


import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.example.pokedex.screens.PokeListScreen
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui_components.PokeListFilterCard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = Color.Red
                ) {
                    PokeListScreen()
//                    Column(modifier = Modifier.fillMaxSize()) {
//                        AsyncImage(
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png")
//                                .build(),
//                            onSuccess = {
//                                        println(it.result.drawable.toString())
//                            },
//                            onLoading = {
//
//                            },
//                            contentDescription = "",
//                        )
//                        dominateColorView()

//                    }

                }
            }
        }
    }
}


@Composable
fun dominateColorView() {
    val colors: MutableList<Pair<Color, String>> = remember {
        mutableStateListOf()
    }
    val tempColors: MutableList<Pair<Color, String>> = mutableListOf()

    val bmp = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.poke_test)

    LaunchedEffect(key1 = Unit) {
        Palette.from(bmp).generate { pallete ->
            pallete?.swatches?.forEach { it ->
                tempColors.add(Pair(Color(it.rgb), it.bodyTextColor.toString()))
            }
            colors.addAll(tempColors)
            println(colors.toString())
        }
    }

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        items(colors) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .height(30.dp)
                    .width(30.dp)
                    .background(it.first)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.poke_test),
//                    contentDescription = "Pokemon img",
//                    modifier = Modifier
//                        .align(
//                            Alignment.Center
//                        ).clip(RoundedCornerShape(10.dp))
//                )
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokedexTheme {
        Greeting("Android")
    }
}