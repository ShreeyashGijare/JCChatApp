package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_MESSAGES
import com.example.jetpackcomposechatapp.utils.Constants.MESSAGES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _chatState = mutableStateOf(ChatState())

    var currentUser: UserData? = null
    private var receiverUser: UserData? = null

    private val _chatMessages = MutableStateFlow<List<ChatState>>(emptyList())
    val chatMessages: StateFlow<List<ChatState>> = _chatMessages

    fun setReceiverUser(receiverUser: UserData) {
        this.receiverUser = receiverUser
    }

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

    fun getUserChats() {
        viewModelScope.launch {
            db.collection(CHAT_MESSAGES)
                .document(generateChatId(auth.currentUser?.uid!!, receiverUser?.userId!!))
                .collection(MESSAGES)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapShot, e ->
                    if (e != null) {
                        Log.i("ChatRepository", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapShot != null && !snapShot.isEmpty) {
                        viewModelScope.launch {
                            _chatMessages.emit(snapShot.toObjects(ChatState::class.java))
                        }
                    }
                }
        }
    }

    fun sendMessage(message: String) {

        val currentUserListObject = ChatUserObject(
            imageUrl = currentUser?.imageUrl,
            name = currentUser?.name,
            number = currentUser?.number,
            emailId = currentUser?.emailId,
            userId = currentUser?.userId
        )

        val receiverUserListObject = ChatUserObject(
            imageUrl = receiverUser?.imageUrl,
            name = receiverUser?.name,
            number = receiverUser?.number,
            emailId = receiverUser?.emailId,
            userId = receiverUser?.userId
        )

        _chatState.value = _chatState.value.copy(
            message = message,
            timeStamp = Calendar.getInstance().timeInMillis,
            senderId = auth.currentUser?.uid!!,
            receiverId = receiverUser?.userId!!
        )


        //check receiver user exists in sender chat list and update
        /*db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
            .collection(CHAT_LIST_USER_NODE)
            .document(receiverUser?.userId!!)
            .set(receiverUserListObject)*/
        val receiverUserObjectRef = db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
            .collection(CHAT_LIST_USER_NODE)
            .document(receiverUser?.userId!!)

        receiverUserObjectRef.get().addOnSuccessListener {
            if (!it.exists()) {
                receiverUserObjectRef.set(receiverUserListObject)
            } else {
                Log.i("ChatMessage-receiver", " -->  Exists")
            }
        }

        //check sender user exists in receiver chat list and update
        /*db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!)
            .collection(CHAT_LIST_USER_NODE).document(auth.currentUser?.uid!!)
            .set(currentUserListObject)*/
        val senderUserObjectRef = db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!)
            .collection(CHAT_LIST_USER_NODE).document(auth.currentUser?.uid!!)

        senderUserObjectRef.get().addOnSuccessListener {
            if (!it.exists()) {
                senderUserObjectRef.set(currentUserListObject)
            } else {
                Log.i("ChatMessage-sender", " -->  Exists")
            }
        }

        //Add Message to chatRoom
        db.collection(CHAT_MESSAGES)
            .document(generateChatId(auth.currentUser?.uid!!, receiverUser?.userId!!))
            .collection(MESSAGES)
            .add(_chatState.value)
    }

    private fun generateChatId(senderId: String, receiverId: String): String {
        return if (senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"
    }
}