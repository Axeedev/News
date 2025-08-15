@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.news.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.news.domain.entity.Article
import com.example.news.domain.usecases.AddSubscriptionUseCase
import com.example.news.domain.usecases.CleanAllArticlesUseCase
import com.example.news.domain.usecases.GetAllSubscriptionsUseCase
import com.example.news.domain.usecases.GetArticlesByTopicsUseCase
import com.example.news.domain.usecases.RemoveSubscriptionUseCase
import com.example.news.domain.usecases.UpdateArticlesForTopicUseCase
import com.example.news.domain.usecases.UpdateArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val cleanAllArticlesUseCase: CleanAllArticlesUseCase,
    private val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    private val getArticlesByTopicsUseCase: GetArticlesByTopicsUseCase,
    private val removeSubscriptionUseCase: RemoveSubscriptionUseCase,
    private val updateArticlesForTopicUseCase: UpdateArticlesForTopicUseCase,
    private val updateArticlesUseCase: UpdateArticlesUseCase,

): ViewModel(){

    private val _state : MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val state
        get() = _state.asStateFlow()


    init {
        observeSubscriptions()
        observeSelectedSubscriptions()

    }

    private fun observeSubscriptions(){
        getAllSubscriptionsUseCase()
            .onEach { subscriptions ->
                _state.update {prev->
                    val updatedTopics = subscriptions.associateWith {
                        prev.subscriptions[it] ?: true
                    }
                    prev.copy(subscriptions = updatedTopics)
                }

            }
            .launchIn(viewModelScope)
    }
    private fun observeSelectedSubscriptions(){
        _state.map {
            it.selectedSubscriptions
        }
            .distinctUntilChanged()
            .flatMapLatest {
                getArticlesByTopicsUseCase(it)
            }.onEach { list ->
                _state.update {
                    it.copy(articles = list)
                }
            }.launchIn(viewModelScope)

    }
    fun processCommand(command: SubscriptionsCommand){
        when(command){
            SubscriptionsCommand.ClickSubscribe -> {
                _state.update {
                    viewModelScope.launch {
                        val topic = _state.value.query
                        addSubscriptionUseCase(topic)
                    }
                    it.copy(query = "")
                }

            }
            is SubscriptionsCommand.InputTitle -> {
                _state.update {
                    it.copy(query = command.query)
                }

            }
            SubscriptionsCommand.Refresh -> {
                viewModelScope.launch {
                    updateArticlesUseCase()
                }
            }
            is SubscriptionsCommand.RemoveSubscription -> {
                viewModelScope.launch {
                    removeSubscriptionUseCase.invoke(command.subscription)
                }
            }
            SubscriptionsCommand.RemoveSubscriptions -> {
                viewModelScope.launch {
                    val toggledSubscriptions = state.value.selectedSubscriptions
                    cleanAllArticlesUseCase(toggledSubscriptions)
                }
            }
            is SubscriptionsCommand.ToggleSubscription -> {

                _state.update { prev ->
                    val subscriptions = prev.subscriptions.toMutableMap()
                    val previous = subscriptions[command.subscription] ?: false

                    subscriptions[command.subscription] = !previous
                    prev.copy(subscriptions = subscriptions)

                }
            }
        }
    }
}

sealed interface SubscriptionsCommand{

    data class InputTitle(val query: String) : SubscriptionsCommand

    data object Refresh : SubscriptionsCommand

    data class ToggleSubscription(val subscription: String) : SubscriptionsCommand

    data object RemoveSubscriptions : SubscriptionsCommand

    data object ClickSubscribe : SubscriptionsCommand

    data class RemoveSubscription(val subscription: String): SubscriptionsCommand
}

data class ScreenState(
    val query: String = "",
    val subscriptions: Map<String, Boolean> = mapOf(),
    val articles: List<Article> = listOf()
){
    val isSaveButtonEnabled: Boolean
        get() = query.isNotBlank()

    val selectedSubscriptions
        get() = subscriptions.filter { it.value }.map { it.key }
}