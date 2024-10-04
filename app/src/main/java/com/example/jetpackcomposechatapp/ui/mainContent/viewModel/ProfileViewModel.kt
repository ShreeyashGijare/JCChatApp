package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    var currentUser: UserData? = null

    init {
        getUserData(auth.currentUser?.uid!!)
    }

    private fun getUserData(uid: String) {
        viewModelScope.launch {
            db.collection(Constants.USER_NODE).document(uid).addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("ERROR_CHAT", error.message.toString())
                }
                if (value != null) {
                    val user = value.toObject<UserData>()
                    currentUser = user
                }
            }
        }
    }

    fun onProfileEvents(event: ProfileEvents) {
        when (event) {
            is ProfileEvents.UploadProfileImage ->
                uploadImageToFireBaseStorage(event.profileImage) { uploadedUri ->
                    currentUser = currentUser?.copy(
                        imageUrl = uploadedUri
                    )

                    db.collection(Constants.USER_NODE).document(auth.currentUser?.uid.toString()).get()
                        .addOnSuccessListener {
                            db.collection(Constants.USER_NODE).document(auth.currentUser?.uid.toString()).set(currentUser!!)
                        }.addOnFailureListener { exception ->

                        }
                }
        }
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
}


sealed class ProfileEvents {

    data class UploadProfileImage(val profileImage: Uri) : ProfileEvents()
}