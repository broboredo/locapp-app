package com.abcfestas.locapp.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.Red
import com.abcfestas.locapp.ui.theme.Typography
import compose.icons.Octicons
import compose.icons.octicons.Broadcast24

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

@Composable
fun LostConnection(
    message: String? = null,
    action: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Octicons.Broadcast24,
            contentDescription = stringResource(id = R.string.no_internet_connection),
            tint = Red,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Conexão Perdida", color = Red, style = Typography.titleMedium)
        Text(text = "Verifique sua conexão", color = Red)
        if (message != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = Red, style = Typography.titleSmall)
        }
        if (action != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                label = stringResource(id = R.string.retry),
                outline = true,
                mainColor = Red,
                onClick = {
                    action()
                }
            )
        }
    }
}
