package com.example.jetpackcomposechatapp.data.loginData

data class LoginState(
    val email: String = "",
    val emailError: Boolean = false,
    val emailErrorMessage: String = "",
    val password: String = "",
    val passwordError: Boolean = false,
    val passwordErrorMessage: String = "",
)
