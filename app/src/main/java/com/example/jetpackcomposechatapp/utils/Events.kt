package com.example.jetpackcomposechatapp.utils

open class Events<out T>(val content: T) {
    var hasBeenHandled: Boolean = false

    fun getContentOrNull(): T? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }


}