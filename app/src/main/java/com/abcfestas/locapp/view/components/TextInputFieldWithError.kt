package com.abcfestas.locapp.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.ui.theme.Red

@Composable
fun TextInputFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String?,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            keyboardOptions = keyboardOptions,
            placeholder = { Text(text = placeholder) },
            singleLine = singleLine,
            minLines = minLines,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorMessage != null) {
            Text(text = errorMessage, color = Red)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}