package com.abcfestas.locapp.domain.use_case.form_validation


class ValidateMinChars {

    // TODO get string from form_validation resource, but this method cant be a composable
    fun execute(word: String, minLength: Int = 3): ValidateResult {
        if (word.isNotBlank() && word.length < minLength) {
            return ValidateResult(
                success = false,
                errorMessage = "O mínimo de caracteres são 255 caracteres"
            )
        }

        return ValidateResult(success = true)
    }
}