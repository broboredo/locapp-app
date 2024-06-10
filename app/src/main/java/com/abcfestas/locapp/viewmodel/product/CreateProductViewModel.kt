package com.abcfestas.locapp.viewmodel.product

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.domain.use_case.form_validation.ValidatePrice
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateRequired
import com.abcfestas.locapp.repository.ProductRepository
import com.abcfestas.locapp.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    // TODO:
    var step by mutableStateOf(1)
        private set
    var selectedProduct: Product? by mutableStateOf(null)

    var state by mutableStateOf(ProductFormState())
    var productList = mutableStateOf<List<Product>>(listOf())

    private val validateRequired: ValidateRequired = ValidateRequired()
    private val validatePrice: ValidatePrice = ValidatePrice()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    val loadingProducts = mutableStateOf(false)
    val loadingFormButton = mutableStateOf(false)
    val loadingScreen = mutableStateOf(false)

    // TODO product state
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
                    Log.d("CreateProductViewModel", "loadProducts - ERROR: ${response.message}")
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
                save()
            }
            is ProductFormEvent.Update -> {
                update()
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

    fun save()
    {
        if (!validateForm()) return

        viewModelScope.launch {
            loadingFormButton.value = true
            when (val result = repository.createProduct(state)) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: CreateProductViewModel::save", "Failed to submit form: ${result.message}")
                    TODO()
                }
            }
            loadingFormButton.value = false
        }
    }

    private fun fetchProduct() {
        viewModelScope.launch {
            loadingScreen.value = true
            when (val result =
                selectedProduct?.let { it.id }?.let { repository.getProductById(it) }) {
                is Resource.Success -> {
                    val product = result.data!!.data
                    state = state.copy(
                        id = product.ID,
                        name = product.Name,
                        quantity = product.Quantity,
                        description =  product.Description
                    )
                }
                is Resource.Error -> {
                    Log.d("ERROR: CreateProductViewModel::fetchProduct", "Failed to fetch product: ${result.message}")
                    TODO()
                }

                null -> TODO()
            }
            loadingScreen.value = false
        }
    }

    fun update()
    {
        viewModelScope.launch {
            loadingFormButton.value = true
            val result = state.id?.let { repository.updateProduct(it, state) }
            when (result) {
                is Resource.Success -> {
                    validationEventChannel.send(CreateProductViewModel.ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: CreateProductViewModel::updateForm", "Failed to update form: ${result.message}")
                    TODO()
                }

                null -> {
                    Log.d("ERROR: CreateProductViewModel::updateForm", "Product ID is null")
                    TODO()
                }
            }
            loadingFormButton.value = false
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}