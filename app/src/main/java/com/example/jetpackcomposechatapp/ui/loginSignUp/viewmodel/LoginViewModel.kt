package com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.LoginEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.LoginState
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.loginData.rules.Validator
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val fbMessaging: FirebaseMessaging
) : ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    var loginState: State<LoginState> = _loginState

    private val _LoginSuccess = MutableStateFlow(false)
    var logInSuccess: StateFlow<Boolean> = _LoginSuccess

    var inProgress = mutableStateOf(false)
    private var currentUser = mutableStateOf<UserData?>(null)

    fun onEvent(event: LoginEvents) {
        when (event) {
            LoginEvents.LoginButtonClick -> {
                if (validateEmail() && validatePassword()) {
                    login(
                        _loginState.value.email,
                        _loginState.value.password
                    )
                } else {
                    validateEmail()
                    validatePassword()
                }
            }

            is LoginEvents.Email -> {
                _loginState.value = loginState.value.copy(
                    email = event.email
                )
                validateEmail()
            }

            is LoginEvents.Password -> {
                _loginState.value = loginState.value.copy(
                    password = event.password
                )
                validatePassword()
            }
        }
    }

    private fun login(email: String, password: String) {
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("LoginNJNN", "Success")

                inProgress.value = false
                auth.currentUser?.uid?.let { userId ->
                    getUserData(userId)
                }
            } else {
                Log.i("LoginNJNN", it.exception.toString())
            }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(Constants.USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
            }
            if (value != null) {
                val user = value.toObject<UserData>()
                this.currentUser.value = user
                inProgress.value = false
                _LoginSuccess.value = true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailResult = Validator.emailValidation(email = _loginState.value.email)
        _loginState.value = loginState.value.copy(
            emailError = !emailResult.status,
            emailErrorMessage = emailResult.errorMessage
        )
        return emailResult.status
    }

    private fun validatePassword(): Boolean {
        val passwordResult = Validator.passwordValidation(password = _loginState.value.password)
        _loginState.value = loginState.value.copy(
            passwordError = !passwordResult.status,
            passwordErrorMessage = passwordResult.errorMessage
        )
        return passwordResult.status
    }
}