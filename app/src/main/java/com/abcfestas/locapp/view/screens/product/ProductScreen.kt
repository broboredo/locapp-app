package com.abcfestas.locapp.view.screens.product

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.R
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.ui.theme.Green500
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.components.LostConnection
import com.abcfestas.locapp.view.navigation.ScreensEnum
import com.abcfestas.locapp.viewmodel.product.ProductViewModel
import com.abcfestas.locapp.viewmodel.viewModelFactory

@Composable
fun ProductScreen(
    navController: NavController
) {
    // TODO use LayoutDefault - remove modifier.scroll from layoutdefault
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f, false)) {
            Text(text = stringResource(R.string.products), style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            ListProducts(navController)
        }

        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            label = stringResource(R.string.new_product),
            onClick = {
                navController.navigate(ScreensEnum.CreateProductScreen.route)
            }
        )
    }
}

@Composable
fun ListProducts(
    navController: NavController,
    viewModel: ProductViewModel = viewModel(factory = viewModelFactory {
        ProductViewModel(LocAppApplication.appModule.productRepository)
    })
) {
    val productList by remember { viewModel.products }
    val loading by remember { viewModel.loading }
    val error by remember { viewModel.error }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    LazyColumn {
        items(productList) {
            // TODO: pagination
            // viewModel.loadProducts()
            Log.d("Product Screen", "products: ${productList.count()}")
            ProductBox(product = it, navController)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            if (loading) {
                CircularProgressIndicator(
                    color = Green500
                )
            } else if (error.isNotEmpty()) {
                LostConnection(message = error) { viewModel.loadProducts() }
            }
        }
    }
}

@Composable
fun ProductBox(
    product: Product,
    navController: NavController,
    quantityRented: Int? = 0
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    ScreensEnum.ProductDetailsScreen.route.replace(
                        "{productId}",
                        product.id.toString()
                    )
                )
            }
    ) {
        AsyncImage(
            model = product.imagePath,
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .width(64.dp)
                .height(64.dp),
            fallback = painterResource(id = R.drawable.img_cant_load),
            error = painterResource(id = R.drawable.img_error)
        )

        Column(modifier = Modifier.padding(start = 24.dp)) {
            Text(text = product.name, style = Typography.bodyLarge)
            Text(text = "${stringResource(R.string.quantity)}: ${product.quantity}", style = Typography.bodyMedium)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    val navController = rememberNavController()
    ProductScreen(navController)
}