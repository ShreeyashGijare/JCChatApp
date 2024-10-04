package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ProfileEvents
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ProfileViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ProfileScreen(
    navController: NavController,
    profileVieModel: ProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current


    var imgUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            it?.let {
                imgUri = it
            }
            imgUri?.let { profileImageUri ->
                val outputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && profileImageUri.toString() != "") {
                    val src = ImageDecoder.createSource(context.contentResolver, profileImageUri)
                    val bitmap = ImageDecoder.decodeBitmap(src)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                }
                profileVieModel.onProfileEvents(ProfileEvents.UploadProfileImage(profileImageUri))
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Transparent)
                .clickable {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.BottomEnd
        ) {

            Image(
                painter =
                if (imgUri != null
                ) rememberImagePainter(data = imgUri) else painterResource(
                    id = R.drawable.ic_profile
                ), contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(6.dp),
                tint = Color.White
            )
        }
    }
}