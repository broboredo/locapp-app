package com.abcfestas.locapp.viewmodel.product

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.repository.ProductRepository
import com.abcfestas.locapp.util.Constants
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.viewmodel.BaseListViewModel
import kotlinx.coroutines.launch

class ProductViewModel (
    private val repository: ProductRepository
): BaseListViewModel()
{
    var products = mutableStateOf<List<Product>>(listOf())

    fun loadProducts() {
        viewModelScope.launch {
            loading.value = true

            var productSearchable: String? = null
            if (search.value.length >= 3) {
                currentPage.value = 1
                productSearchable = search.value
            }

            val response = repository.getProducts(
                page = currentPage.value,
                search = productSearchable
            )

            when (response) {
                is Resource.Success -> {
                    val responsePaginated = response.data!!.data

                    val customerEntries = responsePaginated.data.mapIndexed { _, product ->
                        Product(
                            id = product.ID,
                            name = product.Name,
                            description = product.Description,
                            price = product.Price,
                            quantity = product.Quantity,
                            imagePath = "${Constants.BASE_URL}${product.ImagePath}"
                        )
                    }

                    currentPage.value = responsePaginated.page
                    error.value = ""
                    if (currentPage.value > 1) {
                        products.value += customerEntries
                    } else {
                        products.value = customerEntries
                    }
                    loading.value = false
                    hasNextPage.value = responsePaginated.totalPages > responsePaginated.page
                }
                is Resource.Error -> {
                    Log.d("ERROR: Product view model", "message: ${response.message}")
                    currentPage.value = 1
                    products.value =  listOf()
                    loading.value = false
                    error.value = response.message.toString()
                    hasNextPage.value = false
                }
            }
        }
    }
}
