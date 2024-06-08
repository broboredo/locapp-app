package com.abcfestas.locapp.view.screens.customer

sealed class NewCustomerFormEvent {
    data class NameChanged(val name: String): NewCustomerFormEvent()
    data class PhoneChanged(val phone: String): NewCustomerFormEvent()
    data class EmailChanged(val email: String): NewCustomerFormEvent()
    data class AddressChanged(val address: String): NewCustomerFormEvent()
    data class NotesChanged(val notes: String): NewCustomerFormEvent()

    data object Submit: NewCustomerFormEvent()
}