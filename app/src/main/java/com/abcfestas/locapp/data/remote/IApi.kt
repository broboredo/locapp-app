package com.abcfestas.locapp.data.remote

import com.abcfestas.locapp.data.remote.responses.customer.CustomerListResponse
import com.abcfestas.locapp.data.remote.responses.customer.CustomerResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductListResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductResponse
import com.abcfestas.locapp.viewmodel.customer.CustomerFormState
import com.abcfestas.locapp.viewmodel.product.ProductFormState
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("customers")
    suspend fun createCustomer(
        @Body customer: CustomerFormState
    ): CustomerResponse

    @PUT("customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body customer: CustomerFormState
    ): CustomerResponse

    @GET("products")
    suspend fun getProducts(
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10
    ): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponse

    @Multipart
    @PUT("products/{id}/image")
    suspend fun syncProductImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): ProductResponse
    @POST("products")
    suspend fun createProduct(
        @Body product: ProductFormState
    ): ProductResponse

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductFormState
    ): ProductResponse
}