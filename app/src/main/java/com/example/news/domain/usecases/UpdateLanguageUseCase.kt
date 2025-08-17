package com.example.news.domain.usecases

import com.example.news.domain.entity.Language
import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(language: Language){
        repository.updateLanguage(language)
    }
}