package com.abcfestas.locapp.viewmodel.product

data class ProductFormState(
    val id: Int? = null,
    val name: String? = "",
    val nameError: String? = null,
    val quantity: Int? = 1,
    val quantityError: String? = null,
    val price: Double? = 1.00,
    val priceError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val imagePath: String? = null
)