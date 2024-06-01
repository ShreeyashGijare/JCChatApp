package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

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


    fun getAllAvailableUsers() {

        /*var userData = UserData()
        db.collection(USER_NODE).get().await().map {
            val result = it.toObject(UserData::class.java)
            userData = result
            Log.i("USDUISABFIUDF", userData.toString())
        }*/


        db.collection(USER_NODE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.i("USDUISABFIUDF", error.message.toString())
                }
                if (value != null) {
                    val userList = value.toObjects<UserData>()
                    Log.i("USDUISABFIUDF", userList.toString())

                }
            }

    }


}

data class Contact(val name: String, val phoneNumber: String)