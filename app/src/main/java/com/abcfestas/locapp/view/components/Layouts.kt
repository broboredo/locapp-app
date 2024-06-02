package com.abcfestas.locapp.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.ui.theme.Typography

@Composable
fun LayoutDefault(
    title: String? = null,
    description: String? = null,
    content: @Composable () -> Unit,
    actionTitle: String,
    onClickAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
        ) {
            if (title != null) {
                Text(text = title, style = Typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            content()
        }

        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            label = actionTitle,
            onClick = onClickAction
        )
    }
}
