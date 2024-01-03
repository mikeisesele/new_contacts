package com.michael.template.feature.contacts.contactscreen.contracts

import com.michael.template.feature.contacts.domain.model.MONTHS

sealed interface ContactScreenViewAction {
    data class SelectDefaultDuration(val duration: MONTHS) : ContactScreenViewAction
}
