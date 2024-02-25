package com.example.jetpackcomposechatapp.data.signUpData

data class SignUpState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: String = "",
    val numberError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
)
