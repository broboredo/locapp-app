package com.abcfestas.locapp.view.screens.customer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.abcfestas.locapp.ui.theme.Red
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.screens.product.WizardFormTopNavigation
import com.abcfestas.locapp.viewmodel.customer.NewCustomerViewModel
import com.abcfestas.locapp.viewmodel.viewModelFactory

@Composable
fun NewCustomerScreen (
    navController: NavController,
    viewModel: NewCustomerViewModel = viewModel(factory = viewModelFactory {
        NewCustomerViewModel(LocAppApplication.appModule.customerRepository)
    })
) {
    val formState = viewModel.state
    val context = LocalContext.current
    val successMessage = stringResource(id = R.string.customer_created_successfully)

    // Coletar os eventos do ViewModel
    LaunchedEffect(Unit) {
        viewModel._validationEvents.collect { event ->
            when(event) {
                is NewCustomerViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }

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
                title = stringResource(R.string.new_customer),
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.name,
                    onValueChange = {
                        viewModel.onEvent(NewCustomerFormEvent.NameChanged(it))
                    },
                    isError = formState.nameError != null,
                    placeholder = {
                        Text(text = "Nome")
                    }
                )
                if (formState.nameError != null) {
                    Text(text = formState.nameError, color = Red)
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.phone,
                    onValueChange = {
                        viewModel.onEvent(NewCustomerFormEvent.PhoneChanged(it))
                    },
                    isError = formState.phoneError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(text = "Telefone")
                    }
                )
                if (formState.phoneError != null) {
                    Text(text = formState.phoneError, color = Red)
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.email,
                    onValueChange = {
                        viewModel.onEvent(NewCustomerFormEvent.EmailChanged(it))
                    },
                    isError = formState.emailError != null,
                    placeholder = {
                        Text(text = "E-mail")
                    }
                )
                if (formState.emailError != null) {
                    Text(text = formState.emailError, color = Red)
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.address,
                    onValueChange = {
                        viewModel.onEvent(NewCustomerFormEvent.AddressChanged(it))
                    },
                    singleLine = false,
                    minLines = 5,
                    isError = formState.addressError != null,
                    placeholder = {
                        Text(text = "Endereço")
                    }
                )
                if (formState.addressError != null) {
                    Text(text = formState.addressError, color = Red)
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.notes,
                    onValueChange = {
                        viewModel.onEvent(NewCustomerFormEvent.NotesChanged(it))
                    },
                    singleLine = false,
                    minLines = 5,
                    isError = formState.notesError != null,
                    placeholder = {
                        Text(text = "Notas")
                    }
                )
                if (formState.notesError != null) {
                    Text(text = formState.notesError, color = Red)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(
            label = stringResource(id = R.string.save),
            onClick = {
                viewModel.onEvent(NewCustomerFormEvent.Submit)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewCustomerScreenPreview() {
    val navController = rememberNavController()
    NewCustomerScreen(navController)
}