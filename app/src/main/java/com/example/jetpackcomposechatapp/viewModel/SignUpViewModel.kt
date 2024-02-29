package com.example.jetpackcomposechatapp.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposechatapp.data.signUpData.SignUpState
import com.example.jetpackcomposechatapp.data.signUpData.UIEvents
import com.example.jetpackcomposechatapp.data.signUpData.rules.Validator
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants.USER_NODE
import com.example.jetpackcomposechatapp.utils.Events
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val TAG: String = SignUpViewModel::class.java.name
    private val _signUpState = mutableStateOf(SignUpState())
    var signUpState: State<SignUpState> = _signUpState

    /*//PLValidation
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()*/

    var inProgress = mutableStateOf(false)
    private val eventMutableState = mutableStateOf<Events<String>?>(null)

    private var signIn = mutableStateOf(false)
    private var user = mutableStateOf<UserData?>(null)

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
                validateDataWithRules()
            }
        }
    }

    private fun validateDataWithRules() {
        val nameResult = Validator.userNameValidation(fName = _signUpState.value.name)
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
                nameError = !nameResult.status,
                numberError = !numberResult.status,
                emailError = !emailResult.status,
                passwordError = !passwordResult.status
            )
            return
        }
        signUp(
            _signUpState.value.name,
            _signUpState.value.number,
            _signUpState.value.email,
            _signUpState.value.password
        )
    }

    private fun signUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("SIGNUP", "SIGNUP")
                signIn.value = true
                createOrUpdateProfile(name, number)
            } else {
                Log.i("SIGNUP", "Error")
                handleException(task.exception, customMessage = "Sign Up Failed")
            }
        }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: user.value?.name,
            number = number ?: user.value?.number,
            imageUrl = imageUrl ?: user.value?.imageUrl
        )

        uid.let {
            inProgress.value = true

            db.collection(USER_NODE).document(uid!!).get().addOnSuccessListener {
                if (it.exists()) {
                    //update the user
                } else {
                    db.collection(USER_NODE).document().set(userData)
                    inProgress.value = false
                    getUserData(uid)
                }
            }.addOnFailureListener {
                handleException(it, "Cannot Retrieve User")
            }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->

            if (error != null) {
                handleException(error, "Cannot Retrieve User")
            }

            if (value != null) {
                val user = value.toObject<UserData>()
                this.user.value = user
                inProgress.value = false
            }
        }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        inProgress.value = false
        Log.e(TAG, "handleException: ", exception)
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage
        eventMutableState.value = Events(message)
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}