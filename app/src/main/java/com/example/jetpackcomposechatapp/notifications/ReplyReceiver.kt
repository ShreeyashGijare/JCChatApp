package com.example.jetpackcomposechatapp.notifications

import android.app.IntentService
import android.app.RemoteInput
import android.content.Intent
import android.util.Log

class ReplyReceiver : IntentService("ReplyReceiver") {

    override fun onHandleIntent(intent: Intent?) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyMessage = remoteInput?.getCharSequence(NotificationService().replyActionKey)
        if (replyMessage != null) {
            Log.d("ReplyReceiver", "User replied: $replyMessage")
        }
    }
}