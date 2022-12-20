package com.example.pokedex.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokedex.ui.theme.CustomPurpleSemiBold
import com.example.pokedex.ui_components.PokeList
import com.example.pokedex.ui_components.PokeListFilterCard
import com.example.pokedex.util.Constants
import com.example.pokedex.viewmodels.PokeViewModel

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@Composable
fun PokeListScreen(pokeViewModel: PokeViewModel, navController: NavController) {
    val filterScreenVisibility = pokeViewModel.filterScreenVisibility
    val blackScreenVisibility = pokeViewModel.blackScreenVisibility
    val pokemonsState = pokeViewModel.pokemonsState.value
    val searchBoxText = pokeViewModel.searchBoxText

    LaunchedEffect(key1 = Unit) {
        Log.d("launchedEffect", "runned: ${pokeViewModel.lastResponseType}")
        if (!pokeViewModel.returnedBackFromPokeDetail.value) {
            pokeViewModel.getPokemonsPaginated()
        } else {
            pokeViewModel.returnedBackFromPokeDetail.value = true
        }
    }
    val activity = LocalContext.current as? Activity

    BackHandler() {
        if (pokeViewModel.lastResponseType == Constants.LastResponseType.SEARCHED_POKE_LIST || pokeViewModel.lastResponseType == Constants.LastResponseType.POKE_DETAIL) {
            pokeViewModel.resetUIState(Constants.LastResponseType.NORMAL_POKE_LIST)
            pokeViewModel.clearSearchBox()
        } else {
            activity?.finish()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            PokeList(
                searchBoxText,
                pokemonsState.data!!,
                pokeViewModel::calcDominantColor,
                pokeViewModel::getPokemonsPaginated,
                pokeViewModel::getPokemon,
                pokeViewModel::resetUIState,
                pokeViewModel::clearSearchBox,
                pokeViewModel::updateSearchBox,
                pokeViewModel::getDominantColorAndDrawable,
                pokeViewModel::getPokemon,
                navController
            )
        }

        // Black Box
        if (blackScreenVisibility) {
            Box(modifier = Modifier
                .clickable {
                    pokeViewModel.changeFilterScreenVisibilty()
                }
                .background(Color.Black.copy(alpha = 0.3f))
                .fillMaxSize()) {}
        }

        // Filter screen
        AnimatedVisibility(
            visible = filterScreenVisibility,
            modifier = Modifier.align(Alignment.Center)
        ) {
            PokeListFilterCard(
                changeVisibility = pokeViewModel::changeFilterScreenVisibilty,
                onFilterClick = pokeViewModel::getPokemonByType,
                resetUIState = pokeViewModel::resetUIState,
                clearSearchBox = pokeViewModel::clearSearchBox
            )
        }
        IconButton(
            onClick = {
                pokeViewModel.changeFilterScreenVisibilty()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 10.dp)
                .background(CustomPurpleSemiBold, RoundedCornerShape(7.dp))
        ) {
            Icon(
                modifier = Modifier.padding(18.dp),
                imageVector = Icons.Default.Tune, contentDescription = "", tint = Color(0xFFf0fcfb)
            )
        }
    }


}