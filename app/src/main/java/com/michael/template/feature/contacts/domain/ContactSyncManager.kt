package com.michael.template.feature.contacts.domain

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.michael.template.domain.DistinctContactModel
import com.michael.template.domain.NewlyFetchedContacts
import com.michael.template.domain.toOldContactModel
import com.michael.template.domain.toUniqueContactModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactSyncManager @Inject constructor(
    private val contactRepository: ContactRepository,
    private val context: Context,
) {

    suspend fun syncContacts(block: (suspend () -> Unit?)? = null) {
        val phoneContacts = readContacts("${ContactsContract.Contacts._ID} desc")
        val basePhoneContacts = phoneContacts.map { it.toOldContactModel() }

        try {
            CoroutineScope(Dispatchers.IO).launch {
                contactRepository.insertNewlyFetchedContacts(phoneContacts)
                contactRepository.getOldContacts().first().ifEmpty {
                    contactRepository.insertPhoneContacts(basePhoneContacts)
                }
                contactRepository.getNewlyFetchedContacts().collectLatest { contacts ->
                    val uniqueContacts = contacts.map { it.toUniqueContactModel() }
                    updateNewContacts(uniqueContacts, block)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateNewContacts(
        contactsNotInDatabase: List<DistinctContactModel>,
        block: (suspend () -> Unit?)?,
    ) {
        contactRepository.getDistinctContacts().collectLatest { distinctContacts ->
            val formattedContacts = distinctContacts
                .toMutableList()
                .apply { addAll(contactsNotInDatabase) }
                .sortedBy { it.dateAdded }
                .reversed()

            contactRepository.insertDistinctContacts(formattedContacts)
            contactRepository.deleteNewlyFetchedContacts()
            if (block != null) {
                block()
            }
        }
    }

    @SuppressLint("Range", "LongMethod")
    private fun readContacts(sortOrder: String): List<NewlyFetchedContacts> {
        val ids = mutableListOf<Long>()
        val names = mutableListOf<String>()
        val contactsList = mutableListOf<NewlyFetchedContacts>()
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
                    NewlyFetchedContacts(
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
