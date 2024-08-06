package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.ErrorState
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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


    private val _chatUserList = MutableStateFlow<List<ChatUserObject>>(emptyList())
    val chatUserList: StateFlow<List<ChatUserObject>> = _chatUserList

    private val _error = MutableStateFlow<ErrorState>(ErrorState())
    val error: StateFlow<ErrorState> = _error

    init {
        getUserChats()
    }

    private fun getUserChats() {
        db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
            .collection(CHAT_LIST_USER_NODE)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _error.emit(
                            _error.value.copy(
                                isError = true,
                                errorMessage = error.message.toString()
                            )
                        )
                    }
                }
                if (value != null) {
                    viewModelScope.launch {
                        _chatUserList.emit(value.toObjects<ChatUserObject>())
                    }
                }

            }
    }

}