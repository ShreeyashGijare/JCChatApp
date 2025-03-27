package com.example.jetpackcomposechatapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object CameraUtils {

    fun getUriFromBitmap(bitmap: Bitmap, context: Context): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "", null)
        return Uri.parse(path)
    }
}