package com.michael.template.feature.contacts.contactscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.template.feature.contacts.local.ContactRepository
import com.michael.template.feature.contacts.utils.ContactSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    private val contactSyncManager: ContactSyncManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ContactScreenState.initialState)
    val state = _state.asStateFlow()

    fun getLatestContacts() {
        viewModelScope.launch {
            setLoadingState()
            contactSyncManager.syncContacts()
            updateUI()
        }
    }

    fun clearDB() {
        viewModelScope.launch {
            contactsRepository.clearDB()
        }
    }

    private suspend fun updateUI() {
        val contactFlow = contactsRepository.getNewContacts()
        contactFlow.collect { contacts ->
            _state.emit(
                state.value.copy(
                    loading = false,
                    updatedContacts = contacts.toUiModel(),
                ),
            )
        }
    }

    private suspend fun setLoadingState() {
        _state.emit(
            state.value.copy(
                loading = true,
            ),
        )
    }
}
