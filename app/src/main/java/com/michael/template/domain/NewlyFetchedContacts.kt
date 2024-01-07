package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "newly_fetched")
@Keep
data class NewlyFetchedContacts(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: ZonedDateTime? = null,
)

fun NewlyFetchedContacts.toOldContactModel(): OldContactModel = OldContactModel(
    id = id,
    name = name,
    phones = phones,
)
fun NewlyFetchedContacts.toUniqueContactModel(): DistinctContactModel = DistinctContactModel(
    id = id,
    name = name,
    phones = phones,
    dateAdded = ZonedDateTime.now(ZoneId.systemDefault()),
)
