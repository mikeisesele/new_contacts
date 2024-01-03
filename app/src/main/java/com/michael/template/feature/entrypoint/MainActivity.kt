package com.michael.template.feature.entrypoint

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.michael.template.core.base.contract.ViewEvent
import com.michael.template.core.base.model.Ignored
import com.michael.template.core.base.util.DialogHandler
import com.michael.template.core.ui.extensions.collectAsEffect
import com.michael.template.core.ui.extensions.rememberStateWithLifecycle
import com.michael.template.core.ui.theme.TemplateTheme
import com.michael.template.feature.contacts.contactscreen.ContactScreenViewModel
import com.michael.template.feature.contacts.contactscreen.components.ContactScreen
import com.michael.template.feature.contacts.contactscreen.components.DefaultOptionsComponent
import com.michael.template.feature.contacts.contactscreen.contracts.ContactScreenViewAction.SelectDefaultDuration
import com.michael.template.feature.contacts.contactscreen.contracts.ContactsSideEffects
import com.michael.template.feature.contacts.domain.model.MONTHS
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val contactScreenViewModel: ContactScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactScreenViewModel.resetSync()
        setContent {
            val state by rememberStateWithLifecycle(contactScreenViewModel.state)
            val coroutineScope = rememberCoroutineScope()
            val dialogHandler = remember { DialogHandler(coroutineScope) }
            val dialogConfig by dialogHandler.dialogConfig

            subscribeToSideEffects(
                contactScreenViewModel::events,
                dialogHandler = dialogHandler,
                onDurationSelected = {
                    contactScreenViewModel.onViewAction(SelectDefaultDuration(it))
                },
            )

            TemplateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ContactScreen(
                        contacts = state.updatedContacts,
                        queriedContacts = state.queriedContacts,
                        isLoading = state.loading,
                        contactSynced = state.contactsSyncFinished,
                        searchQuery = state.searchQuery,
                        dialogConfig = dialogConfig,
                        deleteOldContacts = { contactScreenViewModel.deleteOldContacts() },
                        persistingDays = state.persistingDays,
                        onQueryChanged = { contactScreenViewModel.searchContacts(it) },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scheduleContactsWorkManager()
    }

    private fun scheduleContactsWorkManager() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            contactScreenViewModel.handlePersistenceDefaults()
        } else {
            PermissionX.init(this)
                .permissions(Manifest.permission.READ_CONTACTS)
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        "Contact permission is required io use this app",
                        "OK",
                        "Cancel",
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        contactScreenViewModel.handlePersistenceDefaults()
                    }
                }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun subscribeToSideEffects(
    events: () -> Flow<ViewEvent>,
    dialogHandler: DialogHandler,
    onDurationSelected: (MONTHS) -> Unit,
) {
    events().collectAsEffect { viewEvent ->
        when (viewEvent) {
            is ViewEvent.Effect -> {
                when (viewEvent.effect) {
                    ContactsSideEffects.DisplayDefaultOptions -> {
                        displayDefaultOptions(dialogHandler, onDurationSelected)
                    }
                }
            }
            else -> Ignored
        }
    }
}

private fun displayDefaultOptions(dialogHandler: DialogHandler, onDurationSelected: (MONTHS) -> Unit) {
    dialogHandler.show {
        DefaultOptionsComponent(
            onDurationSelected = {
                onDurationSelected(it)
                dialogHandler.dismiss()
            },
        )
    }
}
