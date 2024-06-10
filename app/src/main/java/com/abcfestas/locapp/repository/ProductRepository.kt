package com.abcfestas.locapp.repository

import android.util.Log
import com.abcfestas.locapp.data.remote.IApi
import com.abcfestas.locapp.data.remote.responses.product.ProductListResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductResponse
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.viewmodel.product.ProductFormState

class ProductRepository(
    private val api: IApi
) {
    suspend fun getProducts(
        search: String? = null,
        page: Int = 1,
        perPage: Int = 10
    ): Resource<ProductListResponse> {
        val response = try {
            api.getProducts(search, page, perPage)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::getProducts", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun getProductById(id: Int): Resource<ProductResponse> {
        val response = try {
            api.getProductById(id)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::getProductById", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun createProduct(product: ProductFormState): Resource<ProductResponse> {
        val response = try {
            api.createProduct(product)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::createProduct", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun updateProduct(id: Int, product: ProductFormState): Resource<ProductResponse> {
        val response = try {
            api.updateProduct(id, product)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::updateProduct", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }
}