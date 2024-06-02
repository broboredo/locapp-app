package com.abcfestas.locapp.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.abcfestas.locapp.ui.theme.Black
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.ui.theme.Red

@Composable
fun Autocomplete(
    labelName: String,
    onProductSelected: (String?) -> Unit,
    onProductCreated: (String) -> Unit,
    items: List<String>,
) {
    @Composable
    fun AutoComplete(
        label: String,
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onItemSelected: (String) -> Unit,
        items: List<String>,
        modifier: Modifier = Modifier
    ) {
        val focusManager = LocalFocusManager.current
        var expanded by remember { mutableStateOf(false) }

        val filteredItems by remember(searchQuery) {
            derivedStateOf {
                items.filter { item ->
                    searchQuery.isNotBlank() && item.contains(searchQuery, ignoreCase = true) && item.lowercase() != searchQuery.lowercase()
                }
            }
        }

        var textFieldWidth by remember { mutableStateOf(IntSize.Zero) }
        Box(modifier.onSizeChanged {
            textFieldWidth = it
        }) {
            OutlinedTextField(
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = GrayLight,
                    unfocusedIndicatorColor = Gray,
                    errorTextColor = Red,
                    errorLabelColor = Red,
                    errorIndicatorColor = Red,
                    unfocusedContainerColor = GrayLight,
                    unfocusedPlaceholderColor = GrayLight,
                    focusedPlaceholderColor = Gray,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black,
                    focusedLabelColor = Gray,
                    unfocusedLabelColor = Gray
                ),
                placeholder = {
                    Text(text = "mesa rústica 2m")
                },
                label = { Text(text = labelName) },
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotBlank(),
                        enter = fadeIn(animationSpec = tween(350)),
                        exit = fadeOut(animationSpec = tween(450)),
                    ) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Clear")
                        }
                    }
                },
                onValueChange = {
                    expanded = it.isNotBlank()
                    onSearchQueryChanged(it)
                    if (it.isNotEmpty()) {
                        onProductCreated(it)
                        onProductSelected(null)
                    }
                },
                singleLine = true
            )

            DropdownMenu(
                modifier = Modifier
                .then(with(LocalDensity.current) {
                    Modifier.width(width = textFieldWidth.width.toDp())
                })
                .heightIn(max = 200.dp),
                expanded = expanded && filteredItems.isNotEmpty(),
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                },
                properties = PopupProperties(focusable = false, dismissOnBackPress = true, dismissOnClickOutside = true),
            ) {
                filteredItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())
                        },
                        onClick = {
                            onItemSelected(item)
                            onProductSelected(item)
                            expanded = false
                            focusManager.clearFocus()
                        },
                    )
                }
            }
        }
    }

    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        AutoComplete(label = "", modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(), searchQuery = searchQuery, onItemSelected = {
            searchQuery = it
        }, items = items, onSearchQueryChanged = {
            searchQuery = it
        })

    }
}