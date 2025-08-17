package com.example.news

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.news.presentation.startup.AppStartupManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory




    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    @Inject
    lateinit var startupManager: AppStartupManager

    override fun onCreate() {
        super.onCreate()
        startupManager.startRefreshData()
    }
}

