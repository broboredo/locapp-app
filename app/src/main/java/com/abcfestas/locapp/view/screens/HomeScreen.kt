package com.abcfestas.locapp.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.MainColor
import com.abcfestas.locapp.ui.theme.Red
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.CircleImage
import com.abcfestas.locapp.view.components.LayoutDefault

@Composable
fun HomeScreen (
    navController: NavController
) {
    LayoutDefault(
        content = {
            CurrentRentals()
            UpcomingRentals()
        },
        actionTitle = stringResource(id = R.string.new_rent),
        onClickAction = {
            Log.d("home", "clicked")
        }
    )
}

@Composable
fun CurrentRentals() {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.current_rentals), style = Typography.titleLarge)
    Spacer(modifier = Modifier.height(16.dp))

    RentBox(
        title = "Adriana",
        subtitle = "1 dia em atraso",
        progress = -0.1f
    )

    RentBox(
        title = "Ítalo",
        subtitle = "3 dias em atraso",
        progress = -0.3f,
        imageResource = R.drawable.product_example
    )

    RentBox(
        title = "Diana de Carvalho",
        subtitle = "Notas...",
        progress = 0.83f,
        imageResource = R.drawable.product_example
    )
    RentBox(
        title = "José da Silva",
        subtitle = "lembrar de pedir algo importante para este cliente",
        progress = 0.36f,
        imageResource = R.drawable.product_example
    )
    RentBox(
        title = "Marcos",
        subtitle = "",
        progress = 0.22f,
        imageResource = R.drawable.product_example
    )
}

@Composable
fun UpcomingRentals() {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(id = R.string.upcoming_rentals), style = Typography.titleLarge)
    Spacer(modifier = Modifier.height(16.dp))

    RentBox(
        title = "Marcos",
        subtitle = "Reservado para 20/10/2020"
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020",
        imageResource = R.drawable.product_example
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020"
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020"
    )
}

@Composable
fun RentBox(
    title: String,
    subtitle: String,
    progress: Float? = null,
    imageResource: Int? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircleImage(
            imageResource = imageResource,
            contentDescription = "Image"
        )

        Column(modifier = Modifier.padding(start = 24.dp)) {
            Text(text = title, style = Typography.bodyLarge)
            Text(text = subtitle, style = Typography.bodyMedium)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (progress != null) {
        LinearProgressIndicator(
            progress = { if (progress < 0) { 1f } else { progress } },
            modifier = Modifier
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(),
            color = if (progress < 0) { Red } else { MainColor },
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}