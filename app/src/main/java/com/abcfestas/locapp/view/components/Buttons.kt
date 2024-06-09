package com.abcfestas.locapp.view.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.ui.theme.Black
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.ui.theme.Green500
import com.abcfestas.locapp.ui.theme.MainColor
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
    val contentAlpha by animateFloatAsState(targetValue = if (loading) 0f else 1f, label = "")
    val loadingAlpha by animateFloatAsState(targetValue = if (loading) 1f else 0f, label = "")

    val buttonColors = if (outline) {
        ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = mainColor,
            disabledContainerColor = GrayLight,
            disabledContentColor = Gray
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.White,
            disabledContainerColor = GrayLight,
            disabledContentColor = Gray
        )
    }

    val buttonModifier = if (outline) {
        modifier
            .border(
                width = 1.dp,
                color = mainColor,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
    } else {
        modifier.fillMaxWidth()
    }

    MaterialButton(
        modifier = buttonModifier,
        onClick = onClick,
        enabled = enabled && !loading,
        shape = RoundedCornerShape(8.dp),
        colors = buttonColors
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            if (loading) {
                LoadingIndicator(
                    modifier = Modifier.graphicsLayer { alpha = loadingAlpha },
                    color = mainColor
                )
            } else {
                Text(
                    text = label,
                    modifier = Modifier.graphicsLayer { alpha = contentAlpha }
                )
            }
        }
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


@Composable
private fun LoadingIndicator(
    animating: Boolean = true,
    modifier: Modifier = Modifier,
    color: Color = MainColor
) {
    val animatedValues = List(3) { index ->
        var animatedValue by remember(key1 = animating) { mutableFloatStateOf(0f) }
        LaunchedEffect(key1 = animating) {
            if (animating) {
                animate(
                    initialValue = 12 / 2f,
                    targetValue = -12 / 2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 300),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(300 * index),
                    ),
                ) { value, _ -> animatedValue = value }
            }
        }
        animatedValue
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        animatedValues.forEach { animatedValue ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(12.dp)
                    .aspectRatio(1f)
                    .offset(y = animatedValue.dp),
                color = color,
            )
        }
    }
}
@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
    )
}