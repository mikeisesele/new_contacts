package com.michael.template.feature.entrypoint

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.michael.template.core.ui.extensions.rememberStateWithLifecycle
import com.michael.template.core.ui.theme.TemplateTheme
import com.michael.template.feature.contacts.contactscreen.ContactScreen
import com.michael.template.feature.contacts.contactscreen.ContactScreenViewModel
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val contactScreenViewModel: ContactScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by rememberStateWithLifecycle(contactScreenViewModel.state)

            TemplateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ContactScreen(
                        contacts = state.updatedContacts,
                        isLoading = state.loading,
                        clearDB = { contactScreenViewModel.clearDB() },
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
            contactScreenViewModel.getLatestContacts()
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
                        contactScreenViewModel.getLatestContacts()
                    }
                }
        }
    }
}
