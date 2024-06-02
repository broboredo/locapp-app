package com.abcfestas.locapp.view.screens.customer

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.data.models.Customer
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.screens.product.WizardFormTopNavigation

@Composable
fun NewCustomerScreen (
    navController: NavController
) {
    var customer by remember { mutableStateOf<Customer?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .align(Alignment.Center),
        ) {

            WizardFormTopNavigation(
                title = stringResource(R.string.new_customer),
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {

                OutlinedTextField(
                    label = {
                        Text(text = "Nome")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {
                        customer!!.name = it
                    }
                )

                OutlinedTextField(
                    label = {
                        Text(text = "Telefone")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {
                        customer!!.phone = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                OutlinedTextField(
                    label = {
                        Text(text = "Endereço")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {
                        customer!!.address = it
                    },
                    singleLine = false,
                    minLines = 5
                )

                OutlinedTextField(
                    label = {
                        Text(text = "Notas")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {
                        customer!!.notes = it
                    },
                    singleLine = false,
                    minLines = 5
                )

            }
        }

        Button(
            label = stringResource(id = R.string.save),
            onClick = {
                customer?.let { save(it) }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

fun save(customer: Customer) {
    Log.d("NewCustomerScreen", "CUSTOMER_NAME: ${customer.name}")
}


@Preview(showBackground = true)
@Composable
fun NewCustomerScreenPreview() {
    val navController = rememberNavController()
    NewCustomerScreen(navController)
}