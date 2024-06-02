package com.abcfestas.locapp.view.screens.product

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.Autocomplete
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.components.CancelButton

@Composable
fun NewProductStepOneScreen (
    navController: NavController
) {
    var newProductName by remember { mutableStateOf("") }
    var productSelected: String? by remember { mutableStateOf(null) }
    
    val items = listOf(
        "Mesa redonda",
        "Dinossauro",
        "Cadeira branca",
        "Messa rústica",
        "Toalha rosa 20m"
    )

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
                title = "Insira o nome do móvel",
                navController = navController
            )
            Text(
                text = "Caso este móvel já exista, basta digitar o nome do móvel, selecionar ele na lista e, em seguida, acrescentar ou retirar a quantidade de itens existentes",
                style = Typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {

                Autocomplete(
                    labelName = "Nome do móvel",
                    onProductSelected = { productAlreadyExists ->
                        productSelected = productAlreadyExists
                    },
                    onProductCreated = { productNameCreated ->
                        newProductName = productNameCreated
                    },
                    items = items
                )
            }
        }

        Button(
            label = stringResource(id = R.string.next),
            onClick = {
                nextStep(
                    navController,
                    newProductName = newProductName,
                    productFound = productSelected
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

fun nextStep(
    navController: NavController,
    newProductName: String,
    productFound: String? = null // TODO: change to ProductModel
) {
    if (productFound !== null) {
        Log.d("NewProductScreen", "Product selected to edit quantity: $productFound")
    } else {
        Log.d("NewProductScreen", "NEW PRODUCT TO GENERATE: $newProductName")
    }
}

@Composable
fun WizardFormTopNavigation(
    title: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Column {
            CancelButton(
                onClick = {
                    navController.popBackStack()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = Typography.titleLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewProductStepOneScreenPreview() {
    val navController = rememberNavController()
    NewProductStepOneScreen(navController)
}