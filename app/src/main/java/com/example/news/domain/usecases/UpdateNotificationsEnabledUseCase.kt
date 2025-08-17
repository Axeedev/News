package com.example.news.domain.usecases

import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository

) {

    suspend operator fun invoke(enabled: Boolean){
        repository.updateNotifications(enabled)
    }
}