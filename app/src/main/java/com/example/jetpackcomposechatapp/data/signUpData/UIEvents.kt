package com.example.jetpackcomposechatapp.data.signUpData

sealed class UIEvents {

    data class FirstName(val firstName: String) : UIEvents()
    data class Number(val number: String) : UIEvents()
    data class Email(val email: String) : UIEvents()
    data class Password(val password: String) : UIEvents()

    object SignUpButtonClick : UIEvents()

}