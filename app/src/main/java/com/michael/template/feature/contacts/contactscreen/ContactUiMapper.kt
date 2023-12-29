package com.michael.template.feature.contacts.contactscreen

import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.toImmutableList
import com.michael.template.domain.ContactModel
import com.michael.template.feature.contacts.utils.isWithinThirtyDaysFromToday
import com.michael.template.feature.contacts.utils.toReadable

fun List<ContactModel>.toUiModel(): ImmutableList<ContactUiModel> =
    filter { it.dateAdded.isWithinThirtyDaysFromToday() }
        .map { it.toUiModel() }
        .sortedBy { it.dateAdded }
        .reversed()
        .toImmutableList()

fun ContactModel.toUiModel(): ContactUiModel = ContactUiModel(
    id = id,
    name = name,
    phones = phones,
    dateAdded = dateAdded,
    readableDateAdded = dateAdded.toReadable(),
)
