package com.michael.template.core.base.util

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.michael.template.feature.contacts.contactscreen.components.DialogConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

private const val DEFAULT_DIALOG_DURATION = 4000L

class DialogHandler(private val coroutineScope: CoroutineScope) {

    private var dialogJob: Job? = null

    private var _dialogConfig: MutableState<DialogConfig> = mutableStateOf(DialogConfig())
    val dialogConfig: State<DialogConfig> = _dialogConfig

    fun dismiss() {
        dialogJob?.cancel()
        _dialogConfig.value = _dialogConfig.value.copy(visible = false)
    }

    fun show(dialogContent: @Composable (BoxScope.() -> Unit)) {
        _dialogConfig.value = _dialogConfig.value.copy(
            dialogContent = dialogContent,
            visible = true,
        )
        // auto dismiss
//        dialogJob?.cancel()
//        dialogJob = if (_dialogConfig.value.visible) {
//            coroutineScope.launch {
//                delay(DEFAULT_DIALOG_DURATION)
//                dismiss()
//            }
//        } else {
//            null
//        }
    }
}
