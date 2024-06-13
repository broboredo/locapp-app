package com.abcfestas.locapp.viewmodel.product

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.R
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.domain.use_case.form_validation.ValidatePrice
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateRequired
import com.abcfestas.locapp.repository.ProductRepository
import com.abcfestas.locapp.util.Constants
import com.abcfestas.locapp.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProductFormViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    var step by mutableStateOf(1)
        private set
    var selectedProduct: Product? by mutableStateOf(null)

    var state by mutableStateOf(ProductFormState())
    var isUpdateForm by mutableStateOf(false);
    var productList = mutableStateOf<List<Product>>(listOf())

    var imageUri: MutableState<Uri?> = mutableStateOf(null)

    private val validateRequired: ValidateRequired = ValidateRequired()
    private val validatePrice: ValidatePrice = ValidatePrice()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    val loadingProducts = mutableStateOf(false)
    val loadingFormButton = mutableStateOf(false)
    val loadingScreen = mutableStateOf(false)

    var productNameOnInput = mutableStateOf("")

    fun loadProducts() {
        viewModelScope.launch {
            loadingProducts.value = true
            var productSearchable: String? = null
            if (productNameOnInput.value.length >= 3) {
                productSearchable = productNameOnInput.value
            }

            val response = repository.getProducts(
                search = productSearchable
            )
            when (response) {
                is Resource.Success -> {
                    val responsePaginated = response.data!!.data

                    productList.value = responsePaginated.data.mapIndexed { _, entry ->
                        Product(
                            id = entry.ID,
                            name = entry.Name,
                            quantity = entry.Quantity,
                            price = entry.Price,
                            description = entry.Description
                        )
                    }

                    loadingProducts.value = false
                }
                is Resource.Error -> {
                    Log.d("ERROR: ProductFormViewModel", "message: ${response.message}")
                    loadingProducts.value = false
                }
            }
        }
    }


    fun nextStep() {
        step++
        if (selectedProduct != null && step == 2) {
            fetchProduct()
        }
    }

    fun previousStep() {
        if (step == 2) {
            reset()
        }
        step--
    }

    fun selectProduct(product: Product) {
        selectedProduct = product
        nextStep()
    }

    private fun reset() {
        productNameOnInput.value = ""
        selectedProduct = null
    }

    fun onEvent(event: ProductFormEvent) {
        when (event) {
            is ProductFormEvent.NameChanged -> {
                state = state.copy(name =  event.name)
            }
            is ProductFormEvent.QuantityChanged -> {
                state = state.copy(quantity =  event.quantity)
            }
            is ProductFormEvent.PriceChanged -> {
                state = state.copy(price =  event.price)
            }
            is ProductFormEvent.DescriptionChanged -> {
                state = state.copy(description =  event.description)
            }
            is ProductFormEvent.Save -> {
                // TODO(save() with context)
            }
            is ProductFormEvent.Update -> {
                // TODO(update() with context)
            }
        }
    }

    private fun validateForm(): Boolean {
        val nameResult = validateRequired.execute(state.name)
        val priceResult = validatePrice.execute(state.price, true)

        val hasError = listOf(nameResult).any { !it.success }

        state = state.copy(
            nameError = nameResult.errorMessage,
            priceError = priceResult.errorMessage
        )

        return !hasError
    }

    fun save(context: Context)
    {
        if (!validateForm()) return

        viewModelScope.launch {
            loadingFormButton.value = true
            val result = repository.createProduct(state)
            if (imageUri.value != null) {
                val resultImage = repository.syncImage(
                    context,
                    result.data!!.data.ID,
                    imageUri.value!!
                )
            }

            when (result) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: ProductFormViewModel::save", "Failed to submit form: ${result.message}")
                }
            }
            loadingFormButton.value = false
        }
    }

    fun fetchProduct(productId: Int? = null) {
        isUpdateForm = productId != null
        viewModelScope.launch {
            loadingScreen.value = true
            when (val result = (productId ?: selectedProduct?.let { it.id }?.let { it })?.let {
                Log.d("LOG: get product id", "id: $it")
                repository.getProductById(
                    it
                )
            }) {
                is Resource.Success -> {
                    val product = result.data!!.data
                    state = state.copy(
                        id = product.ID,
                        name = product.Name,
                        quantity = product.Quantity,
                        description =  product.Description,
                        price = product.Price,
                        imagePath = "${Constants.BASE_URL}${product.ImagePath}"
                    )
                }
                is Resource.Error -> {
                    Log.d("ERROR: ProductFormViewModel::fetchProduct", "Failed to fetch product: ${result.message}")
                }

                null -> TODO()
            }
            loadingScreen.value = false
        }
    }

    fun update(context: Context)
    {
        Log.d("LOG: update", "id: ${state.id}")
        viewModelScope.launch {
            loadingFormButton.value = true
            val result = state.id?.let { repository.updateProduct(it, state) }
            if (imageUri.value != null) {
                val resultImage = state.id?.let {
                    repository.syncImage(
                        context,
                        it,
                        imageUri.value!!
                    )
                }
            }

            when (result) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: ProductFormViewModel::updateForm", "Failed to update form: ${result.message}")
                }

                null -> {
                    Log.d("ERROR: ProductFormViewModel::updateForm", "Product ID is null")
                    TODO()
                }
            }
            loadingFormButton.value = false
        }
    }

    fun getSuccessMessage(context: Context): String
    {
        return if (isUpdateForm) {
            context.getString(R.string.product_updated_successfully)
        } else if (step == 2 && selectedProduct != null) {
            context.getString(R.string.product_quantity_updated_successfully)
        } else {
            context.getString(R.string.product_created_successfully)
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}