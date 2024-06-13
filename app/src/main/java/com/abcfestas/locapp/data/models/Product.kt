package com.abcfestas.locapp.data.models

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val quantity: Int = 1,
    val imagePath: String? = null
)
