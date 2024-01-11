package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.toImmutableList
import com.michael.template.core.base.util.normalize
import com.michael.template.core.base.util.toSetBy
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.feature.contacts.domain.model.NestedListItem
import com.michael.template.util.Constants.FOUR
import com.michael.template.util.Constants.THREE
import com.michael.template.util.Constants.TWO
import com.michael.template.util.isWithinDaysFromToday
import com.michael.template.util.toReadable
import java.time.ZonedDateTime

@Entity(tableName = "distinct_contact")
@Keep
data class DistinctContactModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val phones: List<String>,
    val dateAdded: ZonedDateTime,
)

fun List<DistinctContactModel>.toUiModel(daysFromToday: Long): ImmutableList<NestedListItem> =
    filter { it.dateAdded.isWithinDaysFromToday(daysFromToday) }
        .toSetBy { it.phones.first().normalize() }
        .map { it.toUiModel() }
        .sortedBy { it.dateAdded }
        .reversed()
        .groupContactsByTime()
        .filter { it.contacts.isNotEmpty() }
        .toImmutableList()

fun DistinctContactModel.toUiModel(): ContactUiModel = ContactUiModel(
    id = id,
    name = name,
    phones = phones,
    dateAdded = dateAdded,
    readableDateAdded = dateAdded.toReadable(),
)

fun DistinctContactModel.toOldContacts(): OldContactModel = OldContactModel(
    id = id,
    name = name,
    phones = phones,
)

fun List<ContactUiModel>.groupContactsByTime(): ImmutableList<NestedListItem> {
    val now = ZonedDateTime.now()

    return groupBy {
        when {
            it.dateAdded.isAfter(now.minusDays(1)) -> "RECENTLY"
            it.dateAdded.isAfter(now.minusWeeks(1)) -> "THIS WEEK"
            it.dateAdded.isAfter(now.minusWeeks(TWO)) -> "LAST WEEK"
            it.dateAdded.isAfter(now.minusMonths(1)) -> "EARLIER THIS MONTH"
            it.dateAdded.isAfter(now.minusMonths(TWO)) -> "LAST MONTH"
            it.dateAdded.isAfter(now.minusMonths(THREE)) -> "2 MONTHS AGO"
            it.dateAdded.isAfter(now.minusMonths(FOUR)) -> "3 MONTHS AGO"
            else -> "Older"
        }
    }.map { (timeFrame, contacts) ->
        NestedListItem(timeFrame, contacts.toImmutableList())
    }.toImmutableList()
}
