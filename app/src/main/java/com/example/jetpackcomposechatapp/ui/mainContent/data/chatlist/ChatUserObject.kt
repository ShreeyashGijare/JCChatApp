package com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist

import android.os.Parcelable
import com.example.jetpackcomposechatapp.ui.mainContent.data.chat.MessageType
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class ChatUserObject(
    val imageUrl: String? = "",
    val name: String? = "",
    val number: String? = "",
    val userId: String? = "",
    val emailId: String? = "",
    val timeStamp: Long = Calendar.getInstance().timeInMillis,
    val lastMessage: String = "",
    val lastMessageType: MessageType = MessageType.MESSAGE
): Parcelable