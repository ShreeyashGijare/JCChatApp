package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {

    fun onProfileEvents(event: ProfileEvents) {
        when (event) {
            is ProfileEvents.UploadProfileImage ->
                uploadImageToFireBaseStorage(event.profileImage)
        }
    }


    private fun uploadImageToFireBaseStorage(profileImage: ByteArray) {


    }
}


sealed class ProfileEvents {

    data class UploadProfileImage(val profileImage: ByteArray) : ProfileEvents()
}