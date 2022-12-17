package com.example.pokedex.ui_components
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.*

@Preview
@Composable
fun PokeListHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
                text = "Pokédex", color = CustomPurpleBold,
                fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                fontSize = 35.sp,
            )
        Text(
            text = "Search for a Pokemon by name",
            color = CustomPurpleLight,
            fontFamily = FontFamily(Font(R.font.varela_round))
        )
        SearchBar()
    }
}