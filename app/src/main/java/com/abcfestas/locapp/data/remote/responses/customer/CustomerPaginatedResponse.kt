package com.abcfestas.locapp.data.remote.responses.customer

data class CustomerPaginatedResponse(
    val `data`: List<CustomerData>,
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val totalPages: Int
)