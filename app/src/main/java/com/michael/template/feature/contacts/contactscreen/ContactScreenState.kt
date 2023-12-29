package com.michael.template.feature.contacts.contactscreen

import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.emptyImmutableList

data class ContactScreenState(
    val loading: Boolean,
    val updatedContacts: ImmutableList<ContactUiModel>,
) {
    companion object {
        val initialState = ContactScreenState(loading = false, updatedContacts = emptyImmutableList())
    }
}
