package com.example.jetpackcomposechatapp.data.userData

data class UserData(
    val imageUrl: String? = "",
    val name: String? = "",
    val number: String? = "",
    val userId: String? = ""
    ) {

    fun toMap() = mapOf(
        "imageUrl" to imageUrl,
        "name" to name,
        "number" to number,
        "userId" to userId
    )
}
