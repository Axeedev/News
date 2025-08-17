package com.example.news.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Period
import com.example.news.domain.entity.Settings
import com.example.news.domain.usecases.GetSettingsUseCase
import com.example.news.domain.usecases.UpdateLanguageUseCase
import com.example.news.domain.usecases.UpdateNotificationsEnabledUseCase
import com.example.news.domain.usecases.UpdatePeriodUseCase
import com.example.news.domain.usecases.UpdateWifiOnlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updatePeriodUseCase: UpdatePeriodUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
): ViewModel() {



    private val _state : MutableStateFlow<SettingsScreenState> = MutableStateFlow(
        SettingsScreenState.Initial
    )
    val state
        get() = _state.asStateFlow()

    init {
        getSettingsUseCase()
            .onEach { settings ->
                _state.update {
                    SettingsScreenState.Loaded(
                        language = settings.language,
                        period = settings.period,
                        wifiOnly = settings.wifiOnly,
                        notificationsEnabled = settings.showNotifications
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.UpdateLanguage -> {
                    _state.update {
                        if (it is SettingsScreenState.Loaded){

                            updateLanguageUseCase(command.language)
                            it.copy(language = command.language)
                        }else it
                    }
                }

                is SettingsCommand.UpdateNotificationsEnabled -> {
                    _state.update {
                        if (it is SettingsScreenState.Loaded){


                            updateNotificationsEnabledUseCase(enabled = command.enabled)
                            it.copy(notificationsEnabled = !it.notificationsEnabled)
                        } else it
                    }
                }

                is SettingsCommand.UpdatePeriod -> {
                    _state.update {
                        if (it is SettingsScreenState.Loaded){

                            updatePeriodUseCase(command.period)
                            it.copy(period = command.period)
                        }else it
                    }
                }

                is SettingsCommand.UpdateWifiOnly -> {
                    _state.update {
                        if (it is SettingsScreenState.Loaded){

                            updateWifiOnlyUseCase(command.wifiOnly)
                            it.copy(wifiOnly = !it.wifiOnly)
                        } else it
                    }

                }
            }
        }
    }
}

sealed interface SettingsScreenState{

    data object Initial : SettingsScreenState

    data class Loaded(
        val language: Language = Settings.defaultEnglish,
        val period: Period = Settings.defaultPeriod,
        val wifiOnly: Boolean = Settings.DEFAULT_WIFI_ONLY,
        val notificationsEnabled: Boolean = Settings.DEFAULT_NOTIFICATIONS
    ) : SettingsScreenState

}

sealed interface SettingsCommand{

    data class UpdateLanguage(val language: Language) : SettingsCommand

    data class UpdatePeriod(val period: Period) : SettingsCommand

    data class UpdateWifiOnly(val wifiOnly: Boolean) : SettingsCommand

    data class UpdateNotificationsEnabled(val enabled: Boolean): SettingsCommand
}