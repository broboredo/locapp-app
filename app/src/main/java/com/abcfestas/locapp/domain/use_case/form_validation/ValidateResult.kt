package com.abcfestas.locapp.domain.use_case.form_validation

data class ValidateResult(
    val success: Boolean,
    val errorMessage: String? = null
)