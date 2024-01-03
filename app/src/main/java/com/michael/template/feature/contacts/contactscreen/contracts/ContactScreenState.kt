package com.michael.template.feature.contacts.contactscreen.contracts

import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.emptyImmutableList
import com.michael.template.feature.contacts.domain.model.ContactUiModel

data class ContactScreenState(
    val loading: Boolean,
    val updatedContacts: ImmutableList<ContactUiModel>,
    val queriedContacts: ImmutableList<ContactUiModel>,
    val persistingDays: String,
    val contactsSyncFinished: Boolean,
    val searchQuery: String,
) {
    companion object {
        val initialState = ContactScreenState(
            loading = false,
            updatedContacts = emptyImmutableList(),
            queriedContacts = emptyImmutableList(),
            contactsSyncFinished = false,
            persistingDays = "0",
            searchQuery = "",
        )
    }
}
