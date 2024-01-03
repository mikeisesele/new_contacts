package com.michael.template.feature.contacts.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michael.template.domain.DistinctContactModel
import com.michael.template.domain.NewlyFetchedContacts
import com.michael.template.domain.OldContactModel
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOldContacts(list: List<OldContactModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewlyFetchedContacts(list: List<NewlyFetchedContacts>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistinctContacts(list: List<DistinctContactModel>)

    @Query("SELECT * FROM newly_fetched WHERE id NOT IN (SELECT id FROM old_contacts)")
    fun getNewlyFetchedContacts(): Flow<List<NewlyFetchedContacts>>

    @Query("SELECT * FROM distinct_contact")
    fun getDistinctContacts(): Flow<List<DistinctContactModel>>

    @Query("SELECT * FROM old_contacts")
    fun getOldContacts(): Flow<List<OldContactModel>>

    @Query("DELETE FROM distinct_contact")
    suspend fun deleteUniqueContacts()

    @Query("DELETE FROM old_contacts")
    suspend fun deleteOldContacts()

    @Query("DELETE FROM newly_fetched")
    suspend fun deleteNewlyFetchedContacts()

    @Query("SELECT * FROM distinct_contact WHERE name LIKE '%' || :query || '%' COLLATE NOCASE")
    suspend fun getMatchingContacts(query: String): List<DistinctContactModel>

    @Query("DELETE FROM distinct_contact where distinct_contact.id in (:list)")
    suspend fun deleteDistinctContactsAfterSetDays(list: List<Long>): Int
}
