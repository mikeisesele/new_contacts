package com.michael.template.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michael.template.domain.DistinctContactModel
import com.michael.template.domain.NewlyFetchedContacts
import com.michael.template.domain.OldContactModel
import com.michael.template.feature.contacts.local.ContactDAO
import com.michael.template.feature.contacts.local.ContactPhoneConverter

const val DB_NAME = "latest_contacts"

@Database(
    entities = [DistinctContactModel::class, OldContactModel::class, NewlyFetchedContacts::class],
    version = 4,
    exportSchema = false,
)
@TypeConverters(ContactPhoneConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDAO(): ContactDAO

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
