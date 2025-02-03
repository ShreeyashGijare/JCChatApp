package com.example.jetpackcomposechatapp.ui.loginSignUp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.SignUpState
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.SignUpEvents
import com.example.jetpackcomposechatapp.ui.loginSignUp.data.signUpData.rules.Validator
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants.USER_NODE
import com.example.jetpackcomposechatapp.utils.Events
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val TAG: String = SignUpViewModel::class.java.name
    private val _signUpState = mutableStateOf(SignUpState())
    var signUpState: State<SignUpState> = _signUpState

    var inProgress = mutableStateOf(false)
    private val eventMutableState = mutableStateOf<Events<String>?>(null)

    private val _signInSuccess = MutableStateFlow(false)
    var signInSuccess: StateFlow<Boolean> = _signInSuccess

    private var _profileImageUri: Uri? = null
    val profileImageUri get() = _profileImageUri

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.Name -> {
                _signUpState.value = _signUpState.value.copy(
                    name = event.name
                )
                validateName()
            }

            is SignUpEvents.Email -> {
                _signUpState.value = _signUpState.value.copy(
                    email = event.email
                )
                validateEmail()
            }

            is SignUpEvents.Number -> {
                _signUpState.value = _signUpState.value.copy(
                    number = event.number
                )
                validateNumber()
            }

            is SignUpEvents.Password -> {
                _signUpState.value = _signUpState.value.copy(
                    password = event.password
                )
                validatePassword()
            }

            SignUpEvents.SignUpButtonClick -> {
                if (validateName() && validateNumber() && validateEmail() && validatePassword() && validateProfileUri()) {

                    uploadImageToFireBaseStorage(profileImageUri!!) { profileImageUrl ->
                        _signUpState.value = _signUpState.value.copy(
                            imageUrl = profileImageUrl
                        )
                        signUp(
                            name = _signUpState.value.name,
                            number = _signUpState.value.number,
                            email = _signUpState.value.email,
                            password = _signUpState.value.password,
                            imageUrl = _signUpState.value.imageUrl
                        )
                    }


                } else {
                    /*validateName()
                    validateNumber()
                    validateEmail()
                    validatePassword()*/
                    validateProfileUri()
                }
            }

            is SignUpEvents.UploadImage -> {
                saveImageUri(event.imageUri)
                /*uploadImageToFireBaseStorage(event.imageUri) {
                    _signUpState.value = _signUpState.value.copy(

                    )
                }*/
                validateProfileUri()
            }
        }
    }

    private fun saveImageUri(profileImageUri: Uri?) {
        this._profileImageUri = profileImageUri
    }

    private fun uploadImageToFireBaseStorage(
        profileImage: Uri,
        imageUploadSuccess: (String) -> Unit
    ) {
        val imageRef = storage.reference.child("images/${System.currentTimeMillis()}")
        if (profileImage.path != "") {
            imageRef.putFile(profileImage)
                .addOnSuccessListener {
                    imageRef.downloadUrl
                        .addOnSuccessListener { firebaseImageUri ->
                            imageUploadSuccess.invoke(firebaseImageUri.toString())
                        }
                }.addOnFailureListener {
                    Log.i("UploadImageToFirebase", "Upload Failed")
                }
        } else {
            Log.i("UploadImageToFirebase", "Empty Image")
        }

    }

    fun validateName(): Boolean {
        val nameResult = Validator.userNameValidation(fName = _signUpState.value.name)
        _signUpState.value = _signUpState.value.copy(
            nameError = !nameResult.status,
            nameErrorMessage = nameResult.errorMessage
        )
        return nameResult.status
    }

    fun validateNumber(): Boolean {
        val numberResult = Validator.numberValidation(number = _signUpState.value.number)
        _signUpState.value = _signUpState.value.copy(
            numberError = !numberResult.status,
            numberErrorMessage = numberResult.errorMessage
        )
        return numberResult.status
    }

    fun validateEmail(): Boolean {
        val emailResult = Validator.emailValidation(email = _signUpState.value.email)
        _signUpState.value = _signUpState.value.copy(
            emailError = !emailResult.status,
            emailErrorMessage = emailResult.errorMessage
        )
        return emailResult.status
    }

    fun validatePassword(): Boolean {
        val passwordResult = Validator.passwordValidation(password = _signUpState.value.password)
        _signUpState.value = _signUpState.value.copy(
            passwordError = !passwordResult.status,
            passwordErrorMessage = passwordResult.errorMessage
        )
        return passwordResult.status
    }

    private fun validateProfileUri(): Boolean {
        val validateProfileUri = _profileImageUri != null
        _signUpState.value = _signUpState.value.copy(
            imageError = !validateProfileUri,
            imageErrorMessage = "Please add a profile image!!"
        )
        return validateProfileUri
    }

    private fun signUp(
        name: String,
        number: String,
        email: String,
        password: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            inProgress.value = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createOrUpdateProfile(
                        name = name,
                        number = number,
                        email = email,
                        imageUrl = imageUrl
                    )
                } else {
                    Log.i("SIGNUP", "Error")
                    handleException(task.exception, customMessage = "Sign Up Failed")
                }
            }
        }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            val user = auth.currentUser
            val uid = user?.uid
            val userData = UserData(
                userId = uid,
                name = name,
                number = number,
                imageUrl = imageUrl,
                emailId = email
            )
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userData.name)
                .build()
            user?.let {
                it.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        uid.let {
                            db.collection(USER_NODE).document(uid!!).get()
                                .addOnSuccessListener { snapShot ->
                                    if (snapShot.exists()) {
                                        //update the user
                                    } else {
                                        db.collection(USER_NODE).document(uid).set(userData)
                                        inProgress.value = false
                                        getUserData(uid)
                                    }
                                }.addOnFailureListener { exception ->
                                    handleException(exception, "Cannot Retrieve User")
                                }
                        }
                    } else {
                        Log.i("ProfileUpdateException", task.exception?.message.toString())
                    }
                }
            }
        }
    }

    private fun getUserData(uid: String) {
        viewModelScope.launch {
            inProgress.value = true
            db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error, "Cannot Retrieve User")
                }
                if (value != null) {
                    val user = value.toObject<UserData>()
                    inProgress.value = false
                    _signInSuccess.value = true
                }
            }
        }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        inProgress.value = false
        Log.e(TAG, "handleException: ", exception)
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMessage else customMessage
        eventMutableState.value = Events(message)
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}