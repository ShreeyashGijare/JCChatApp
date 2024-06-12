package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatEvents
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    var currentUser: UserData? = null
    private var receiverUser: UserData? = null

    init {
        getUserData(auth.currentUser?.uid!!)
    }

    fun setReceiverUser(receiverUser: UserData) {
        this.receiverUser = receiverUser
        Log.i("CURRENT_USER", this.receiverUser.toString())
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
                    Log.i("CURRENT_USER", currentUser.toString())
                }
            }
        }
    }


    fun onChatEvent(event: ChatEvents) {
        when (event) {
            is ChatEvents.Message -> {
                _chatState.value = _chatState.value.copy(
                    message = event.message
                )
            }

            ChatEvents.SendButtonClick -> {
                db.collection(CHAT_LIST_NODE).document(currentUser?.userId!!).get()
                    .addOnSuccessListener { snapShot ->
                        db.collection(CHAT_LIST_NODE).document(currentUser?.userId!!)
                            .collection(CHAT_LIST_USER_NODE).document(receiverUser?.userId!!)
                            .set(receiverUser!!)
                    }
                db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!).get()
                    .addOnSuccessListener {
                        db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!)
                            .collection(CHAT_LIST_USER_NODE).document(currentUser?.userId!!)
                            .set(currentUser!!)
                    }
            }
        }
    }
}