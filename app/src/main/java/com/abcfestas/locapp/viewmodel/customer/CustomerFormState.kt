package com.abcfestas.locapp.viewmodel.customer

data class CustomerFormState(
    val id: Int? = null,
    val name: String = "",
    val nameError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val notes: String = "",
    val notesError: String? = null
)