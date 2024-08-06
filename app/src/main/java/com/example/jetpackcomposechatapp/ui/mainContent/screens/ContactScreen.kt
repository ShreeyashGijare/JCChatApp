package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposechatapp.R
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.ui.mainContent.viewModel.ContactsViewModel
import com.example.jetpackcomposechatapp.uiComponents.BodyMediumComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelLargeComponent
import com.example.jetpackcomposechatapp.uiComponents.LabelSmallComponent
import com.example.jetpackcomposechatapp.utils.HomeRouteScreen
import com.google.gson.Gson

@Composable
fun ContactsScreen(
    homeNavController: NavController,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val availableContacts by viewModel.availableUsersToChat

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.getUserListToAddNewChat(context)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
                viewModel.getUserListToAddNewChat(context)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
    Column {
        TopBar(
            viewModel = viewModel,
            onBackArrowClick = {
                homeNavController.popBackStack()
            },
            onSearchButtonClick = {

            },
            onRefreshClick = {
                viewModel.getUserListToAddNewChat(context)
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(15.dp)
        ) {
            items(availableContacts/*.sortedBy { it.name }*/) { user ->
                Log.i("uidbfbfibudsf_sdsadsa___", user.name!!)
                AvailableUsersItem(user = user) { userData ->
                    homeNavController.navigate(
                        "${HomeRouteScreen.ChatScreen.route}?userData=${
                            Gson().toJson(
                                userData
                            )
                        }"
                    ) {
                        homeNavController.popBackStack(HomeRouteScreen.ContactsScreen.route, true)
                    }
                }
            }
        }
    }
}


@Composable
fun TopBar(
    viewModel: ContactsViewModel,
    onBackArrowClick: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_arrow),
                modifier = Modifier
                    .weight(.2f)
                    .clickable {
                        onBackArrowClick()
                    }
            )
            Spacer(modifier = Modifier.weight(.1f))
            Column(modifier = Modifier.weight(2f)) {
                BodyMediumComponent(
                    textValue = stringResource(id = R.string.select_contact),
                    color = MaterialTheme.colorScheme.onBackground
                )
                LabelSmallComponent(
                    textValue = stringResource(
                        id = R.string.total_contacts,
                        viewModel.availableUsersToChat.value.size
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_button),
                modifier = Modifier
                    .weight(.2f)
                    .clickable {
                        onSearchButtonClick()
                    }
            )
            Spacer(modifier = Modifier.weight(.1f))
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.refresh_contacts),
                modifier = Modifier
                    .weight(.2f)
                    .clickable {
                        onRefreshClick()
                    }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = stringResource(R.string.contacts_available))
    }
}

@Composable
fun AvailableUsersItem(
    user: UserData,
    onClick: (UserData) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(user)
            }
            .padding(vertical = 5.dp)

    ) {
        Image(
            painter = if (!user.imageUrl.isNullOrEmpty()) rememberImagePainter(data = user.imageUrl) else painterResource(
                id = R.drawable.chat_icon_one
            ), contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(
                    CircleShape
                )
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    RoundedCornerShape(50)
                )
        )
        Column(
            modifier = Modifier.padding(start = 15.dp)
        ) {
            LabelLargeComponent(
                textValue = user.name!!,
                color = MaterialTheme.colorScheme.onBackground
            )
            LabelSmallComponent(
                textValue = user.number!!,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

