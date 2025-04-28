package com.example.jetpackcomposechatapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.jetpackcomposechatapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {


    private val notificationChannelID: String = "CIRQLE_NOTIFICATION_CHANNEL"
    private val notificationChannelName: String = "CIRQLE_NOTIFICATION"
    val replyActionKey = "key_reply"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let { notification ->
            val title = notification.title
            val body = notification.body
            val imageUrl =
                remoteMessage.notification?.imageUrl?.toString() // <-- get image URL if available
            if (imageUrl != null) {
                sendRichNotification(title, body, imageUrl)
            } else {
                sendSimpleNotification(title, body)
            }
        }
    }


    private fun sendSimpleNotification(title: String?, body: String?) {
        val builder = NotificationCompat.Builder(this, notificationChannelID)
            .setSmallIcon(R.drawable.cirqle_icon)
            .setContentTitle(title)
            .setContentText(body)
            .addAction(replyActionBuilder())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannelIfNeeded(manager)
        manager.notify(0, builder.build())
    }

    private fun sendRichNotification(title: String?, body: String?, imageUrl: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannelIfNeeded(manager)
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val bigLargeIcon =
                        BitmapFactory.decodeResource(resources, R.drawable.cirqle_icon)

                    val style = NotificationCompat.BigPictureStyle()
                        .bigPicture(resource)
                        .bigLargeIcon(bigLargeIcon)

                    val builder = NotificationCompat.Builder(
                        this@NotificationService,
                        notificationChannelID
                    )
                        .setSmallIcon(R.drawable.cirqle_icon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(style)
                        .addAction(replyActionBuilder())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                    manager.notify(1, builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle when Glide load is cleared
                }
            })
    }

    private fun createNotificationChannelIfNeeded(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelID,
                notificationChannelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
    }


    private fun replyActionBuilder(): NotificationCompat.Action {

        val remoteInput: RemoteInput = RemoteInput.Builder(replyActionKey)
            .setLabel("Reply to this message")
            .build()

        val replyPendingIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, ReplyReceiver::class.java), // This will handle the reply
            PendingIntent.FLAG_MUTABLE
        )

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_send_message,
            "Reply",
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()
        return replyAction
    }
}