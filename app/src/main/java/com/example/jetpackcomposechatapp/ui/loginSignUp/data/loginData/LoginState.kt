package com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData

data class LoginState(
    val email: String = "",
    val emailError: Boolean = false,
    val emailErrorMessage: String = "",
    val password: String = "",
    val passwordError: Boolean = false,
    val passwordErrorMessage: String = "",
)
