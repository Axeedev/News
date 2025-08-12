package com.example.news.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    viewModel: SubscriptionsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("News")
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear subscriptions"
                    )
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh subscriptions"
                    )
                }
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                SubscriptionsTextField(
                    value = state.query
                ) { value ->
                    viewModel.processCommand(SubscriptionsCommand.InputTitle(value))
                }
            }
            item {
                ButtonSubscribe { viewModel.processCommand(SubscriptionsCommand.ClickSubscribe) }
            }
            item {
                val subscriptionsCount = state.subscriptions.size
                Text(
                    text = "Subscriptions ($subscriptionsCount):"
                )
            }
            item {
                LazyRow(

                ) {
                    items(
                        items = state.subscriptions.keys.toList(),

                    ) { subscription ->
                        Text(
                            text = subscription
                        )
                    }
                }
            }
            item {
                HorizontalDivider()
            }
            item {
                val articlesCount = state.articles.size
                Text(
                    text = "Subscriptions ($articlesCount):"
                )
            }
            items(
                items = state.articles,
            ) {article ->
                Text(
                    text = article.title
                )
            }
        }
    }
}

@Composable
fun ButtonSubscribe(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = "Subscribe"
        )
    }
}


@Composable
fun SubscriptionsTextField(
    modifier: Modifier = Modifier,
    value: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onQueryChange
    )
}