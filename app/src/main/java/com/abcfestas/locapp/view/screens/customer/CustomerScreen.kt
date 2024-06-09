package com.abcfestas.locapp.view.screens.customer

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.LocAppApplication
import com.abcfestas.locapp.R
import com.abcfestas.locapp.data.models.Customer
import com.abcfestas.locapp.ui.theme.Green400
import com.abcfestas.locapp.ui.theme.Green500
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.view.components.Button
import com.abcfestas.locapp.view.components.LostConnection
import com.abcfestas.locapp.view.navigation.ScreensEnum
import com.abcfestas.locapp.viewmodel.customer.CustomerViewModel
import com.abcfestas.locapp.viewmodel.viewModelFactory
import kotlinx.coroutines.launch

@Composable
fun CustomerScreen(
    navController: NavController
) {
    // TODO use LayoutDefault - remove modifier.scroll from layoutdefault
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f, false)) {
            Text(text = stringResource(R.string.customers), style = Typography.titleLarge)

            ListCustomers(navController = navController)
        }

        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            label = stringResource(R.string.new_customer),
            onClick = {
                navController.navigate(ScreensEnum.CustomerFormScreen.route)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCustomers(
    navController: NavController,
    viewModel: CustomerViewModel = viewModel(factory = viewModelFactory {
        CustomerViewModel(LocAppApplication.appModule.customerRepository)
    })
) {
    val customerList by remember { viewModel.customers }
    val loading by remember { viewModel.loading }
    var hasNextPage by remember { viewModel.hasNextPage }
    var currentPage by remember { viewModel.currentPage }
    val error by remember { viewModel.error }
    var search by remember { viewModel.search }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // search
    var expanded by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCustomers()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (
                    hasNextPage &&
                    visibleItems.isNotEmpty() &&
                    visibleItems.last().index == customerList.lastIndex
                ) {
                    coroutineScope.launch {
                        currentPage++
                        viewModel.loadCustomers()
                    }
                }
            }
    }

    SearchBar(
        modifier = Modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = search,
                onQueryChange = {
                    search = it
                    viewModel.loadCustomers()
                },
                onSearch = {
                    expanded = false
                    viewModel.loadCustomers()
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(stringResource(id = R.string.customer_search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            )
        },
        expanded = false,
        onExpandedChange = { expanded = it },
        content = {}
    )
    Spacer(modifier = Modifier.height(24.dp))

    LazyColumn(state = listState) {
        items(customerList) {
            CustomerBox(navController = navController, customer =  it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            if (loading) {
                CircularProgressIndicator(
                    color = Green500
                )
            } else if (error.isNotEmpty()) {
                LostConnection(message = error) { viewModel.loadCustomers() }
            }
        }
    }
}

@Composable
fun CustomerBox(
    navController: NavController,
    customer: Customer
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(ScreensEnum.CustomerDetailsScreen.route.replace("{customerId}", customer.id.toString()))
                Log.d("CustomerScreen", "Customer #${customer.id} ${customer.name} clicked")
            }
    ) {
        Column {
            Text(text = customer.name, style = Typography.bodyLarge)
            Text(text = customer.phone!!, style = Typography.bodyMedium)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(color = Green400)
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun CustomerScreenPreview() {
    val navController = rememberNavController()
    CustomerScreen(navController)
}