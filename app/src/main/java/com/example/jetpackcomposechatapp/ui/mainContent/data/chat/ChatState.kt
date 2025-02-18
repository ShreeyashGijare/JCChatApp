package com.example.jetpackcomposechatapp.ui.mainContent.data.chat

data class ChatState(
    var message: String? = "",
    var timeStamp: Long = 0L,
    var senderId: String? = null,
    var receiverId: String? = null,
    var messageType: MessageType = MessageType.MESSAGE
)


enum class MessageType{
    MESSAGE,
    IMAGE
}