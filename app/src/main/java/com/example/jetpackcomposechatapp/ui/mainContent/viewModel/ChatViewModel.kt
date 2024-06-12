package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatEvents
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState


    fun onChatEvent(event: ChatEvents) {
        when (event) {
            is ChatEvents.Message -> {
                _chatState.value = _chatState.value.copy(
                    message = event.message
                )
            }

            ChatEvents.SendButtonClick -> {
            }
        }
    }
}