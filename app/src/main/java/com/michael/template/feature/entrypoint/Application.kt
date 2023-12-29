package com.michael.template.feature.entrypoint

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.michael.template.core.data.SharedPref
import com.michael.template.util.Constants
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class Application : Application(), LifecycleObserver {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate() {
        super.onCreate()
//        Timber.plant(Timber.DebugTree())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        // App in background
        CoroutineScope(Dispatchers.Main).launch {
            sharedPref.saveToSharedPref(Constants.IS_APP_ACTIVE, false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // App in foreground
        CoroutineScope(Dispatchers.Main).launch {
            sharedPref.saveToSharedPref(Constants.IS_APP_ACTIVE, true)
        }
    }
}
