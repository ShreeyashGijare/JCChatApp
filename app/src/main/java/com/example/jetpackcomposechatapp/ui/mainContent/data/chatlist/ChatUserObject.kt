package com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist

import java.util.Calendar

data class ChatUserObject(
    val imageUrl: String? = "",
    val name: String? = "",
    val number: String? = "",
    val userId: String? = "",
    val emailId: String? = "",
    val timeStamp: Long = Calendar.getInstance().timeInMillis
)