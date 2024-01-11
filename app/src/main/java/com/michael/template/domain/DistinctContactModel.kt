package com.michael.template.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.toImmutableList
import com.michael.template.core.base.util.normalize
import com.michael.template.core.base.util.toSetBy
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.feature.contacts.domain.model.NestedListContentType
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

fun List<DistinctContactModel>.toUiModel(daysFromToday: Long): ImmutableList<NestedListContentType> =
    filter { it.dateAdded.isWithinDaysFromToday(daysFromToday) }
        .toSetBy { it.phones.first().normalize() }
        .map { it.toUiModel() }
        .sortContacts { it.dateAdded }

fun <T : Comparable<T>> List<ContactUiModel>.sortContacts(sortBy: (ContactUiModel) -> T):
    ImmutableList<NestedListContentType> = sortedByDescending(sortBy)
    .groupContactsByTime()
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

fun List<ContactUiModel>.groupContactsByTime(): ImmutableList<NestedListContentType> {
    val now = ZonedDateTime.now()

    return groupBy {
        when {
            it.dateAdded.isAfter(now.minusDays(1)) -> "Latest"
            it.dateAdded.isAfter(now.minusWeeks(1)) -> "Earlier This Week"
            it.dateAdded.isAfter(now.minusWeeks(TWO)) -> "Last Week"
            it.dateAdded.isAfter(now.minusMonths(1)) -> "Earlier This Month"
            it.dateAdded.isAfter(now.minusMonths(TWO)) -> "Last Month"
            it.dateAdded.isAfter(now.minusMonths(THREE)) -> "2 Months Ago"
            it.dateAdded.isAfter(now.minusMonths(FOUR)) -> "3 Months Ago"
            else -> "Older"
        }
    }.map { (timeFrame, contacts) ->
        NestedListItem(
            timeFrame = timeFrame,
            contacts = contacts.toImmutableList(),
        )
    }.filter { it.contacts.isNotEmpty() }
        .toImmutableList()
        .toNestedList()
}

internal fun ImmutableList<NestedListItem>.toNestedList() = flatMap {
    it.toNestedListContentTypes()
}.toImmutableList()

internal fun NestedListItem.toNestedListContentTypes(): List<NestedListContentType> =
    buildList {
        add(NestedListContentType.Header(timeFrame))
        contacts.forEach { contact ->
            add(contact)
        }
    }
