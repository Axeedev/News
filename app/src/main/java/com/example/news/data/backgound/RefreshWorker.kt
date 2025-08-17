package com.example.news.data.backgound

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.news.domain.usecases.GetSettingsUseCase
import com.example.news.domain.usecases.UpdateArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first


@HiltWorker
class RefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val updateArticlesUseCase: UpdateArticlesUseCase,
    private val notificationsHelper: NotificationsHelper,
    private val getSettingsUseCase: GetSettingsUseCase
    ): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        Log.d("RefreshWorker", "started work")
        val settings = getSettingsUseCase().first()
        val topics = updateArticlesUseCase()
        Log.d("RefreshWorker", "finished work")
        if (topics.isNotEmpty() && settings.showNotifications){
            notificationsHelper.showNotification(topics)
        }
        return Result.success()
    }
}