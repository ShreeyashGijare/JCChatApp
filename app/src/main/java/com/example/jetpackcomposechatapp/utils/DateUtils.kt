package com.example.jetpackcomposechatapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    fun convertLongToTimeAMPM(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("hh:mm", Locale.getDefault())
        return format.format(date)
    }

    fun formatDate(date: Date): String {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        calendar.add(Calendar.DATE, -1)
        val yesterday = calendar.time

        val dayDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dayDateFormat.format(date)

        return when (formattedDate) {
            dayDateFormat.format(today) -> "Today"
            dayDateFormat.format(yesterday) -> "Yesterday"
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        }
    }

    fun formatMessageTimeStampToDate(messageTime: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = dateFormat.format(Date(messageTime))
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DATE, -1)
        val yesterday = calendar.time
        return when (dateObject) {
            dateFormat.format(today) -> convertLongToTimeAMPM(messageTime)
            dateFormat.format(yesterday) -> "Yesterday"
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(dateObject)
        }
    }
}