package com.example.jetpackcomposechatapp.data.userData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val imageUrl: String? = "",
    val name: String? = "",
    val number: String? = "",
    val userId: String? = "",
    val emailId: String? = "",
    val token: String? = ""
) : Parcelable {

    fun toMap() = mapOf(
        "imageUrl" to imageUrl,
        "name" to name,
        "number" to number,
        "userId" to userId,
        "emailId" to emailId,
        "token" to token
    )
}
