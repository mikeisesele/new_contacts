package com.michael.template.feature.contacts.contactscreen.contracts

import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.emptyImmutableList
import com.michael.template.feature.contacts.domain.model.DisplaySort
import com.michael.template.feature.contacts.domain.model.NestedListContentType

data class ContactScreenState(
    val loading: Boolean,
    val updatedContacts: ImmutableList<NestedListContentType>,
    val queriedContacts: ImmutableList<NestedListContentType>,
    val persistingDays: String,
    val contactsSyncFinished: Boolean,
    val searchQuery: String,
    val currentDisplaySort: DisplaySort,
) {
    companion object {
        val initialState = ContactScreenState(
            loading = false,
            updatedContacts = emptyImmutableList(),
            queriedContacts = emptyImmutableList(),
            contactsSyncFinished = false,
            persistingDays = "0",
            searchQuery = "",
            currentDisplaySort = DisplaySort.DATE_ADDED,
        )
    }
}
