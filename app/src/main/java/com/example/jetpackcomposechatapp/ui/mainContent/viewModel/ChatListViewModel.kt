package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.ErrorState
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var currentUser = MutableStateFlow(UserData())

    private val _chatUserList = MutableStateFlow<List<ChatUserObject>>(emptyList())
    val chatUserList: StateFlow<List<ChatUserObject>> = _chatUserList.asStateFlow()

    private val _error = MutableStateFlow(ErrorState())
    val error: StateFlow<ErrorState> = _error

    /*init {
        getUserChats()
    }*/

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
                    val user = value.toObject(UserData::class.java)
                    currentUser.value = user!!
                }
            }
        }
    }


    fun getUserChats() {
        db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
                .collection(CHAT_LIST_USER_NODE)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _error.update {errorState ->
                        errorState.copy(
                            isError = true,
                            errorMessage = error.message.toString()
                        )
                    }
                }
                if (value != null) {
                    _chatUserList.update {
                        value.toObjects()
                    }
                }
            }
    }
}