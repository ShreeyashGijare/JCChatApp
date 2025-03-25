package com.example.jetpackcomposechatapp.ui.mainContent.data.chat

import java.util.Random
import java.util.UUID

data class ChatState(
    val messageId: String = UUID.randomUUID().toString().substring(0, 15),
    var message: String? = "",
    var timeStamp: Long = 0L,
    var senderId: String? = null,
    var receiverId: String? = null,
    var messageType: MessageType = MessageType.MESSAGE
)


enum class MessageType {
    MESSAGE,
    IMAGE
}