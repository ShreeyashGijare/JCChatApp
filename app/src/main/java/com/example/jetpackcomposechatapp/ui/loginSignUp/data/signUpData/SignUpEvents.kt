package com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData

sealed class SignUpEvents {

    data class Name(val name: String) : SignUpEvents()
    data class Number(val number: String) : SignUpEvents()
    data class Email(val email: String) : SignUpEvents()
    data class Password(val password: String) : SignUpEvents()

    object SignUpButtonClick : SignUpEvents()

}