package com.example.news.presentation.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Period
import com.example.news.domain.entity.asString
import com.example.news.domain.entity.toPeriod

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.processCommand(SettingsCommand.UpdateNotificationsEnabled(it))
        }
    )
    val state = viewModel.state.collectAsState()
    val currState = state.value
    if (currState is SettingsScreenState.Loaded) {
        state
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    text = "Settings"
                ) { onBackPressed.invoke() }
            },

            ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LanguageCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    defaultLanguage = currState.language
                ) {
                    Log.d("SettingsScreen", it)
                    viewModel.processCommand(SettingsCommand.UpdateLanguage((Language.valueOf(it.uppercase()))))

                    Log.d("SettingsScreen", it)
                }

                PeriodCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    defaultPeriod = currState.period
                ) {
                    viewModel.processCommand(SettingsCommand.UpdatePeriod(it.toPeriod()))
                }

                NotificationsEnableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    defaultSwitched = currState.notificationsEnabled
                ) {
                    if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }else viewModel.processCommand(SettingsCommand.UpdateNotificationsEnabled(it))

                }


                WifiOnlyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    defaultSwitched = currState.wifiOnly
                ) { viewModel.processCommand(SettingsCommand.UpdateWifiOnly(it)) }


            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = text
            )
        },
        navigationIcon = {
            Icon(
                modifier = modifier
                    .clickable { onBackPressed.invoke() }
                    .clip(CircleShape),
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "back to news"
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageCard(
    modifier: Modifier,
    defaultLanguage: Language,
    onLanguageSelected: (String) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedLanguage by remember { mutableStateOf(defaultLanguage.asString) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select language",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Select language for news search",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    shape = RoundedCornerShape(16),
                    value = selectedLanguage,
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Language.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.asString
                                )
                            },
                            {
                                onLanguageSelected.invoke(it.asString)
                                selectedLanguage = it.asString
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodCard(
    modifier: Modifier,
    defaultPeriod: Period,
    onPeriodSelected: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White
        )
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedPeriod by remember { mutableStateOf(defaultPeriod.asString()) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(
                text = "Update interval",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "How ofter to update interval",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(

                    colors = TextFieldDefaults.colors(

                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),

                    readOnly = true,
                    value = selectedPeriod,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Period.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.asString()
                                )
                            },
                            {
                                onPeriodSelected(it.minutes)
                                selectedPeriod = it.asString()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiOnlyCard(
    modifier: Modifier,
    defaultSwitched: Boolean,
    onSwitchedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        var isSwitched by remember { mutableStateOf(defaultSwitched) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Update only via Wi-Fi",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Save mobile data",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Switch(
                checked = isSwitched,
                onCheckedChange = {
                    isSwitched = it
                    onSwitchedChange(it)
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsEnableCard(
    modifier: Modifier,
    defaultSwitched: Boolean,
    onSwitchedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        var isSwitched by remember { mutableStateOf(defaultSwitched) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Show notifications about new articles",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Switch(
                checked = isSwitched,
                onCheckedChange = {
                    isSwitched = it
                    onSwitchedChange(it)
                }
            )
        }


    }
}


