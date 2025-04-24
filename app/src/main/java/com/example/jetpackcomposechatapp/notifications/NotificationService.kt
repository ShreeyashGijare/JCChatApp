package com.example.jetpackcomposechatapp.notifications

import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}