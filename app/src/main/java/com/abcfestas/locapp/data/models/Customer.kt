package com.abcfestas.locapp.data.models

data class Customer(
    val id: Int,
    var name: String,
    var address: String?,
    var phone: String?,
    var notes: String?
)
