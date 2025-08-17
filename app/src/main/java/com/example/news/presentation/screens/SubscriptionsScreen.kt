package com.example.news.presentation.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.news.domain.entity.Article
import com.example.news.presentation.ui.theme.CustomIcons
import com.example.news.utils.DateFormatter.toDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionsViewModel = viewModel(),
    onNavigateToSettings: () -> Unit,
) {


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier,
                onRefreshDataClick = { viewModel.processCommand(SubscriptionsCommand.Refresh) },
                onDeleteDataClick = {
                    viewModel.processCommand(SubscriptionsCommand.RemoveSubscriptions)
                },
                onSettingsClick = onNavigateToSettings
            )
        },
    ) {
        val state by viewModel.state.collectAsState()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Subscriptions(
                    subscriptions = state.subscriptions,
                    query = state.query,
                    isButtonEnabled = state.isSaveButtonEnabled,
                    onQueryChange = {
                        viewModel.processCommand(SubscriptionsCommand.InputTitle(it))
                    },
                    onTopicClick = {
                        viewModel.processCommand(SubscriptionsCommand.ToggleSubscription(it))
                    },
                    onDeleteSubscription = {
                        viewModel.processCommand(SubscriptionsCommand.RemoveSubscription(it))
                    },
                    onSubscribeButtonClick = {
                        viewModel.processCommand(SubscriptionsCommand.ClickSubscribe)
                    }
                )
            }
            if (state.articles.isNotEmpty()) {
                item {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.DarkGray
                    )
                }

                item {
                    val articlesCount = state.articles.size
                    Text(
                        text = "Subscriptions ($articlesCount):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                items(
                    items = state.articles,
                    key = {
                        it.url
                    }
                ) { article ->
                    ArticleCard(
                        article = article
                    )
                }
            } else if (state.subscriptions.isNotEmpty()) {
                item {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.DarkGray
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "No articles for selected subscriptions"
                    )
                }
            }
        }
    }
}


@Composable
fun ButtonSubscribe(
    modifier: Modifier = Modifier,
    isButtonEnabled: Boolean,
    onClick: () -> Unit,
) {

    Button(
        enabled = isButtonEnabled,
        shape = RoundedCornerShape(25),
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add subscription"
        )
        Text(
            text = "Subscribe"
        )
    }
}

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        article.imageUrl?.let {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                model = it,
                contentDescription = "image of the article",
                contentScale = ContentScale.FillWidth
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = article.title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        if (article.description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = article.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = article.source,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = article.publishedAt.toDateFormat(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val context = LocalContext.current
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = CustomIcons.OpenInNew,
                    contentDescription = "open in"
                )
                Spacer(Modifier.size(8.dp))
                Text("Read")
            }
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Look at the article I just found:\n${article.title}\n\n${article.url}")
                    }
                    context.startActivity(intent)
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "share"
                )
                Spacer(Modifier.size(8.dp))
                Text("Share")
            }


        }

        Spacer(Modifier.size(16.dp))

    }
}


@Composable
private fun SubscriptionsChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    topic: String,
    onClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = {
            onClick(topic)
        },
        label = {
            Text(topic)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onDeleteClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = "clear"
            )
        }
    )

}

@Composable
private fun Subscriptions(
    modifier: Modifier = Modifier,
    subscriptions: Map<String, Boolean>,
    query: String,
    isButtonEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit,
    onSubscribeButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChange,
            label = {
                Text(
                    text = "What interests you?"
                )
            },
            singleLine = true
        )
        Spacer(
            Modifier.height(8.dp)
        )
        ButtonSubscribe(
            modifier = Modifier.fillMaxWidth(),
            isButtonEnabled = isButtonEnabled
        ) {
            onSubscribeButtonClick()
        }
        if (subscriptions.isNotEmpty()) {
            Text(
                text = "Subscriptions (${subscriptions.keys.size}):",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subscriptions.forEach { (subscription, isSelected) ->
                    item {
                        SubscriptionsChip(
                            isSelected = isSelected,
                            topic = subscription,
                            onClick = onTopicClick
                        ) { onDeleteSubscription.invoke(subscription) }
                    }

                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 16.dp),
                text = "No subscriptions yet...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onRefreshDataClick: () -> Unit,
    onDeleteDataClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "News",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(end = 8.dp)
                    .clickable { onRefreshDataClick.invoke() },
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh subscriptions"
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(end = 8.dp)
                    .clickable {
                        onDeleteDataClick.invoke()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = "Clear subscriptions"
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onSettingsClick.invoke()
                    },
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )


        }
    )
}

