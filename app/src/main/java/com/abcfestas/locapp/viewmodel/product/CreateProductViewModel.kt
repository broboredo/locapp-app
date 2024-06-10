
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.repository.ProductRepository

class CreateProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    // TODO:
    var step by mutableStateOf(1)
        private set
    var selectedProduct: Product? by mutableStateOf(null)

    // TODO product state
    var quantity by mutableStateOf(0)
    var quantityError: String? by mutableStateOf(null)
    var price by mutableStateOf("")
    var priceError: String? by mutableStateOf(null)
    var description by mutableStateOf("")
    var descriptionError: String? by mutableStateOf(null)
    var productName by mutableStateOf("")

    fun nextStep() {
        step++
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
        productName = ""
        selectedProduct = null
    }

    fun updateProduct()
    {
        Log.d("click to update quantity", "updateProduct")
        TODO()
    }

    fun save()
    {
        Log.d("clicke to save product", "save product")
        TODO()
    }
}