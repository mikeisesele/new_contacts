package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "old_contacts")
@Keep
data class OldContactModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: ZonedDateTime? = null,
)
