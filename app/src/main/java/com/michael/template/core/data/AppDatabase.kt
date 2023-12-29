package com.michael.template.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michael.template.domain.ContactModel
import com.michael.template.domain.OldContactModel
import com.michael.template.feature.contacts.local.ContactDAO
import com.michael.template.feature.contacts.utils.ContactPhoneConverter

const val DB_NAME = "app_database"

@Database(
    entities = [ContactModel::class, OldContactModel::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(ContactPhoneConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDAO(): ContactDAO

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
