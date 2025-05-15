package com.example.jetpackcomposechatapp.ui.mainContent.data.chat

data class ChatState(
    val messageId: String = "",
    var message: String? = "",
    var timeStamp: Long = 0L,
    var senderId: String? = null,
    var receiverId: String? = null,
    var messageType: MessageType = MessageType.MESSAGE,
    var messageReactions: List<String> = arrayListOf(),
    var repliedMessageId: String? = null,
    var repliedMessage: String? = null
)


enum class MessageType {
    MESSAGE,
    IMAGE,
    REPLY
}