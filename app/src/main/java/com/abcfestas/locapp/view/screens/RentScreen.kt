package com.abcfestas.locapp.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.view.components.LayoutDefault

@Composable
fun RentScreen(
    navController: NavController
) {
    LayoutDefault(
        title = stringResource(R.string.rentals),
        content = {
            ListRentals()
        },
        actionTitle = stringResource(R.string.new_rent),
        onClickAction = {
            // TODO:
            Log.d("rent", "clicked")
        }
    )
}

@Composable
fun ListRentals() {
    Spacer(modifier = Modifier.height(16.dp))

    RentBox(title = "Adriana", subtitle = "20/10/2020")
    RentBox(title = "Cristina Adrianne", subtitle = "22/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
    RentBox(title = "Jorge Manoel", subtitle = "29/10/2020")
}

@Preview(showBackground = true)
@Composable
fun RentScreenPreview() {
    val navController = rememberNavController()
    RentScreen(navController)
}