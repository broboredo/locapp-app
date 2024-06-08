package com.abcfestas.locapp.data.remote.responses.product

data class ProductPaginatedResponse(
    val `data`: List<ProductData>,
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val totalPages: Int
)