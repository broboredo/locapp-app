package com.abcfestas.locapp.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.ui.theme.Black
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.ui.theme.Green500
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24
import compose.icons.octicons.ChevronRight24
import androidx.compose.material3.Button as MaterialButton

@Composable
fun Button(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    outline: Boolean = false,
    mainColor: Color = Green500
) {
    if (!outline) {
        MaterialButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = enabled && !loading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainColor,
                contentColor = Color.White,
                disabledContainerColor = GrayLight,
                disabledContentColor = Gray
            )
        ) {
            Text(text = label)
        }
    } else {
        MaterialButton(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = mainColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth(),
            onClick = onClick,
            enabled = enabled && !loading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = mainColor,
                disabledContainerColor = GrayLight,
                disabledContentColor = Gray
            )
        ) {
            Text(text = label)
        }
    }
}

@Composable
fun ContinueButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    MaterialButton(
        modifier = modifier.size(32.dp),
        shape = CircleShape,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Green500,
            contentColor = Color.White,
            disabledContainerColor = GrayLight,
            disabledContentColor = Gray
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Octicons.ChevronRight24,
            contentDescription = "Next",
            modifier = modifier.padding(0.dp)
        )
    }
}

@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    MaterialButton(
        modifier = modifier.padding(0.dp).size(24.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GrayLight
        ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Octicons.ArrowLeft24,
                contentDescription = "Cancel",
                modifier = modifier.padding(0.dp).fillMaxSize()
            )
        }
    }
}
