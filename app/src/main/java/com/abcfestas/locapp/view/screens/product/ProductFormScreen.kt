package com.abcfestas.locapp.view.screens.product

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.components.CancelButton
import com.abcfestas.locapp.view.components.TextInputField
import com.abcfestas.locapp.view.components.TextInputFieldWithError
import com.abcfestas.locapp.viewmodel.product.ProductFormEvent
import com.abcfestas.locapp.viewmodel.product.ProductFormViewModel
import com.abcfestas.locapp.viewmodel.viewModelFactory
import compose.icons.Octicons
import compose.icons.octicons.Image24
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ProductFormScreen(
    navController: NavController,
    productId: Int? = null,
    viewModel: ProductFormViewModel = viewModel(factory = viewModelFactory {
        ProductFormViewModel(LocAppApplication.appModule.productRepository)
    })
) {
    val step = viewModel.step
    val context = LocalContext.current
    val successMessage = stringResource(
        id = if (productId != null) {
            R.string.product_updated_successfully
        } else if (step == 1) {
            R.string.product_created_successfully
        } else {
            R.string.product_quantity_updated_successfully
        }
    )

    LaunchedEffect(Unit) {
        if (productId != null) {
            viewModel.fetchProduct(productId)
        }

        viewModel.validationEvents.collect { event ->
            when(event) {
                is ProductFormViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }

    if (productId == null) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                slideInHorizontally { fullWidth -> fullWidth
                } togetherWith slideOutHorizontally { fullWidth -> -fullWidth
                }
            }, label = ""
        ) { targetStep ->
            when (targetStep) {
                1 -> ProductNameStep(viewModel, navController)
                2 -> {
                    if (viewModel.selectedProduct != null) {
                        if (viewModel.loadingScreen.value) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            EditQuantityStep(viewModel)
                        }
                    } else {
                        NewProductDetailsStep(viewModel, navController)
                    }
                }
            }
        }
    } else {
        NewProductDetailsStep(viewModel, navController)
    }
}

@Composable
fun ProductNameStep(
    viewModel: ProductFormViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    var query by remember { mutableStateOf("") }
    val products by remember { viewModel.productList }

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

            TextInputField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.productNameOnInput.value = it
                    viewModel.onEvent(ProductFormEvent.NameChanged(it))
                },
                placeholder = "Nome do móvel",
                onKeyboardEnter = {
                    viewModel.nextStep()
                }
            )

            if (query.isNotEmpty()) {
                LazyColumn {
                    val filteredProducts = products.filter { it.name.contains(query, ignoreCase = true) }
                    items(filteredProducts) { product ->
                        TextButton(onClick = { viewModel.selectProduct(product) }) {
                            Text(product.name)
                        }
                    }
                }
            }
        }

        Button(
            label = if (viewModel.productNameOnInput.value.isNotEmpty()) {
                stringResource(id = R.string.next)
            } else {
                stringResource(id = R.string.enter_product_name)
           },
            onClick = {
                viewModel.nextStep()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            enabled = viewModel.productNameOnInput.value.isNotEmpty(),
            loading = viewModel.loadingFormButton.value
        )
    }
}

@Composable
fun EditQuantityStep(viewModel: ProductFormViewModel) {
    val productState = viewModel.state

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .align(Alignment.Center),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {
                Column {
                    CancelButton(
                        onClick = {
                            viewModel.previousStep()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Quantidade deste móvel", style = Typography.titleLarge)
                }
            }
            Text(
                text = "Este móvel já está registrado. Atualize a quantidade atual para refletir o estoque correto.",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextInputFieldWithError(
                value = productState.quantity.toString(),
                onValueChange = { viewModel.onEvent(ProductFormEvent.QuantityChanged(it.toInt())) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeholder = "Quantidade",
                isError = productState.quantityError != null,
                errorMessage = productState.quantityError,
            )

        }

        Button(
            label = stringResource(id = R.string.update),
            onClick = {
                viewModel.onEvent(ProductFormEvent.Update)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loading = viewModel.loadingFormButton.value
        )
    }
}

@Composable
fun NewProductDetailsStep(viewModel: ProductFormViewModel, navController: NavController)
{
    val productState = viewModel.state
    val context: Context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CancelButton(
                    onClick = {
                        if (viewModel.isUpdateForm) {
                            navController.popBackStack()
                        } else {
                            viewModel.previousStep()
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (viewModel.isUpdateForm) {
                        "Atualizar móvel"
                    } else {
                        "Cadastro: ${viewModel.productNameOnInput.value}"
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {
                Camera(
                    viewModel = viewModel
                )

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (viewModel.isUpdateForm) {
                        TextInputFieldWithError(
                            value = productState.name.toString(),
                            onValueChange = {
                                viewModel.onEvent(ProductFormEvent.NameChanged(it))
                            },
                            placeholder = "Adicione o nome do móvel",
                            isError = productState.nameError != null,
                            errorMessage = productState.nameError,
                            label = {
                                Text(text = "Móvel")
                            }
                        )
                    }

                    TextInputFieldWithError(
                        value = productState.quantity.toString(),
                        onValueChange = {
                            if (it.isEmpty()) {
                                viewModel.onEvent(ProductFormEvent.QuantityChanged(0))
                            } else {
                                viewModel.onEvent(ProductFormEvent.QuantityChanged(it.toInt()))
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        placeholder = "Adicione a quantidade que possui deste mesmo móvel",
                        isError = productState.quantityError != null,
                        errorMessage = productState.quantityError,
                        label = {
                            Text(text = "Quantidade")
                        }
                    )

                    TextInputFieldWithError(
                        value = productState.price.toString(),
                        onValueChange = {
                            if (it.isEmpty()) {
                                viewModel.onEvent(ProductFormEvent.PriceChanged(0.00))
                            } else {
                                viewModel.onEvent(ProductFormEvent.PriceChanged(it.toDouble()))
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        placeholder = "Qual valor de locação deste móvel (por unidade)?",
                        isError = productState.priceError != null,
                        errorMessage = productState.priceError,
                        label = {
                            Text(text = "Preço")
                        }
                    )

                    TextInputFieldWithError(
                        value = productState.description,
                        onValueChange = { viewModel.onEvent(ProductFormEvent.DescriptionChanged(it)) },
                        placeholder = "Se tem alguma nota sobre este tipo de móvel, deixe anotado aqui...",
                        isError = productState.descriptionError != null,
                        errorMessage = productState.descriptionError,
                        singleLine = false,
                        minLines = 5,
                        label = {
                            Text(text = "Descrição")
                        }
                    )
                }
            }

        }

        Button(
            label = stringResource(id = if (viewModel.isUpdateForm) {
                R.string.update
            } else {
                R.string.save
            }),
            onClick = {
                if (viewModel.isUpdateForm) {
                    viewModel.update(context)
                } else {
                    viewModel.save(context)
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loading = viewModel.loadingFormButton.value
        )
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

@Composable
fun Camera(viewModel: ProductFormViewModel) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        context.getString(R.string.fileprovider),
        file
    )
    var imageUrl by remember { mutableStateOf<Any?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUrl = uri
            viewModel.imageUri.value = uri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Gray),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl == null && viewModel.state.imagePath != null) {
                AsyncImage(
                    model = viewModel.state.imagePath,
                    contentDescription = "Toque para adicionar uma foto",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Octicons.Image24,
                        contentDescription = "Sem imagem cadastrada",
                        tint = GrayLight,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Toque para adicionar uma foto", color = GrayLight)
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName, ".jpg", externalCacheDir
    )
}
