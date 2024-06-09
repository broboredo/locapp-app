package com.abcfestas.locapp.viewmodel.customer

sealed class CustomerFormEvent {
    data class NameChanged(val name: String): CustomerFormEvent()
    data class PhoneChanged(val phone: String): CustomerFormEvent()
    data class EmailChanged(val email: String): CustomerFormEvent()
    data class AddressChanged(val address: String): CustomerFormEvent()
    data class NotesChanged(val notes: String): CustomerFormEvent()

    data object Save: CustomerFormEvent()
    data object Update: CustomerFormEvent()
}