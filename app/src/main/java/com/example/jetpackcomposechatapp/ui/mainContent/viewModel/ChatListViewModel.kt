package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _chatUserList = MutableStateFlow<List<UserData>>(emptyList())
    val chatUserList: StateFlow<List<UserData>> = _chatUserList

    init {
        getUserChats()
    }

    private fun getUserChats() {
        db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
            .collection(CHAT_LIST_USER_NODE).addSnapshotListener { value, error ->
                if (error != null) {

                }
                if (value != null) {
                    viewModelScope.launch {
                        _chatUserList.emit(value.toObjects<UserData>())
                    }
                }

            }
    }

}