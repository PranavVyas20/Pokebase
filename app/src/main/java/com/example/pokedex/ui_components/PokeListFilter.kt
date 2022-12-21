package com.example.pokedex.ui_components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.*
import com.example.pokedex.util.Constants

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
    PokeListFilter(type = "Electric", bg = ElectricType, icon = R.drawable.pokemon_type_icon_electric),
    PokeListFilter(type = "Psychic", bg = PhysicType, icon = R.drawable.pokemon_type_icon_psychic)
)

@Composable
fun PokeListFilterCard(
    changeVisibility: () -> Unit,
    onFilterClick: (String) -> Unit,
    resetUIState: (String) -> Unit,
    clearSearchBox: () -> Unit

) {
    Card(
        modifier = Modifier
            .padding(start = 23.dp, end = 23.dp)
            .shadow(50.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    maxLines = 1,
                    text = "Séarch by type",
                    color = CustomPurpleBold,
                    fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                    fontSize = 25.sp
                )
                Row() {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.Bottom)
//                            .width(24.dp),
                        ,
                        onClick = {
                            //rest the filter and use the already saved list(if present)
                            changeVisibility()
                            resetUIState(Constants.LastResponseType.NORMAL_POKE_LIST)
                            clearSearchBox()
                        }) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Bottom),
                            imageVector = Icons.Default.ClearAll,
                            contentDescription = "",
                            tint = CustomPurpleLight
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .width(24.dp),
                        onClick = {
                            //close the filter card
                            changeVisibility()
                        }) {
                        Icon(
                            modifier = Modifier.align(Alignment.Bottom),
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = CustomPurpleLight
                        )
                    }
                }

            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()
            ) {
                items(pokefilters) { item ->
                    PokeListFilterItem(
                        item.type,
                        item.bg,
                        item.icon,
                        onFilterClick,
                        changeVisibility,
                        clearSearchBox
                    )
                }
            }
        }
    }
}

@Composable
fun PokeListFilterItem(
    type: String,
    bg: Color,
    icon: Int,
    onFilterClick: (String) -> Unit,
    changeVisibility: () -> Unit,
    clearSearchBox: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                // make api call and hide this filter
                onFilterClick(type.replaceFirstChar { it.lowercaseChar() })
                changeVisibility()
                clearSearchBox()
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