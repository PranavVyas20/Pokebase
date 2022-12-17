package com.example.pokedex.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokedex.ui.theme.CustomPurpleSemiBold
import com.example.pokedex.ui_components.PokeList
import com.example.pokedex.ui_components.PokeListFilterCard
import com.example.pokedex.ui_components.PokeListHeader
import com.example.pokedex.ui_components.PokeListItem

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@Composable
fun PokeListScreen() {
    val filterScreenVisibility = remember { mutableStateOf(false) }
    val blackScreenVisibility = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            PokeList()
        }

        // Black Box
        if (blackScreenVisibility.value) {
            Box(modifier = Modifier
                .clickable {
                    filterScreenVisibility.value = !filterScreenVisibility.value
                    blackScreenVisibility.value = !blackScreenVisibility.value
                }
                .background(Color.Black.copy(alpha = 0.3f))
                .fillMaxSize()) {}
        }

        // Filter screen
        AnimatedVisibility(
            visible = filterScreenVisibility.value,
            modifier = Modifier.align(Alignment.Center)
        ) {
            PokeListFilterCard()
        }
        IconButton(
            onClick = {
                filterScreenVisibility.value = !filterScreenVisibility.value
                blackScreenVisibility.value = !blackScreenVisibility.value
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .background(CustomPurpleSemiBold, RoundedCornerShape(7.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Tune, contentDescription = "", tint = Color(0xFFf0fcfb)
            )
        }
    }


}