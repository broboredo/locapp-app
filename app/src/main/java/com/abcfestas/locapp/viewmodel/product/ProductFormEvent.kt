package com.abcfestas.locapp.viewmodel.product

sealed class ProductFormEvent {
    data class NameChanged(val name: String): ProductFormEvent()
    data class QuantityChanged(val quantity: Int): ProductFormEvent()
    data class PriceChanged(val price: Double): ProductFormEvent()
    data class DescriptionChanged(val description: String): ProductFormEvent()

    data object Save: ProductFormEvent()
    data object Update: ProductFormEvent()
}