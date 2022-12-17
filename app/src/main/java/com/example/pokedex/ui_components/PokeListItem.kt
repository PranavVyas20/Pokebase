package com.example.pokedex.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.R

@Preview
@Composable
fun PokeListItem() {
    Card(
        modifier = Modifier.padding(8.dp),
        backgroundColor = Color(0xFFb9e8d5),
        shape = RoundedCornerShape(15.dp)
    ) {
        PokeListItemDetails(
            pokemonName = "Bulbasaur", imageUrl = "", idNumber = "001", bgColor = 0
        )
    }
}

@Composable
fun PokeListItemDetails(
    pokemonName: String, imageUrl: String, idNumber: String, bgColor: Int
) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Image(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            painter = painterResource(id = R.drawable.poke_test),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pokemonName,
                fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                color = Color(0xFF442a60)
            )
            Text(
                text = idNumber, color = Color(0xFF442a60)
            )
        }
    }
}
