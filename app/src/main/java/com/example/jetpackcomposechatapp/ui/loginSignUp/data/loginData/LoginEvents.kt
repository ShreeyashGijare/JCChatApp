package com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData

sealed class LoginEvents {

    data class Email(val email: String) : LoginEvents()

    data class Password(val password: String) : LoginEvents()

    object LoginButtonClick : LoginEvents()
}