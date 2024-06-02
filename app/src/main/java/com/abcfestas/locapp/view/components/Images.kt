package com.abcfestas.locapp.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.ui.theme.GrayLight
import compose.icons.Octicons
import compose.icons.octicons.FileMedia24

@Composable
fun RoundedImage(
    modifier: Modifier = Modifier,
    imageResource: Int? = null,
    contentDescription: String? = null,
    modifierImage: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = GrayLight,
                shape = RoundedCornerShape(8.dp)
            )
            .height(74.66.dp)
            .width(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (imageResource != null) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = contentDescription,
                modifier = modifierImage
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                imageVector = Octicons.FileMedia24,
                contentDescription = "Image not found"
            )
        }
    }
}

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    imageResource: Int? = null,
    contentDescription: String? = null,
    modifierImage: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = GrayLight,
                shape = CircleShape
            )
            .height(56.dp)
            .width(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (imageResource != null) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = contentDescription,
                modifier = modifierImage
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                imageVector = Octicons.FileMedia24,
                contentDescription = "Image not found"
            )
        }
    }
}