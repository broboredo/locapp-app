package com.abcfestas.locapp.view.screens.customer

data class NewCustomerFormState(
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