package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
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
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {


    private val _chatState = mutableStateOf(ChatState())

    var currentUser: UserData? = null

    private var _receiverUser: MutableStateFlow<UserData> = MutableStateFlow(UserData())
    val receiverUser: StateFlow<UserData> get() = _receiverUser

    private val _chatMessages = MutableStateFlow<List<ChatState>>(emptyList())
    val chatMessages: StateFlow<List<ChatState>> = _chatMessages

    fun setReceiverUser(receiverUser: UserData) {
        this._receiverUser.value = receiverUser
        getReceiverUserData(receiverUser.userId!!)
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

    private fun getReceiverUserData(uid: String) {
        viewModelScope.launch {
            db.collection(Constants.USER_NODE).document(uid).addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("ERROR_CHAT", error.message.toString())
                }
                if (value != null) {
                    val user = value.toObject<UserData>()
                    _receiverUser.value = user!!
                }
            }
        }
    }

    fun getUserChats() {
        viewModelScope.launch {
            db.collection(CHAT_MESSAGES)
                .document(generateChatId(auth.currentUser?.uid!!, _receiverUser.value.userId!!))
                .collection(MESSAGES)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
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

    fun onEvents(event: ChatEvents) {

        when (event) {
            is ChatEvents.Message -> {
                sendMessage(message = event.message, messageType = event.messageType)
            }

            is ChatEvents.UploadImage -> {
                uploadChatImage(
                    event.image
                ) { imagePath ->
                    sendMessage(
                        message = imagePath,
                        messageType = event.messageType
                    )
                }
            }
        }
    }

    private fun uploadChatImage(image: Uri, imageUploadSuccess: (String) -> Unit) {
        val imageRef = storage.reference.child("chatImages/${System.currentTimeMillis()}")
        if (image.path != "") {
            imageRef.putFile(image)
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


    private fun sendMessage(message: String, messageType: MessageType) {

        val currentUserListObject = ChatUserObject(
            imageUrl = currentUser?.imageUrl,
            name = currentUser?.name,
            number = currentUser?.number,
            emailId = currentUser?.emailId,
            userId = currentUser?.userId,
        )

        val receiverUserListObject = ChatUserObject(
            imageUrl = _receiverUser.value.imageUrl,
            name = _receiverUser.value.name,
            number = _receiverUser.value.number,
            emailId = _receiverUser.value.emailId,
            userId = _receiverUser.value.userId
        )

        _chatState.value = _chatState.value.copy(
            message = message,
            timeStamp = Calendar.getInstance().timeInMillis,
            senderId = auth.currentUser?.uid!!,
            receiverId = _receiverUser.value.userId!!,
            messageType = messageType
        )

        val receiverUserObjectRef = db.collection(CHAT_LIST_NODE).document(auth.currentUser?.uid!!)
            .collection(CHAT_LIST_USER_NODE)
            .document(_receiverUser.value.userId!!)

        receiverUserObjectRef.get().addOnSuccessListener {
            if (!it.exists()) {
                receiverUserObjectRef.set(receiverUserListObject)
            } else {
                Log.i("ChatMessage-receiver", " -->  Exists")
                receiverUserObjectRef.set(
                    receiverUserListObject.copy(
                        timeStamp = Calendar.getInstance().timeInMillis,
                        lastMessage = message,
                        lastMessageType = messageType
                    )
                )
            }
        }

        val senderUserObjectRef =
            db.collection(CHAT_LIST_NODE).document(_receiverUser.value.userId!!)
                .collection(CHAT_LIST_USER_NODE).document(auth.currentUser?.uid!!)

        senderUserObjectRef.get().addOnSuccessListener {
            if (!it.exists()) {
                senderUserObjectRef.set(currentUserListObject)
            } else {
                Log.i("ChatMessage-sender", " -->  Exists")
                senderUserObjectRef.set(
                    currentUserListObject.copy(
                        timeStamp = Calendar.getInstance().timeInMillis,
                        lastMessage = message,
                        lastMessageType = messageType
                    )
                )
            }
        }

        //Add Message to chatRoom
        db.collection(CHAT_MESSAGES)
            .document(generateChatId(auth.currentUser?.uid!!, _receiverUser.value.userId!!))
            .collection(MESSAGES)
            .add(_chatState.value)
    }

    private fun addReaction() {

    }

    private fun generateChatId(senderId: String, receiverId: String): String {
        return if (senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"
    }
}

sealed class ChatEvents {

    data class UploadImage(val image: Uri, val messageType: MessageType) : ChatEvents()
    data class Message(val message: String, val messageType: MessageType) : ChatEvents()
}