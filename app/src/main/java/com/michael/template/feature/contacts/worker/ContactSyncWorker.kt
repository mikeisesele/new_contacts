package com.michael.template.feature.contacts.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.michael.template.feature.contacts.domain.ContactSyncManager
import com.michael.template.util.Constants.WORKER_NAME

class ContactSyncWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val contactSyncManager: ContactSyncManager,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            contactSyncManager.syncContacts()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val TAG = WORKER_NAME
    }
}
