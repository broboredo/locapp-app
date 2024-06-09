package com.abcfestas.locapp.viewmodel.customer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateEmail
import com.abcfestas.locapp.domain.use_case.form_validation.ValidateRequired
import com.abcfestas.locapp.repository.CustomerRepository
import com.abcfestas.locapp.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CustomerFormViewModel (
    private val repository: CustomerRepository
): ViewModel() {
    var state by mutableStateOf(CustomerFormState())

    private val validateRequired: ValidateRequired = ValidateRequired()
    private val validateEmail: ValidateEmail = ValidateEmail()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    val loadingFormButton = mutableStateOf(false)
    val loadingScreen = mutableStateOf(false)

    fun onEvent(event: CustomerFormEvent) {
        when (event) {
            is CustomerFormEvent.NameChanged -> {
                state = state.copy(name =  event.name)
            }
            is CustomerFormEvent.PhoneChanged -> {
                state = state.copy(phone =  event.phone)
            }
            is CustomerFormEvent.AddressChanged -> {
                state = state.copy(address =  event.address)
            }
            is CustomerFormEvent.EmailChanged -> {
                state = state.copy(email =  event.email)
            }
            is CustomerFormEvent.NotesChanged -> {
                state = state.copy(notes =  event.notes)
            }
            is CustomerFormEvent.Save -> {
                submitForm()
            }
            is CustomerFormEvent.Update -> {
                updateForm()
            }
        }
    }

    private fun validateForm(): Boolean {
        val nameResult = validateRequired.execute(state.name)
        val emailResult = validateEmail.execute(state.email)

        val hasError = listOf(nameResult, emailResult).any { !it.success }

        state = state.copy(
            nameError = nameResult.errorMessage,
            emailError = emailResult.errorMessage
        )

        return !hasError
    }

    private fun submitForm() {
        if (!validateForm()) return

        viewModelScope.launch {
            loadingFormButton.value = true
            when (val result = repository.createCustomer(state)) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: CustomerFormViewModel::submitForm", "Failed to submit form: ${result.message}")
                    TODO()
                }
            }
            loadingFormButton.value = false
        }
    }

    fun fetchCustomer(customerId: Int) {
        viewModelScope.launch {
            loadingScreen.value = true
            when (val result = repository.getCustomerById(customerId)) {
                is Resource.Success -> {
                    val customer = result.data!!.data
                    state = state.copy(
                        id = customer.ID,
                        name = customer.Name,
                        address = customer.Address,
                        phone = customer.Phone,
                        email = customer.Email,
                        notes =  customer.Notes
                    )
                }
                is Resource.Error -> {
                    Log.d("ERROR: CustomerFormViewModel::fetchCustomer", "Failed to fetch customer: ${result.message}")
                    TODO()
                }
            }
            loadingScreen.value = false
        }
    }

    private fun updateForm() {
        if (!validateForm()) return

        viewModelScope.launch {
            loadingFormButton.value = true
            val result = state.id?.let { repository.updateCustomer(it, state) }
            when (result) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success)
                }
                is Resource.Error -> {
                    Log.d("ERROR: CustomerFormViewModel::updateForm", "Failed to update form: ${result.message}")
                    TODO()
                }

                null -> {
                    Log.d("ERROR: CustomerFormViewModel::updateForm", "Customer ID is null")
                    TODO()
                }
            }
            loadingFormButton.value = false
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}
