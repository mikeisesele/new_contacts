package com.michael.template.feature.contacts.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michael.template.domain.ContactModel
import com.michael.template.domain.OldContactModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(list: List<OldContactModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewContacts(list: List<ContactModel>)

    @Query("Select * FROM old_contacts ORDER BY old_contacts.id DESC")
    suspend fun getAllContacts(): List<OldContactModel>

    @Query("Select * FROM new_contact")
    fun getLatestContacts(): Flow<List<ContactModel>>

    @Query("DELETE FROM new_contact where new_contact.id in (:list)")
    suspend fun deleteContacts(list: List<Long>): Int

    @Query("DELETE FROM new_contact")
    suspend fun deleteAllContacts()

    @Query("DELETE FROM old_contacts")
    suspend fun deleteOldContacts()
}
