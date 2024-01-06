package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.toImmutableList
import com.michael.template.core.base.util.toSetBy
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.util.isWithinDaysFromToday
import com.michael.template.util.toReadable
import java.time.LocalDateTime

@Entity(tableName = "distinct_contact")
@Keep
data class DistinctContactModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: LocalDateTime,
)

fun List<DistinctContactModel>.toUiModel(daysFromToday: Long): ImmutableList<ContactUiModel> =
    filter { it.dateAdded.isWithinDaysFromToday(daysFromToday) }
        .toSetBy { it.phones.first() }
        .map { it.toUiModel() }
        .sortedBy { it.dateAdded }
        .reversed()
        .toImmutableList()

fun DistinctContactModel.toUiModel(): ContactUiModel = ContactUiModel(
    id = id,
    name = name,
    phones = phones,
    dateAdded = dateAdded,
    readableDateAdded = dateAdded.toReadable(),
)
