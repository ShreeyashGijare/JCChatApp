package com.example.jetpackcomposechatapp.ui.mainContent.data.chat

sealed class ChatEvents {

    data class Message(val message: String) : ChatEvents()

    object SendButtonClick : ChatEvents()

}