package com.abcfestas.locapp.view.screens.customer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.R
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.components.TextInputFieldWithError
import com.abcfestas.locapp.view.screens.product.WizardFormTopNavigation
import com.abcfestas.locapp.viewmodel.customer.CustomerFormEvent
import com.abcfestas.locapp.viewmodel.customer.CustomerFormViewModel
import com.abcfestas.locapp.viewmodel.viewModelFactory

@Composable
fun CustomerFormScreen (
    navController: NavController,
    customerId: Int? = null,
    viewModel: CustomerFormViewModel = viewModel(factory = viewModelFactory {
        CustomerFormViewModel(LocAppApplication.appModule.customerRepository)
    })
) {
    val isUpdateForm = customerId != null
    val formState = viewModel.state
    val context = LocalContext.current
    val successMessage = stringResource(
        id = if (isUpdateForm) {
            R.string.customer_updated_successfully
        } else {
            R.string.customer_created_successfully
        }
    )

    LaunchedEffect(Unit) {
        if (customerId != null) {
            viewModel.fetchCustomer(customerId)
        }

        viewModel.validationEvents.collect { event ->
            when(event) {
                is CustomerFormViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }

    if (viewModel.loadingScreen.value) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .align(Alignment.Center),
            ) {

                WizardFormTopNavigation(
                    title = stringResource(
                        id = if (isUpdateForm) {
                            R.string.update_customer
                        } else {
                            R.string.new_customer
                        }
                    ),
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false)
                ) {
                    TextInputFieldWithError(
                        value = formState.name,
                        onValueChange = { viewModel.onEvent(CustomerFormEvent.NameChanged(it)) },
                        isError = formState.nameError != null,
                        errorMessage = formState.nameError,
                        placeholder = stringResource(id = R.string.name)
                    )

                    TextInputFieldWithError(
                        value = formState.phone,
                        onValueChange = { viewModel.onEvent(CustomerFormEvent.PhoneChanged(it)) },
                        isError = formState.phoneError != null,
                        errorMessage = formState.phoneError,
                        placeholder = stringResource(id = R.string.phone),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    TextInputFieldWithError(
                        value = formState.email,
                        onValueChange = { viewModel.onEvent(CustomerFormEvent.EmailChanged(it)) },
                        isError = formState.emailError != null,
                        errorMessage = formState.emailError,
                        placeholder = stringResource(id = R.string.email)
                    )

                    TextInputFieldWithError(
                        value = formState.address,
                        onValueChange = { viewModel.onEvent(CustomerFormEvent.AddressChanged(it)) },
                        isError = formState.addressError != null,
                        errorMessage = formState.addressError,
                        placeholder = stringResource(id = R.string.address),
                        singleLine = false,
                        minLines = 5
                    )

                    TextInputFieldWithError(
                        value = formState.notes,
                        onValueChange = { viewModel.onEvent(CustomerFormEvent.NotesChanged(it)) },
                        isError = formState.notesError != null,
                        errorMessage = formState.notesError,
                        placeholder = stringResource(id = R.string.customer_notes_placeholder),
                        singleLine = false,
                        minLines = 5
                    )
                }
            }

            Button(
                label = stringResource(
                    id = if (isUpdateForm) {
                        R.string.update
                    } else {
                        R.string.save
                    }
                ),
                onClick = {
                    if (isUpdateForm) {
                        viewModel.onEvent(CustomerFormEvent.Update)
                    } else {
                        viewModel.onEvent(CustomerFormEvent.Save)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                loading = viewModel.loadingFormButton.value
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerScreenFormPreview() {
    val navController = rememberNavController()
    CustomerFormScreen(navController)
}