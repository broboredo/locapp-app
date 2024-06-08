package com.abcfestas.locapp.viewmodel.customer

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
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

    var currentPage = mutableIntStateOf(1)
    var customers = mutableStateOf<List<Customer>>(listOf())
    var error = mutableStateOf("")
    var loading = mutableStateOf(false)
    var hasNextPage = mutableStateOf(false)
    var search = mutableStateOf("")

    init {
        search.value = ""
    }
    fun loadCustomers() {
        viewModelScope.launch {
            loading.value = true
            Log.d("Customer view model", "loadCustomers CURRENT PAGE: $currentPage")

            var customerSearchable: String? = null
            if (search.value.length >= 3) {
                currentPage.value = 1
                customerSearchable = search.value
            }

            val response = repository.getCustomers(
                page = currentPage.value,
                search = customerSearchable
            )
            when (response) {
                is Resource.Success -> {
                    val responsePaginated = response.data!!.data

                    Log.d("Sucesso response paginated page", responsePaginated.page.toString())
                    Log.d("Sucesso response paginated total", responsePaginated.total.toString())
                    Log.d("Sucesso response paginated totalPages", responsePaginated.totalPages.toString())
                    Log.d("Sucesso response paginated pageSize", responsePaginated.pageSize.toString())

                    val customerEntries = responsePaginated.data.mapIndexed { _, entry ->
                        Customer(
                            id = entry.ID,
                            name = entry.Name,
                            phone = entry.Phone,
                            notes = entry.Notes,
                            address = entry.Address
                        )
                    }
                    currentPage.value = responsePaginated.page
                    error.value = ""
                    if (currentPage.value > 1) {
                        customers.value += customerEntries
                    } else {
                        customers.value = customerEntries
                    }
                    loading.value = false
                    hasNextPage.value = responsePaginated.totalPages > responsePaginated.page
                }
                is Resource.Error -> {
                    Log.d("Customer view model", "loadCustomers - ERROR: ${response.message}")
                    currentPage.value = 1
                    customers.value =  listOf()
                    loading.value = false
                    error.value = response.message.toString()
                    hasNextPage.value = false
                }
            }
        }
    }
}
