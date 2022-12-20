package com.example.pokedex.util

object Constants {
    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val PAGE_SIZE = 20
    const val BASE_ICON_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    object LastResponseType{
        const val NORMAL_POKE_LIST = "normal_poke_list"
        const val FILTERED_POKE_LIST = "filtered_poke_list"
        const val SEARCHED_POKE_LIST = "searched_poke_list"
        const val POKE_DETAIL = "poke_detail"
    }
}