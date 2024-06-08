package com.abcfestas.locapp.domain.use_case.form_validation


import android.util.Patterns

class ValidateEmail {

    // TODO get string from form_validation resource, but this method cant be a composable
    fun execute(email: String): ValidateResult {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidateResult(
                success = false,
                errorMessage = "Este não é um e-mail válido"
            )
        }

        return ValidateResult(success = true)
    }
}