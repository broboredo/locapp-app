package com.abcfestas.locapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abcfestas.locapp.ui.theme.LocAppTheme
import com.abcfestas.locapp.view.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LocAppTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
