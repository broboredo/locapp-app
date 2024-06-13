package com.abcfestas.locapp.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.util.Constants
import compose.icons.Octicons
import compose.icons.octicons.FileMedia24

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

@Composable
fun LazyImage(
    modifier: Modifier = Modifier,
    url: String?,
    contentScale: ContentScale = ContentScale.Fit,
    description: String? = ""
)
{
    if (url != null) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .addHeader("Security-token", Constants.SECURITY_TOKEN)
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = description,
            contentScale = contentScale,
            modifier = modifier,
            fallback = painterResource(id = R.drawable.img_cant_load),
            error = painterResource(id = R.drawable.img_error)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.img_cant_load),
            contentDescription = description,
            modifier = modifier
        )
    }
}