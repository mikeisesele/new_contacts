package com.michael.template.feature.contacts.domain

import com.michael.template.domain.DistinctContactModel
import com.michael.template.domain.NewlyFetchedContacts
import com.michael.template.domain.OldContactModel
import com.michael.template.feature.contacts.local.ContactDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactDAO: ContactDAO) {
    fun getNewlyFetchedContacts(): Flow<List<NewlyFetchedContacts>> = contactDAO.getNewlyFetchedContacts()
    fun getDistinctContacts() = contactDAO.getDistinctContacts()
    fun getOldContacts() = contactDAO.getOldContacts()
    suspend fun deleteOldContacts() = contactDAO.deleteOldContacts()
    suspend fun deleteDistinctContactsAfterSetDays(oldIDs: List<Long>) {
        contactDAO.deleteDistinctContactsAfterSetDays(oldIDs)
    }

    suspend fun deleteNewlyFetchedContacts() = contactDAO.deleteNewlyFetchedContacts()

    suspend fun insertDistinctContacts(newContacts: List<DistinctContactModel>) {
        contactDAO.insertDistinctContacts(newContacts)
    }

    suspend fun insertPhoneContacts(oldContacts: List<OldContactModel>) {
        contactDAO.insertOldContacts(oldContacts)
    }

    suspend fun insertNewlyFetchedContacts(newContacts: List<NewlyFetchedContacts>) {
        contactDAO.insertNewlyFetchedContacts(newContacts)
    }
    suspend fun getMatchingContacts(query: String) = contactDAO.getMatchingContacts(query)
}
