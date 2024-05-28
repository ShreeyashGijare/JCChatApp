package com.example.jetpackcomposechatapp.ui.mainContent.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun ContactsScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val contentResolver = context.contentResolver
    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var hasPermission by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            contacts = removeDuplicateContacts(fetchContacts(context))
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
                hasPermission = true
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            contacts = removeDuplicateContacts(fetchContacts(context))
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        contacts.forEach { contact ->
            BasicText(text = "Name: ${contact.name}, Phone: ${contact.phoneNumber}")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun fetchContacts(context: Context): List<Contact> {
    val contactList = mutableListOf<Contact>()
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            contactList.add(Contact(name, number))
        }
    }
    return contactList
}

data class Contact(val name: String, val phoneNumber: String)

fun removeDuplicateContacts(contacts: List<Contact>): List<Contact> {
    val seenNames = mutableSetOf<String>()
    val uniqueContacts = contacts.toMutableList()
    uniqueContacts.removeAll { (name, _) ->
        if (seenNames.contains(name)) {
            true
        } else {
            seenNames.add(name)
            false
        }
    }
    return uniqueContacts
}