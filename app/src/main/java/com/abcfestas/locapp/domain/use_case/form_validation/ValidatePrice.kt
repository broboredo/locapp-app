package com.abcfestas.locapp.domain.use_case.form_validation


class ValidatePrice {

    // TODO get string from form_validation resource, but this method cant be a composable
    fun execute(price: Double?, isRequired: Boolean = false): ValidateResult {
        if (price != null) {
            if(price.isNaN()) {
                return ValidateResult(
                    success = false,
                    errorMessage = "Preço inválido"
                )
            } else if (price <= 0) {
                return ValidateResult(
                    success = false,
                    errorMessage = "Preço é obrigatório"
                )
            }
        } else if (isRequired) {
            return ValidateResult(
                success = false,
                errorMessage = "Preço é obrigatório"
            )
        }

        return ValidateResult(success = true)
    }
}