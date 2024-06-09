import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.abcfestas.locapp.data.models.Product
import com.abcfestas.locapp.repository.ProductRepository

class CreateProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    var step by mutableStateOf(1)
        private set
    var productName by mutableStateOf("")
    var selectedProduct: Product? by mutableStateOf(null)
    var quantity by mutableStateOf(0)
    var price by mutableStateOf("")
    var description by mutableStateOf("")

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
}