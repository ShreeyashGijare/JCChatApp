package com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData

import android.net.Uri

sealed class SignUpEvents {

    data class Name(val name: String) : SignUpEvents()
    data class Number(val number: String) : SignUpEvents()
    data class Email(val email: String) : SignUpEvents()
    data class Password(val password: String) : SignUpEvents()

    data class UploadImage(val imageUri: Uri) : SignUpEvents()

    object SignUpButtonClick : SignUpEvents()

}