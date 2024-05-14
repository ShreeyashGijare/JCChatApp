package com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData

data class SignUpState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: Boolean = false,
    val nameErrorMessage: String = "",
    val numberError: Boolean = false,
    val numberErrorMessage: String = "",
    val emailError: Boolean = false,
    val emailErrorMessage: String = "",
    val passwordError: Boolean = false,
    val passwordErrorMessage: String = ""
)
