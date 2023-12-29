package com.michael.template.feature.contacts.local

import com.michael.template.domain.ContactModel
import com.michael.template.domain.OldContactModel
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactDAO: ContactDAO) {
    fun getNewContacts() = contactDAO.getLatestContacts()
    suspend fun clearDB() {
        contactDAO.deleteAllContacts()
        contactDAO.deleteOldContacts()
    }

    suspend fun insertNewContacts(newContacts: List<ContactModel>) {
        contactDAO.insertNewContacts(newContacts)
    }

    suspend fun insertOldContacts(oldContacts: List<OldContactModel>) {
        contactDAO.insertContacts(oldContacts)
    }

    suspend fun getAllContacts() = contactDAO.getAllContacts()
}
