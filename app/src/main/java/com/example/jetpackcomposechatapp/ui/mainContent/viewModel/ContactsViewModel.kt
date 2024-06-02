package com.example.jetpackcomposechatapp.ui.mainContent.viewModel

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposechatapp.data.userData.UserData
import com.example.jetpackcomposechatapp.utils.Constants.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private var uniqueContacts: List<Contact> = ArrayList()
    var availableUsersToChat: MutableList<UserData> = ArrayList()


    fun getUserListToAddNewChat(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            removeDuplicateContacts(fetchContacts(context))
            getAllAvailableUsers()
        }
    }

    private fun fetchContacts(context: Context): List<Contact> {
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

    private fun removeDuplicateContacts(contacts: List<Contact>) {
        val seenNames = mutableSetOf<String>()
        uniqueContacts = contacts.toMutableList()
        (uniqueContacts as MutableList<Contact>).removeAll { (name, _) ->
            if (seenNames.contains(name)) {
                true
            } else {
                seenNames.add(name)
                false
            }
        }
        uniqueContacts = uniqueContacts.map { contact ->
            contact.copy(
                phoneNumber = contact.phoneNumber.replace("+91", "").replace(" ", "")
                    .replace("-", "")
            )
        }
    }

    private fun getAllAvailableUsers() {
        viewModelScope.launch {
            db.collection(USER_NODE)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                    }
                    if (value != null) {
                        val availableUserList = value.toObjects<UserData>()
                        val uniqueAvailableUsersList = availableUserList.map { it.number }.toSet()
                        val uniqueContactList = uniqueContacts.map { it.phoneNumber }.toSet()
                        val mergedListAvailableUsersAndContacts = uniqueAvailableUsersList.intersect(uniqueContactList)
                        availableUsersToChat =
                            availableUserList.filter { it.number in mergedListAvailableUsersAndContacts }.toMutableList()
                        Log.i("uidbfbfibudsf", availableUsersToChat.toString())
                    }
                }
        }
    }
}

data class Contact(val name: String, val phoneNumber: String)