package com.abcfestas.locapp.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

open class BaseListViewModel (): ViewModel()
{
    var currentPage = mutableIntStateOf(1)
    var hasNextPage = mutableStateOf(false)
    var search = mutableStateOf("")
    var error = mutableStateOf("")
    var loading = mutableStateOf(false)

    init {
        search.value = ""
    }
}
