package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatEvents
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.ChatState
import com.example.jetpackcomposechatapp.utils.Constants
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_LIST_USER_NODE
import com.example.jetpackcomposechatapp.utils.Constants.CHAT_MESSAGES
import com.example.jetpackcomposechatapp.utils.Constants.MESSAGES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
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


//    var _chatMessages by mutableStateOf<List<ChatState>>(emptyList())

    private val _chatMessages = MutableStateFlow<List<ChatState>>(emptyList())
    val chatMessages: StateFlow<List<ChatState>> = _chatMessages


    init {
        viewModelScope.launch {

            try {
                val userDataDeferred = async { getUserData(auth.currentUser?.uid!!) }
                userDataDeferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun setReceiverUser(receiverUser: UserData) {
        this.receiverUser = receiverUser
        getUserChats()
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

    private fun getUserChats() {
        viewModelScope.launch {

            //ProperLogic
            val resultMessages =
                db.collection(CHAT_MESSAGES).document(currentUser?.userId!!).collection(MESSAGES)
                    .document(receiverUser?.userId!!).collection("msgTest").get().await()

            //ShreeToDny
            /*val resultMessages =
                db.collection(CHAT_MESSAGES).document("CD5MEI0XCARs3RAuTCAFmOvOnEH3")
                    .collection(MESSAGES)
                    .document("dMsqKxsaytXQ0C8do9TmYDLuqQ43").collection("msgTest").get().await()*/

            //DnyToShree
            /*val resultMessages =
                db.collection(CHAT_MESSAGES).document("dMsqKxsaytXQ0C8do9TmYDLuqQ43")
                    .collection(MESSAGES)
                    .document("CD5MEI0XCARs3RAuTCAFmOvOnEH3").collection("msgTest").get().await()*/


            /*_chatMessages.value = resultMessages.map { document ->
                document.toObject(ChatState::class.java)
            }.sortedBy { it.timeStamp }*/

            _chatMessages.emit(resultMessages.map { document ->
                document.toObject(ChatState::class.java)
            }.sortedBy { it.timeStamp }.asReversed())

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

                _chatState.value = _chatState.value.copy(
                    timeStamp = Calendar.getInstance().timeInMillis,
                    senderId = currentUser?.userId!!,
                    receiverId = receiverUser?.userId!!
                )

                db.collection(CHAT_LIST_NODE).document(currentUser?.userId!!)
                    .collection(CHAT_LIST_USER_NODE)
                    .document(receiverUser?.userId!!)
                    .set(receiverUser!!)

                db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!).get()
                    .addOnSuccessListener {
                        db.collection(CHAT_LIST_NODE).document(receiverUser?.userId!!)
                            .collection(CHAT_LIST_USER_NODE).document(currentUser?.userId!!)
                            .set(currentUser!!)
                    }

                db.collection(CHAT_MESSAGES).document(currentUser?.userId!!)
                    .collection(MESSAGES)
                    .document(receiverUser?.userId!!)
                    .collection("msgTest")
                    .add(_chatState.value)/*.addOnCompleteListener {
                        if (it.isSuccessful) {
                            getUserChats()
                        }
                    }*/

                db.collection(CHAT_MESSAGES).document(receiverUser?.userId!!)
                    .collection(MESSAGES)
                    .document(currentUser?.userId!!)
                    .collection("msgTest")
                    .add(_chatState.value)
            }
        }
    }
}


