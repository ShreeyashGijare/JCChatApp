package com.example.jetpackcomposechatapp.data.signUpData.rules

import android.util.Patterns

object Validator {

    fun userNameValidation(fName: String): ValidationResult {
        return ValidationResult(
            (fName.isNotEmpty() && fName.length >= 3),
            errorMessage = "Please enter a valid name"
        )
    }

    fun numberValidation(number: String): ValidationResult {
        return ValidationResult(
            status = (number.length == 10),
            errorMessage = "Please enter a valid phone number"
        )
    }


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

        if (password.length < 8) {
            return ValidationResult(
                status = false,
                errorMessage = "The password must consist of at least 8 characters"
            )
        }

        val containsLettersAndDigits =
            password.any { it.isLetter() } && password.any { it.isUpperCase() } && password.any { it.isDigit() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                status = false,
                errorMessage = "The password must contain one digit, one capital letter"
            )
        }
        return ValidationResult(
            status = true
        )
    }
}

data class ValidationResult(
    val status: Boolean = false,
    val errorMessage: String = ""
)