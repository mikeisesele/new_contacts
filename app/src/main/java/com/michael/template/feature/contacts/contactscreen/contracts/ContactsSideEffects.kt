package com.michael.template.feature.contacts.contactscreen.contracts

import com.michael.template.core.base.contract.SideEffect

sealed class ContactsSideEffects : SideEffect {
    object DisplayDefaultOptions : ContactsSideEffects()
}
