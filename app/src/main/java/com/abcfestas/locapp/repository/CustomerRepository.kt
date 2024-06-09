package com.abcfestas.locapp.repository

import android.util.Log
import com.abcfestas.locapp.data.remote.IApi
import com.abcfestas.locapp.data.remote.responses.customer.CustomerListResponse
import com.abcfestas.locapp.data.remote.responses.customer.CustomerResponse
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.viewmodel.customer.CustomerFormState

class CustomerRepository(
    private val api: IApi
) {
    suspend fun getCustomers(
        search: String? = null,
        page: Int = 1,
        perPage: Int = 10
    ): Resource<CustomerListResponse> {
        val response = try {
            api.getCustomers(search, page, perPage)
        } catch (e: Exception) {
            Log.d("ERROR: CustomerRepository::getCustomers", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun getCustomerById(id: Int): Resource<CustomerResponse> {
        val response = try {
            api.getCustomerById(id)
        } catch (e: Exception) {
            Log.d("ERROR: CustomerRepository::getCustomerById", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun createCustomer(customer: CustomerFormState): Resource<CustomerResponse> {
        val response = try {
            api.createCustomer(customer)
        } catch (e: Exception) {
            Log.d("ERROR: CustomerRepository::createCustomer", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun updateCustomer(id: Int, customer: CustomerFormState): Resource<CustomerResponse> {
        val response = try {
            api.updateCustomer(id, customer)
        } catch (e: Exception) {
            Log.d("ERROR: CustomerRepository::updateCustomer", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }
}