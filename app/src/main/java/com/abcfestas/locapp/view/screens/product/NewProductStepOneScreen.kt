package com.abcfestas.locapp.view.screens.product

import CreateProductViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.CancelButton
import com.abcfestas.locapp.viewmodel.viewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateProductScreen(
    navController: NavController,
    viewModel: CreateProductViewModel = viewModel(factory = viewModelFactory {
        CreateProductViewModel(LocAppApplication.appModule.productRepository)
    })
) {
    val step = viewModel.step

    AnimatedContent(
        targetState = step,
        transitionSpec = {
            slideInHorizontally { fullWidth -> fullWidth
            } togetherWith slideOutHorizontally { fullWidth -> -fullWidth
            }
        }, label = ""
    ) { targetStep ->
        when (targetStep) {
            1 -> ProductNameStep(viewModel)
            2 -> {
                if (viewModel.selectedProduct != null) {
                    EditQuantityStep(viewModel)
                } else {
                    NewProductDetailsStep(viewModel)
                }
            }
        }
    }
}

@Composable
fun ProductNameStep(viewModel: CreateProductViewModel) {
    var query by remember { mutableStateOf("") }
    val products = listOf(
        Product(
            1,
            "Nome do produto 1",
            "descricao do produto 1",
            10.50,
            1
        ),
        Product(
            2,
            "Nome do produto 2",
            "descricao do produto 2",
            150.00,
            2
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = query,
            onValueChange = {it
                query = it
                viewModel.productName = it
            },
            label = { Text("Nome do Produto") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (query.isNotEmpty()) {
            LazyColumn {
                val filteredProducts = products.filter { it.name.contains(query, ignoreCase = true) }
                items(filteredProducts) { product ->
                    TextButton(onClick = { viewModel.selectProduct(product) }) {
                        Text(product.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.nextStep() }) {
            Text("Avançar")
        }
    }
}

@Composable
fun EditQuantityStep(viewModel: CreateProductViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Editar quantidade para: ${viewModel.selectedProduct?.name}")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = viewModel.quantity.toString(),
            onValueChange = { viewModel.quantity = it.toInt() },
            label = { Text("Quantidade") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { viewModel.previousStep() }) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* Save quantity and go back or finish */ }) {
                Text("Salvar")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NewProductDetailsStep(viewModel: CreateProductViewModel)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Cadastrar novo produto: ${viewModel.productName}")
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.quantity.toString(),
            onValueChange = { viewModel.quantity = it.toInt() },
            label = { Text("Quantidade") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = viewModel.price,
            onValueChange = { viewModel.price = it },
            label = { Text("Preço") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Descrição") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { viewModel.previousStep() }) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* Save new product */ }) {
                Text("Salvar")
            }
        }
    }
}


/*

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


@Preview(showBackground = true)
@Composable
fun NewProductStepOneScreenPreview() {
    val navController = rememberNavController()
    NewProductStepOneScreen(navController)
}*/

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