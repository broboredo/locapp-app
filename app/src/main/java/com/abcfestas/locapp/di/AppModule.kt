package com.abcfestas.locapp.di

import android.content.Context
import com.abcfestas.locapp.data.remote.IApi
import com.abcfestas.locapp.repository.CustomerRepository
import com.abcfestas.locapp.repository.ProductRepository
import com.abcfestas.locapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val api: IApi
    val productRepository: ProductRepository
    val customerRepository: CustomerRepository
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val api: IApi by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.API_BASE_URL)
            .build()
            .create(IApi::class.java)
    }

    override val productRepository: ProductRepository by lazy {
        ProductRepository(api)
    }

    override val customerRepository: CustomerRepository by lazy {
        CustomerRepository(api)
    }
}