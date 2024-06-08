package com.abcfestas.locapp.viewmodel.customer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateEmail
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateRequired
import com.abcfestas.locapp.repository.CustomerRepository
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.view.screens.customer.NewCustomerFormEvent
import com.abcfestas.locapp.view.screens.customer.NewCustomerFormState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NewCustomerViewModel (
    private val repository: CustomerRepository
): ViewModel() {
    var state by mutableStateOf(NewCustomerFormState())

    private val validateRequired: ValidateRequired = ValidateRequired()
    private val validateEmail: ValidateEmail = ValidateEmail()

    private val validationEventChannel = Channel<ValidationEvent>()
    val _validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: NewCustomerFormEvent) {
        when (event) {
            is NewCustomerFormEvent.NameChanged -> {
                state = state.copy(name =  event.name)
            }
            is NewCustomerFormEvent.PhoneChanged -> {
                state = state.copy(phone =  event.phone)
            }
            is NewCustomerFormEvent.AddressChanged -> {
                state = state.copy(address =  event.address)
            }
            is NewCustomerFormEvent.EmailChanged -> {
                state = state.copy(email =  event.email)
            }
            is NewCustomerFormEvent.NotesChanged -> {
                state = state.copy(notes =  event.notes)
            }
            is NewCustomerFormEvent.Submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        val nameResult = validateRequired.execute(state.name)
        val emailResult = validateEmail.execute(state.email)

        val hasError = listOf(
            nameResult,
            emailResult
        ).any { !it.success }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val result = repository.createCustomer(state)
            when (result) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    // Tratar o erro, se necessário
                }
            }
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}
