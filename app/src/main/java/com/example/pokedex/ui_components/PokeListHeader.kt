package com.example.pokedex.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokedex.R
import com.example.pokedex.navigation.Screen
import com.example.pokedex.ui.theme.CustomPurpleBold
import com.example.pokedex.ui.theme.CustomPurpleLight

@Composable
fun PokeListHeader(
    searchBoxText: String,
    onSearchPressed: (query: String, Boolean) -> Unit,
    onClickDismissed: () -> Unit,
    clearSearchBox: () -> Unit,
    navController: NavController,
    updateSearchBox: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Pokédex", color = CustomPurpleBold,
                fontFamily = FontFamily(Font(R.font.tt_interfaces_bold)),
                fontSize = 35.sp,
            )
            IconButton(onClick = { navController.navigate(Screen.FavPokemonSceen.route) }) {
                Icon(
                    imageVector = Icons.Outlined.Bookmarks,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }

        Text(
            text = "Search for a Pokemon by name",
            color = CustomPurpleLight,
            fontFamily = FontFamily(Font(R.font.varela_round))
        )
        SearchBar(
            searchBoxText = searchBoxText,
            onSearchPressed,
            onClickDismissed,
            clearSearchBox,
            updateSearchBox
        )

    }
}