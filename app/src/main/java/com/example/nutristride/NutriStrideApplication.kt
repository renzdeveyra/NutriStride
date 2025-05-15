package com.example.nutristride

import androidx.multidex.MultiDexApplication
import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.sync.SyncManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NutriStrideApplication : MultiDexApplication() {

    @Inject
    lateinit var syncManager: SyncManager

    @Inject
    lateinit var authManager: FirebaseAuthManager

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Schedule background sync if user is logged in
        if (authManager.isUserLoggedIn) {
            syncManager.schedulePeriodicalSync()
        }
    }
}
