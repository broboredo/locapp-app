package com.abcfestas.locapp.view.screens.product

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.abcfestas.locapp.util.Constants
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

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName, ".jpg", externalCacheDir
    )
}

@Composable
fun ProductFormScreen(
    navController: NavController,
    productId: Int? = null,
    viewModel: ProductFormViewModel = viewModel(factory = viewModelFactory {
        ProductFormViewModel(LocAppApplication.appModule.productRepository)
    })
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productId?.let { viewModel.fetchProduct(it) }

        viewModel.validationEvents.collect { event ->
            when(event) {
                is ProductFormViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, viewModel.getSuccessMessage(context), Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }

    ContentBasedOnStep(navController, productId, viewModel)
}

@Composable
private fun ContentBasedOnStep(
    navController: NavController,
    productId: Int?,
    viewModel: ProductFormViewModel
) {
    val step = viewModel.step

    when {
        productId != null -> ProductForm(viewModel, navController)
        viewModel.selectedProduct != null && step == 2 -> EditQuantityStep(viewModel)
        step == 1 -> ProductNameStep(viewModel, navController)
        else -> ProductForm(viewModel, navController)
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
                title = stringResource(id = R.string.product_flow_step_one_title),
                navController = navController
            )
            Text(
                text = stringResource(id = R.string.product_flow_step_one_subtitle),
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
                placeholder = stringResource(id = R.string.product_name),
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
    val context: Context = LocalContext.current

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
                    Text(text = stringResource(id = R.string.product_quantity), style = Typography.titleLarge)
                }
            }
            Text(
                text = stringResource(id = R.string.product_flow_step_two_update_quantity),
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                placeholder = stringResource(id = R.string.quantity),
                isError = productState.quantityError != null,
                errorMessage = productState.quantityError,
            )

        }

        Button(
            label = stringResource(id = R.string.update),
            onClick = {
                viewModel.update(context)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loading = viewModel.loadingFormButton.value
        )
    }
}

@Composable
fun ProductForm(viewModel: ProductFormViewModel, navController: NavController)
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
                    text = stringResource(id = if (viewModel.isUpdateForm) {
                         R.string.update_product
                    } else {
                        R.string.product_register_title
                    }, viewModel.productNameOnInput.value),
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
                            placeholder = stringResource(id = R.string.product_name_hint),
                            isError = productState.nameError != null,
                            errorMessage = productState.nameError,
                            label = {
                                Text(text = stringResource(id = R.string.product))
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
                        placeholder = stringResource(id = R.string.product_quantity_hint),
                        isError = productState.quantityError != null,
                        errorMessage = productState.quantityError,
                        label = {
                            Text(text = stringResource(id = R.string.quantity))
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
                        placeholder = stringResource(id = R.string.product_price_hint),
                        isError = productState.priceError != null,
                        errorMessage = productState.priceError,
                        label = {
                            Text(text = stringResource(id = R.string.price))
                        }
                    )

                    TextInputFieldWithError(
                        value = productState.description,
                        onValueChange = { viewModel.onEvent(ProductFormEvent.DescriptionChanged(it)) },
                        placeholder = stringResource(id = R.string.product_description_hint),
                        isError = productState.descriptionError != null,
                        errorMessage = productState.descriptionError,
                        singleLine = false,
                        minLines = 5,
                        label = {
                            Text(text = stringResource(id = R.string.description))
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
            Toast.makeText(context, "Permissão Salva", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permissão Negada", Toast.LENGTH_SHORT).show()
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
            if (imageUrl == null && viewModel.state.imagePath != Constants.BASE_URL) {
                AsyncImage(
                    model = viewModel.state.imagePath,
                    contentDescription = viewModel.state.name,
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
                        contentDescription = stringResource(id = R.string.no_image),
                        tint = GrayLight,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(id = R.string.add_image), color = GrayLight)
                }
            }
        }
    }
}
