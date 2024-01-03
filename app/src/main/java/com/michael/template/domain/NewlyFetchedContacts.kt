package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "newly_fetched")
@Keep
data class NewlyFetchedContacts(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: LocalDateTime? = null,
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
    dateAdded = LocalDateTime.now(),
)
