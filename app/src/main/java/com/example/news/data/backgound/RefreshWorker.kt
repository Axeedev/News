package com.example.news.data.backgound

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.news.domain.usecases.UpdateArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class RefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val updateArticlesUseCase: UpdateArticlesUseCase
    ): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        Log.d("RefreshWorker", "started work")
        updateArticlesUseCase()
        Log.d("RefreshWorker", "finished work")
        return Result.success()
    }
}