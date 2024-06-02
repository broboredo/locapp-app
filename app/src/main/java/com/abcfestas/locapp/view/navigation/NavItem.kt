package com.abcfestas.locapp.view.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.abcfestas.locapp.R
import compose.icons.Octicons
import compose.icons.octicons.Archive24
import compose.icons.octicons.Checklist24
import compose.icons.octicons.Home24
import compose.icons.octicons.People24

data class NavItem(
    val label: Int,
    val icon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItem(label = R.string.home, icon = Octicons.Home24, route = ScreensEnum.HomeScreen.route),
    NavItem(label = R.string.products, icon = Octicons.Archive24, route = ScreensEnum.ProductScreen.route),
    NavItem(label = R.string.rentals, icon = Octicons.Checklist24, route = ScreensEnum.RentScreen.route),
    NavItem(label = R.string.customers, icon = Octicons.People24, route = ScreensEnum.CustomerScreen.route),
)