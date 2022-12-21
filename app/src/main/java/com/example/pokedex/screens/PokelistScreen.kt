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
    val pokemonsState = pokeViewModel.pokemonState.value
    val searchBoxText = pokeViewModel.searchBoxText
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        if (!pokeViewModel.returnedBackFromPokeDetail.value) {
            pokeViewModel.getPokemonsPaginated()
        } else {
            pokeViewModel.returnedBackFromPokeDetail.value = true
        }

//        if (!pokeViewModel.returnedBackFromPokeDetail.value) {
//            pokeViewModel.getPokemonsPaginated()
//            pokeViewModel.getSavedPokemons()
//            pokeViewModel.lastResponseType = Constants.LastResponseType.NORMAL_POKE_LIST
//
//        } else {
//            pokeViewModel.returnedBackFromPokeDetail.value = false
//            pokeViewModel.lastResponseType = Constants.LastResponseType.SEARCHED_POKE_LIST
//        }

    }
    val activity = context as? Activity
    BackHandler() {
        if (pokemonsState.data!!.size > 1) {
            activity?.finish()
        } else {
            Log.d("lastListType", pokeViewModel.lastListType)
            // Currently on searched poke screen
            if (pokeViewModel.lastListType == Constants.LastResponseType.NORMAL_POKE_LIST) {
                pokeViewModel.resetUIState(Constants.LastResponseType.NORMAL_POKE_LIST)
            } else if (pokeViewModel.lastListType == Constants.LastResponseType.FILTERED_POKE_LIST) {
                pokeViewModel.resetUIState(Constants.LastResponseType.FILTERED_POKE_LIST)
            }
            pokeViewModel.clearSearchBox()
        }
    }

    // Check for null data here and show error screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            PokeList(
                searchBarText = searchBoxText,
                pokemonListEntries = pokemonsState.data!!,
                calculateDominantColor = pokeViewModel::calcDominantColor,
                getPokemonsPaginated = pokeViewModel::getPokemonsPaginated,
                onSearchPressed = pokeViewModel::getPokemon,
                onClickDismissed = pokeViewModel::resetUIState,
                clearSearchBox = pokeViewModel::clearSearchBox,
                updateSearchBox = pokeViewModel::updateSearchBox,
                getDominantColorAndDrawable = pokeViewModel::getDominantColorAndDrawable,
                getPokemonDetails = pokeViewModel::getPokemon,
                navController = navController
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