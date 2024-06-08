package com.abcfestas.locapp.domain.use_case.form_validation


class ValidateMaxChars {

    // TODO get string from form_validation resource, but this method cant be a composable
    fun execute(word: String, maxLength: Int = 255): ValidateResult {
        if (word.isNotBlank() && word.length > maxLength) {
            return ValidateResult(
                success = false,
                errorMessage = "O máximo de caracteres são $maxLength caracteres"
            )
        }

        return ValidateResult(success = true)
    }
}