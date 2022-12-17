package com.example.pokedex.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.*

private data class PokeListFilter(
    val type: String, val bg: Color, val icon: Int
)

private val pokefilters: List<PokeListFilter> = listOf(
    PokeListFilter(type = "Normal", bg = NormalType, icon = R.drawable.pokemon_type_icon_normal),
    PokeListFilter(type = "Water", bg = WaterType, icon = R.drawable.pokemon_type_icon_water),
    PokeListFilter(type = "Bug", bg = BugType, icon = R.drawable.bug_type),
    PokeListFilter(type = "Fairy", bg = FairyType, icon = R.drawable.pokemon_type_icon_fairy),
    PokeListFilter(type = "Fire", bg = FireType, icon = R.drawable.pokemon_type_icon_fire),
    PokeListFilter(type = "Grass", bg = GrassType, icon = R.drawable.pokemon_type_icon_grass),
    PokeListFilter(
        type = "Electric", bg = ElectricType, icon = R.drawable.pokemon_type_icon_electric
    ),
    PokeListFilter(type = "Psychic", bg = PhysicType, icon = R.drawable.pokemon_type_icon_psychic)
)

@Preview
@Composable
fun PokeListFilterCard() {
    Card(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Séarch by type", color = CustomPurpleBold,
                fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                fontSize = 35.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()
            ) {
                items(pokefilters) { item ->
                    PokeListFilterItem(item.type, item.bg, item.icon)
                }
            }
        }
    }
}

@Composable
fun PokeListFilterItem(type: String, bg: Color, icon: Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                       // make api call and hide this filter
            }, backgroundColor = bg, shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = type, modifier = Modifier.align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .height(38.dp)
                    .width(38.dp)
            )
        }
    }
}