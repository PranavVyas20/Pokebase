package com.example.pokedex.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.CustomPurpleLight
import com.example.pokedex.ui.theme.CustomPurpleSemiBold

@Preview
@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf("") }
    TextField(value = searchQuery.value, onValueChange = {
        searchQuery.value = it
    }, placeholder = {
        Text(
            text = "Name",
            fontFamily = FontFamily(Font(R.font.varela_round)),
            color = CustomPurpleLight
        )
    }, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp), leadingIcon = {
            Icon(Icons.Default.Search, "", tint = CustomPurpleLight)
        }, colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFe5f5f5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}