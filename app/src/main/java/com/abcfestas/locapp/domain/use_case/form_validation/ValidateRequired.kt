package com.abcfestas.locapp.domain.use_case.form_validation


class ValidateRequired {

    // TODO get string from form_validation resource, but this method cant be a composable
    fun execute(word: String): ValidateResult {
        if(word.isBlank()) {
            return ValidateResult(
                success = false,
                errorMessage = "O campo é obrigatório"
            )
        }

        return ValidateResult(success = true)
    }
}