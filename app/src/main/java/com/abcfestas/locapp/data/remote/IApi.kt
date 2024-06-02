package com.abcfestas.locapp.data.remote

import com.abcfestas.locapp.data.remote.responses.customer.CustomerListResponse
import com.abcfestas.locapp.data.remote.responses.customer.CustomerResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductListResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApi {

    @GET("customers")
    suspend fun getCustomers(
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10
    ): CustomerListResponse

    @GET("customers/{id}")
    suspend fun getCustomerById(@Path("id") id: Int): CustomerResponse

    @GET("products")
    suspend fun getProducts(
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10
    ): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponse
}