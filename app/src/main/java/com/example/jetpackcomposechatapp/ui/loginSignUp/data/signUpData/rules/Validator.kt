package com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.rules

object Validator {

    private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}+"

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




    fun emailValidation(email: String): com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.rules.ValidationResult {

        if (!email.matches(Regex(emailPattern))) {
            return com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.rules.ValidationResult(
                status = false,
                errorMessage = "Enter a valid Email"
            )
        }
        return com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.rules.ValidationResult(
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