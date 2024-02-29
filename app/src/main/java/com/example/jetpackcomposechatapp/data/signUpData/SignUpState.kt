package com.example.jetpackcomposechatapp.data.signUpData

data class SignUpState(
    val name: String = "",
    val number: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: Boolean = false,
    val numberError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false
)
