package com.example.news.domain.usecases

import com.example.news.domain.entity.Period
import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdatePeriodUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(period: Period){
        repository.updatePeriod(period.minutes)
    }
}