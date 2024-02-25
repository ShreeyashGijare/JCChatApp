package com.example.jetpackcomposechatapp.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.signUpData.SignUpState
import com.example.jetpackcomposechatapp.data.signUpData.UIEvents
import com.example.jetpackcomposechatapp.data.signUpData.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val TAG: String = SignUpViewModel::class.java.name
    private val _signUpState = mutableStateOf(SignUpState())
    var signUpState: State<SignUpState> = _signUpState

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: UIEvents) {

        when (event) {

            is UIEvents.FirstName -> {
                _signUpState.value = signUpState.value.copy(
                    name = event.firstName
                )
            }

            is UIEvents.Email -> {
                _signUpState.value = signUpState.value.copy(
                    email = event.email
                )
            }

            is UIEvents.Number -> {
                _signUpState.value = signUpState.value.copy(
                    number = event.number
                )

            }

            is UIEvents.Password -> {
                _signUpState.value = signUpState.value.copy(
                    password = event.password
                )

            }

            UIEvents.SignUpButtonClick -> {
                signUp(
                    _signUpState.value.name,
                    _signUpState.value.number,
                    _signUpState.value.email,
                    _signUpState.value.password
                )
            }
        }

    }


    fun validateDataWithRules() {

        val nameResult = Validator.firstNameValidation(fName = _signUpState.value.name)
        val numberResult = Validator.numberValidation(number = _signUpState.value.number)
        val emailResult = Validator.emailValidation(email = _signUpState.value.email)
        val passwordResult = Validator.passwordValidation(password = _signUpState.value.password)

        val hasError = listOf(
            nameResult,
            numberResult,
            emailResult,
            passwordResult
        ).any { !it.status }

        if (hasError) {
            _signUpState.value = signUpState.value.copy(
                nameError = nameResult.errorMessage,
                numberError = numberResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }
        viewModelScope.launch {

            validationEventChannel.send(ValidationEvent.Success)

        }
    }

    private fun signUp(name: String, number: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("SIGNUP", "SIGNUP")
            } else {
                Log.i("SIGNUP", "Error")

            }
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}