package com.abcfestas.locapp.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.MainColor
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
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null
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
            modifier = Modifier.fillMaxWidth(),
            label = label,
            visualTransformation = if (value.isEmpty()) {
                PlaceholderTransformation(placeholder)
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = MainColor
            ),
        )
        if (isError && errorMessage != null) {
            Text(text = errorMessage, color = Red)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
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
            keyboardOptions = keyboardOptions,
            placeholder = { Text(text = placeholder) },
            singleLine = singleLine,
            minLines = minLines,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

class PlaceholderTransformation(private val placeholder: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return transformedText(text, placeholder)
    }
}

fun transformedText(text: AnnotatedString, placeholder: String): TransformedText {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Gray, fontSize = 14.sp)) {
            append(placeholder)
        }
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return 0
        }

        override fun transformedToOriginal(offset: Int): Int {
            return 0
        }
    }

    return TransformedText(annotatedString, numberOffsetTranslator)
}