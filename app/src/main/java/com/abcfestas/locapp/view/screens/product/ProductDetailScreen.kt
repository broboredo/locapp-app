package com.abcfestas.locapp.view.screens.product

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun ProductDetailScreen (
    navController: NavController,
    productId: Int
) {
    Text("PRODUCT DETAIL SCREEN")
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    // val navController = rememberNavController()
    // ProductDetailScreen(navController)
}