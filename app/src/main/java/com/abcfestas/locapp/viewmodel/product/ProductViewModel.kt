package com.abcfestas.locapp.viewmodel.product

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.repository.ProductRepository
import com.abcfestas.locapp.util.Resource
import kotlinx.coroutines.launch

class ProductViewModel (
    private val repository: ProductRepository
): ViewModel() {

    private var currentPage = 1;

    var products = mutableStateOf<List<Product>>(listOf())
    var error = mutableStateOf("")
    var loading = mutableStateOf(false)

    /*init {
        loadProducts()
    }*/

    fun loadProducts() {
        viewModelScope.launch {
            loading.value = true
            // Thread.sleep(10_000)
            Log.d("Product view model", "loadProducts")
            val response = repository.getProducts(page = currentPage)
            when (response) {
                is Resource.Success -> {
                    Log.d("Product view model", "loadProducts - SUCCESS")
                    val productEntries = response.data!!.data.mapIndexed { _, entry ->
                        Product(
                            id = entry.ID,
                            name = entry.Name,
                            description = entry.Description,
                            price = entry.Price,
                            quantity = entry.Quantity
                        )
                    }
                    // TODO: pagination
                    //currentPage++
                    error.value = ""
                    products.value += productEntries
                    loading.value = false
                }
                is Resource.Error -> {
                    Log.d("Product view model", "loadProducts - ERROR: ${response.message}")
                    loading.value = false
                    error.value = response.message.toString()
                }
            }
        }
    }
}
