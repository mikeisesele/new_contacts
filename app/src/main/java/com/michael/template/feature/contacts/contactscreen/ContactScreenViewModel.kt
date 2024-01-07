package com.michael.template.feature.contacts.contactscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.template.core.base.contract.ViewEvent
import com.michael.template.core.base.model.ImmutableList
import com.michael.template.core.base.model.emptyImmutableList
import com.michael.template.core.data.PreferenceType
import com.michael.template.core.data.SharedPref
import com.michael.template.domain.toUiModel
import com.michael.template.feature.contacts.contactscreen.contracts.ContactScreenState
import com.michael.template.feature.contacts.contactscreen.contracts.ContactScreenViewAction
import com.michael.template.feature.contacts.contactscreen.contracts.ContactsSideEffects
import com.michael.template.feature.contacts.domain.ContactRepository
import com.michael.template.feature.contacts.domain.ContactSyncManager
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import com.michael.template.feature.contacts.domain.model.MONTHS
import com.michael.template.util.Constants.DEFAULT_PERSISTENCE_SET
import com.michael.template.util.Constants.ONE_MONTH
import com.michael.template.util.Constants.PERSISTENCE_KEY
import com.michael.template.util.Constants.THREE_MONTH
import com.michael.template.util.Constants.TWO_MONTHS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.utils.ifEmpty
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    private val contactSyncManager: ContactSyncManager,
    private val sharedPref: SharedPref,
) : ViewModel() {

    private val _state = MutableStateFlow(ContactScreenState.initialState)
    val state = _state.asStateFlow()

    private val eventsFlow = Channel<ViewEvent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND,
        onUndeliveredElement = {
            Log.e(this.toString(), null, IllegalStateException("Missed view event $it"))
        },
    )

    private fun dispatchViewEvent(event: ViewEvent) {
        launch {
            eventsFlow.send(event)
        }
    }
    val events
        get() = eventsFlow.receiveAsFlow()

    private var daysPersisted = loadDaysPersisted()
    private val defaultSet = sharedPref.loadFromSharedPref<Boolean>(PreferenceType.BOOLEAN, DEFAULT_PERSISTENCE_SET)

    private fun launch(block: suspend CoroutineScope.() -> Unit) {
        try {
            viewModelScope.launch(
                context = Dispatchers.IO,
                block = block,
            )
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun updateState(transform: (ContactScreenState) -> ContactScreenState) {
        _state.update(transform)
    }

    init {
        launch { clearOldContacts() }
    }

    private fun getLatestContacts() = launch {
        setLoadingState()
        contactSyncManager.syncContacts {
            contactsRepository.getDistinctContacts().collectLatest { contacts ->
                updateUI(contacts = contacts.toUiModel(daysPersisted))
            }
        }
    }

    private fun updateUI(contacts: ImmutableList<ContactUiModel>) = updateState { state ->
        state.copy(
            loading = false,
            contactsSyncFinished = true,
            updatedContacts = contacts,
            persistingDays = daysPersisted.toString(),
        )
    }

    fun deleteOldContacts() = launch { contactsRepository.deleteOldContacts() }
    fun resetSync() = updateState { state ->
        state.copy(contactsSyncFinished = false)
    }

    private fun setLoadingState() = updateState { state ->
        state.copy(loading = true)
    }

    fun searchContacts(query: String) {
        val trimmedQuery = query.trim().lowercase()
        launch {
            val queriedList = contactsRepository.getMatchingContacts("%$trimmedQuery%")
            updateState { state ->
                state.copy(
                    searchQuery = query,
                    queriedContacts = queriedList
                        .toUiModel(daysPersisted)
                        .ifEmpty { emptyImmutableList() },
                )
            }
        }
    }

    private fun clearOldContacts() = launch {
        val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
        contactsRepository.getDistinctContacts().collect {
            val oldContacts = it.filter { model ->
                model.dateAdded.isBefore(currentTime.minusDays(daysPersisted))
            }
            val oldIDs = oldContacts.map { it.id }
            contactsRepository.deleteDistinctContactsAfterSetDays(oldIDs)
        }
    }

    fun onViewAction(viewAction: ContactScreenViewAction) {
        when (viewAction) {
            is ContactScreenViewAction.SelectDefaultDuration -> {
                setDefaultOption(viewAction.duration)
            }
        }
    }
    fun handlePersistenceDefaults() {
        daysPersisted = loadDaysPersisted()
        if (!defaultSet) {
            displayDefaultOptions()
        } else {
            if (state.value.updatedContacts.isEmpty()) {
                getLatestContacts()
            }
        }
    }

    private fun displayDefaultOptions() {
        dispatchViewEvent(ViewEvent.Effect(ContactsSideEffects.DisplayDefaultOptions))
    }

    private fun setDefaultOption(duration: MONTHS) {
        when (duration) {
            MONTHS.ONE -> sharedPref.saveToSharedPref(PERSISTENCE_KEY, ONE_MONTH)
            MONTHS.TWO -> sharedPref.saveToSharedPref(PERSISTENCE_KEY, TWO_MONTHS)
            MONTHS.THREE -> sharedPref.saveToSharedPref(PERSISTENCE_KEY, THREE_MONTH)
        }
        sharedPref.saveToSharedPref(DEFAULT_PERSISTENCE_SET, true)
        daysPersisted = loadDaysPersisted()
        updateState { state ->
            state.copy(persistingDays = daysPersisted.toString())
        }
        getLatestContacts()
    }

    private fun loadDaysPersisted() = sharedPref.loadFromSharedPref<Long>(PreferenceType.LONG, PERSISTENCE_KEY)
}
