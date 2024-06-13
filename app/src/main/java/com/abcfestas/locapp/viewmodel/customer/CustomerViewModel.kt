package com.abcfestas.locapp.viewmodel.customer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.data.models.Customer
import com.abcfestas.locapp.repository.CustomerRepository
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.viewmodel.BaseListViewModel
import kotlinx.coroutines.launch

class CustomerViewModel (
    private val repository: CustomerRepository
): BaseListViewModel()
{
    var customers = mutableStateOf<List<Customer>>(listOf())
    fun loadCustomers() {
        viewModelScope.launch {
            loading.value = true

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

                    val customerEntries = responsePaginated.data.mapIndexed { _, customer ->
                        Customer(
                            id = customer.ID,
                            name = customer.Name,
                            phone = customer.Phone,
                            notes = customer.Notes,
                            address = customer.Address
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
                    Log.d("ERROR: Customer view model", "message: ${response.message}")
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
