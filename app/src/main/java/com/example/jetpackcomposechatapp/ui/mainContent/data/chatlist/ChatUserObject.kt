package com.example.jetpackcomposechatapp.ui.mainContent.data.chatlist

import android.os.Parcelable
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
    val lastMessage: String = ""
): Parcelable