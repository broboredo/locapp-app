package com.abcfestas.locapp.viewmodel.customer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.data.models.Customer
import com.abcfestas.locapp.repository.CustomerRepository
import com.abcfestas.locapp.util.Resource
import kotlinx.coroutines.launch

class CustomerViewModel (
    private val repository: CustomerRepository
): ViewModel() {

    private var currentPage = 1;

    var customers = mutableStateOf<List<Customer>>(listOf())
    var error = mutableStateOf("")
    var loading = mutableStateOf(false)

    fun loadCustomers() {
        viewModelScope.launch {
            loading.value = true
            // Thread.sleep(10_000)
            Log.d("Customer view model", "loadCustomers")
            val response = repository.getCustomers(page = currentPage)
            when (response) {
                is Resource.Success -> {
                    Log.d("Customer view model", "loadCustomers - SUCCESS")
                    val customerEntries = response.data!!.data.mapIndexed { _, entry ->
                        Customer(
                            id = entry.ID,
                            name = entry.Name,
                            phone = entry.Phone,
                            notes = entry.Notes,
                            address = entry.Address
                        )
                    }
                    // TODO: pagination
                    //currentPage++
                    error.value = ""
                    customers.value = customerEntries
                    loading.value = false
                }
                is Resource.Error -> {
                    Log.d("Customer view model", "loadCustomers - ERROR: ${response.message}")
                    loading.value = false
                    error.value = response.message.toString()
                }
            }
        }
    }
}
