package com.example.pokedex.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.CustomPurpleLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchBoxText: String,
    onSearchPressed: (query: String, Boolean) -> Unit,
    onClickDismissed: () -> Unit,
    clearSearchBox: () -> Unit,
    updateSearchBox: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val showDismissButton = remember {
        mutableStateOf(false)
    }

    TextField(
        value = searchBoxText,
        onValueChange = {
            updateSearchBox(it)
            showDismissButton.value = true
        },
        placeholder = {
            Text(
                text = "Name",
                fontFamily = FontFamily(Font(R.font.varela_round)),
                color = CustomPurpleLight
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    showDismissButton.value = true
                }
            },
        shape = RoundedCornerShape(8.dp),
        leadingIcon = { Icon(Icons.Default.Search, "", tint = CustomPurpleLight) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFe5f5f5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            if (showDismissButton.value) {
                IconButton(onClick = {
                    if (searchBoxText.isNotEmpty()) {
                        // load already saved pokemon list
                        onClickDismissed()
                        clearSearchBox()
                    }
                    showDismissButton.value = false
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = CustomPurpleLight
                    )
                }
            }
        },
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = {
            // Search for pokemon
            CoroutineScope(Dispatchers.IO).launch {
                onSearchPressed(searchBoxText, false)
            }
            // remove focus
            focusManager.clearFocus()
            // dismiss keyboard
            keyboardController?.hide()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}