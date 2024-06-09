package com.abcfestas.locapp.view.navigation

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.Green500
import com.abcfestas.locapp.view.screens.HomeScreen
import com.abcfestas.locapp.view.screens.RentScreen
import com.abcfestas.locapp.view.screens.customer.CustomerFormScreen
import com.abcfestas.locapp.view.screens.customer.CustomerScreen
import com.abcfestas.locapp.view.screens.product.CreateProductScreen
// import com.abcfestas.locapp.view.screens.product.NewProductStepOneScreen
import com.abcfestas.locapp.view.screens.product.ProductDetailScreen
import com.abcfestas.locapp.view.screens.product.ProductScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = Gray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(1.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOfNavItems.forEach {navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                        onClick = {
                            vibrate(context)
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(navItem.label), fontWeight = FontWeight.W700)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Green500,
                            selectedTextColor = Green500,
                            indicatorColor = Color.White,
                            unselectedIconColor = Gray,
                            unselectedTextColor = Gray,
                        )
                    )
                }
            }
        }
    ) {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ScreensEnum.HomeScreen.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            // Main screens
            composable(route = ScreensEnum.HomeScreen.route) {
                HomeScreen(navController)
            }
            composable(route = ScreensEnum.RentScreen.route) {
                RentScreen(navController)
            }

            // Customer Screens
            composable(route = ScreensEnum.CustomerFormScreen.route) {
                CustomerFormScreen(navController)
            }
            composable(route = ScreensEnum.CustomerScreen.route) {
                CustomerScreen(navController)
            }
            composable(
                route = ScreensEnum.CustomerDetailsScreen.route,
                arguments = listOf(
                    navArgument("customerId") {
                        type = NavType.IntType
                        nullable = false
                    }
                )
            ) {
                val customerId = remember {
                    it.arguments?.getInt("customerId") ?: 0 // TODO create try catch or not found screen
                }
                CustomerFormScreen(navController, customerId)
            }

            // Product Screens
            composable(route = ScreensEnum.ProductScreen.route) {
                ProductScreen(
                    navController
                )
            }
            composable(
                route = ScreensEnum.ProductDetailsScreen.route,
                arguments = listOf(
                    navArgument("productId") {
                        type = NavType.IntType
                        nullable = false
                    }
                )
            ) {
                val productId = remember {
                    it.arguments?.getInt("productId") ?: 0 // TODO create try catch or not found screen
                }
                ProductDetailScreen(navController, productId)
            }
            composable(route = ScreensEnum.NewProductStepOneScreen.route) {
                // NewProductStepOneScreen(navController)
                CreateProductScreen(navController)
            }
        }
    }
}

private fun vibrate(context: Context, duration: Long = 100) {
    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            it.vibrate(duration)
        }
    }
}
