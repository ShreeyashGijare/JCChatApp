package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.BuildConfig
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist.ChatUserObject
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_MESSAGES
import com.example.jetpackcomposechatapp.utils.Constants.MESSAGES
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val googleCredentials: GoogleCredentials
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

            is ChatEvents.AddReaction -> {
                addReaction(
                    messageId = event.messageId,
                    reaction = event.reaction
                )
            }

            is ChatEvents.RemoveReaction -> {
                removeReaction(
                    messageId = event.messageId,
                    reaction = event.reaction
                )
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
            messageId = UUID.randomUUID().toString().substring(0, 15),
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
            receiverUserObjectRef.set(
                receiverUserListObject.copy(
                    timeStamp = Calendar.getInstance().timeInMillis,
                    lastMessage = message,
                    lastMessageType = messageType
                )
            )
        }

        val senderUserObjectRef =
            db.collection(CHAT_LIST_NODE).document(_receiverUser.value.userId!!)
                .collection(CHAT_LIST_USER_NODE).document(auth.currentUser?.uid!!)

        senderUserObjectRef.get().addOnSuccessListener {
            senderUserObjectRef.set(
                currentUserListObject.copy(
                    timeStamp = Calendar.getInstance().timeInMillis,
                    lastMessage = message,
                    lastMessageType = messageType
                )
            )
        }

        //Add Message to chatRoom
        db.collection(CHAT_MESSAGES)
            .document(generateChatId(auth.currentUser?.uid!!, _receiverUser.value.userId!!))
            .collection(MESSAGES)
            .document(_chatState.value.messageId).set(_chatState.value).addOnSuccessListener {
                sendNotification(message, messageType)
            }
    }

    private fun addReaction(messageId: String, reaction: String) {
        db.collection(CHAT_MESSAGES)
            .document(generateChatId(auth.currentUser?.uid!!, _receiverUser.value.userId!!))
            .collection(MESSAGES)
            .document(messageId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    document.reference.update(
                        "messageReactions",
                        FieldValue.arrayUnion(reaction)
                    )
                        .addOnSuccessListener {
                            Log.d("Chat-Reaction", "Reaction added successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Chat-Reaction", e.message.toString())
                        }
                }
            }.addOnFailureListener { e ->
                Log.d("Chat-Reaction", e.message.toString())
            }
    }

    private fun removeReaction(messageId: String, reaction: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection(CHAT_MESSAGES)
                .document(generateChatId(auth.currentUser?.uid!!, _receiverUser.value.userId!!))
                .collection(MESSAGES)
                .document(messageId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        document.reference.update(
                            "messageReactions",
                            FieldValue.arrayRemove(reaction)
                        )
                            .addOnSuccessListener {
                                Log.d("Chat-Reaction", "Reaction removed successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Chat-Reaction", e.message.toString())
                            }
                    }
                }.addOnFailureListener { e ->
                    Log.d("Chat-Reaction", e.message.toString())
                }
        }
    }

    private fun generateChatId(senderId: String, receiverId: String): String {
        return if (senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"
    }


    private fun sendNotification(message: String, messageType: MessageType) {
        val notificationObject = JSONObject()
        notificationObject.put("title", currentUser?.name.toString())
        when (messageType) {
            MessageType.MESSAGE -> {
                notificationObject.put("body", message)
            }

            MessageType.IMAGE -> {
                notificationObject.put("body", "\uD83D\uDCF7 Photo")
                notificationObject.put("image", message)
            }
        }
        val dataObject = JSONObject()
        dataObject.put("userId", currentUser?.userId.toString())
        val messageObject = JSONObject()
        messageObject.put("token", receiverUser.value.token)
        messageObject.put("notification", notificationObject)
        messageObject.put("data", dataObject)
        val mainObject = JSONObject()
        mainObject.put("message", messageObject)
        callApi(mainObject)
    }

    private fun callApi(jsonObj: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            googleCredentials.refreshIfExpired()
            val accessToken = googleCredentials.accessToken.tokenValue

            val json = "application/json; charset=utf-8".toMediaType()
            val client = OkHttpClient()
            val url = "https://fcm.googleapis.com/v1/projects/${BuildConfig.FIREBASE_PROJECT_ID}/messages:send"
            val body = jsonObj.toString().toRequestBody(json)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Content-Type", "application/json")
                .build()

            println("Notification: $url")

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Notification: ${e.message}")
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    println("Notification: $responseBody")
                }
            })
        }
    }
}

sealed class ChatEvents {
    data class UploadImage(val image: Uri, val messageType: MessageType) : ChatEvents()

    data class Message(val message: String, val messageType: MessageType) : ChatEvents()

    data class AddReaction(val messageId: String, val reaction: String) :
        ChatEvents()

    data class RemoveReaction(val messageId: String, val reaction: String) :
        ChatEvents()
}