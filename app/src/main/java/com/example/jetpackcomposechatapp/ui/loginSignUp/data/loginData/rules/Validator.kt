package com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.rules

object Validator {

    private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}+"

    fun emailValidation(email: String): ValidationResult {

        if (!email.matches(Regex(emailPattern))) {
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
        return ValidationResult(
            status = password.isNotEmpty(),
            errorMessage = "Please enter a password"
        )
    }
}

data class ValidationResult(
    val status: Boolean = false,
    val errorMessage: String = ""
)