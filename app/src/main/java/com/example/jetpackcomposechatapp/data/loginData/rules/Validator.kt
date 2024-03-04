package com.example.jetpackcomposechatapp.data.loginData.rules

import android.util.Patterns

object Validator {

    fun emailValidation(email: String): ValidationResult {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                status = false,
                errorMessage = "Enter a valid Email"
            )
        }
        return ValidationResult(
            status = true
        )
    }

    fun passwordValidation(password: String): ValidationResult {
        return ValidationResult(status = password.isNotEmpty(), errorMessage = "Please enter a password")
    }
}

data class ValidationResult(
    val status: Boolean = false,
    val errorMessage: String = ""
)