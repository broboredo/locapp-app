package com.abcfestas.locapp.view.screens.product

import CreateProductViewModel
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import coil.compose.rememberImagePainter
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.R
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.GrayLight
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.CancelButton
import com.abcfestas.locapp.view.components.TextInputField
import com.abcfestas.locapp.view.components.TextInputFieldWithError
import com.abcfestas.locapp.viewmodel.viewModelFactory
import compose.icons.Octicons
import compose.icons.octicons.Image24
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
            1 -> ProductNameStep(viewModel, navController)
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
fun ProductNameStep(viewModel: CreateProductViewModel, navController: NavController) {
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
                    viewModel.productName = it
                },
                placeholder = "Nome do Produto",
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

        com.abcfestas.locapp.view.components.Button(
            label = if (viewModel.productName.isNotEmpty()) {
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
            enabled = viewModel.productName.isNotEmpty()
        )
    }
}

@Composable
fun EditQuantityStep(viewModel: CreateProductViewModel) {
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
                    Text(text = "Quantidade do produto", style = Typography.titleLarge)
                }
            }
            Text(
                text = "Este produto já está registrado. Atualize a quantidade atual para refletir o estoque correto.",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextInputFieldWithError(
                value = viewModel.quantity.toString(),
                onValueChange = { viewModel.quantity = it.toInt() },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeholder = "Quantidade",
                isError = viewModel.quantityError != null,
                errorMessage = viewModel.quantityError,
            )

        }

        com.abcfestas.locapp.view.components.Button(
            label = stringResource(id = R.string.update),
            onClick = {
                viewModel.updateProduct()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun NewProductDetailsStep(viewModel: CreateProductViewModel)
{
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
                        viewModel.previousStep()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cadastro do ${viewModel.productName}",
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
                Camera()

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextInputFieldWithError(
                        value = viewModel.quantity.toString(),
                        onValueChange = { viewModel.quantity = it.toInt() },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        placeholder = "Adicione a quantidade que possui deste mesmo produto",
                        isError = viewModel.quantityError != null,
                        errorMessage = viewModel.quantityError,
                        label = {
                            Text(text = "Quantidade")
                        }
                    )

                    TextInputFieldWithError(
                        value = viewModel.price,
                        onValueChange = { viewModel.price = it },
                        placeholder = "Qual valor de locação deste produto (por unidade)?",
                        isError = viewModel.priceError != null,
                        errorMessage = viewModel.priceError,
                        label = {
                            Text(text = "Preço")
                        }
                    )

                    TextInputFieldWithError(
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
                        placeholder = "Se tem alguma nota sobre este tipo de móvel, deixe anotado aqui...",
                        isError = viewModel.descriptionError != null,
                        errorMessage = viewModel.descriptionError,
                        singleLine = false,
                        minLines = 5,
                        label = {
                            Text(text = "Descrição")
                        }
                    )
                }
            }

        }

        com.abcfestas.locapp.view.components.Button(
            label = stringResource(id = R.string.save),
            onClick = {
                viewModel.save()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
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
fun Camera(currentImageUri: String? = null) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        context.getString(R.string.fileprovider),
        file
    )

    var imageUrl by remember { mutableStateOf<Any?>(currentImageUri) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUrl = uri
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
            if (imageUrl != null) {
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
