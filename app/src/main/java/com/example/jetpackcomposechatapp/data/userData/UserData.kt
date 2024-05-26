package com.example.jetpackcomposechatapp.data.userData

data class UserData(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""
) {

    fun toMap() = mapOf(
        "imageUrl" to imageUrl,
        "name" to name,
        "number" to number,
        "userId" to userId
    )
}
