package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "new_contact")
@Keep
data class ContactModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: LocalDateTime,
)
