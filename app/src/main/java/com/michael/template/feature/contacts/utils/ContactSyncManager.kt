package com.michael.template.feature.contacts.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.michael.template.domain.OldContactModel
import com.michael.template.feature.contacts.local.ContactRepository
import com.michael.template.feature.contacts.local.toContactModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class ContactSyncManager @Inject constructor(
    private val contactRepository: ContactRepository,
    private val context: Context,
) {

    fun syncContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val phoneContacts = readContacts("${ContactsContract.Contacts._ID} desc")
                val dataBaseContacts = contactRepository.getAllContacts()
                sync(phoneContacts, dataBaseContacts)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun sync(phoneContacts: List<OldContactModel>, dataBaseContacts: List<OldContactModel>) {
        if (phoneContacts.size > dataBaseContacts.size) {
            val contactsNotInDatabase = phoneContacts.subtract(dataBaseContacts.toSet())
            val existingNewContacts = contactRepository.getNewContacts().first()
            val newMapped = contactsNotInDatabase.map {
                it.toContactModel().copy(dateAdded = LocalDateTime.now())
            }
            contactRepository.insertNewContacts(
                existingNewContacts.toMutableList().apply {
                    addAll(newMapped)
                },
            )
            contactRepository.insertOldContacts(phoneContacts)
        } else {
            "No new contact added".log(this::class.java, "47")
        }
    }

    @SuppressLint("Range", "LongMethod")
    private fun readContacts(sortOrder: String): List<OldContactModel> {
        val ids = mutableListOf<Long>()
        val names = mutableListOf<String>()
        val contactsList = mutableListOf<OldContactModel>()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            sortOrder,
        )
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)))
                names.add(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) ?: "",
                )
            }
            // construct a map for contacts phones <id, list of phones>
            val phonesMap = mutableMapOf<Long, MutableList<String>>()
            val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} IN (${ids.joinToString()})",
                null,
                null,
            )
            if (phoneCursor!!.count > 0) {
                while (phoneCursor.moveToNext()) {
                    val id = phoneCursor.getLong(
                        phoneCursor
                            .getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ),
                    )
                    val phoneNumber = phoneCursor.getString(
                        phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ),
                    )
                    val contactPhones = phonesMap[id] ?: mutableListOf()
                    contactPhones.add(phoneNumber)
                    phonesMap[id] = contactPhones
                }
            }
            phoneCursor.close()
            for (i in 0 until ids.size)
                contactsList.add(
                    OldContactModel(
                        ids[i],
                        names[i],
                        phonesMap[ids[i]] ?: listOf(),
                    ),
                )
        }
        cursor.close()
        return contactsList
    }
}
