package com.abcfestas.locapp.view.screens

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.Red
import com.abcfestas.locapp.view.components.LayoutDefault
import compose.icons.Octicons
import compose.icons.octicons.Alert24

@Composable
fun SomethingWentWrongScreen(
    navController: NavController,
    errorMessage: String? = null
) {
    LayoutDefault(
        title = stringResource(id = R.string.something_went_wrong),
        description = errorMessage,
        content = { 
           Icon(
               imageVector = Octicons.Alert24,
               contentDescription = stringResource(id = R.string.something_went_wrong),
               tint = Red
           )
        },
        actionTitle = stringResource(id = R.string.back),
        onClickAction = {
            navController.popBackStack()
        }
    )
}