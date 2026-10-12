package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.ChatMessage
import com.example.data.model.DifficultyLevel
import com.example.data.model.RobotPersonality
import com.example.data.model.RobotState
import com.example.data.model.Topic
import com.example.data.repository.CurriculumData
import com.example.ui.components.AnimatedRobotAvatar
import com.example.ui.components.AudioVisualizerBar
import com.example.ui.components.CorrectionCard
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.RobotAccentPink
import com.example.ui.theme.RobotCyanPrimary
import com.example.ui.theme.RobotPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.RoboSpeakViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    viewModel: RoboSpeakViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var inputMessageText by remember { mutableStateOf("") }
    var showTopicDialog by remember { mutableStateOf(false) }
    var showMicPermissionDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Permission launcher for RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            showMicPermissionDialog = true
        }
    }

    // Auto-scroll on new message
    LaunchedEffect(uiState.chatMessages.size) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    if (showMicPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showMicPermissionDialog = false },
            title = { Text("Microphone Permission Required") },
            text = { Text("RoboSpeak AI needs access to your microphone so you can practice speaking English with your AI robot tutor.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showMicPermissionDialog = false
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                ) {
                    Text("Grant Permission")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMicPermissionDialog = false }) {
                    Text("Type instead")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.selectedTopic.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${uiState.selectedTopic.bengaliTitle} • ${uiState.selectedPersonality.title}",
                            fontSize = 11.sp,
                            color = RobotCyanPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { showTopicDialog = true }) {
                        Text(
                            text = "Topics",
                            color = RobotCyanPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            ConversationBottomBar(
                inputText = inputMessageText,
                onInputChange = { inputMessageText = it },
                onSend = {
                    if (inputMessageText.isNotBlank()) {
                        viewModel.sendMessage(inputMessageText)
                        inputMessageText = ""
                    }
                },
                isMicListening = uiState.isMicListening,
                onMicClick = {
                    if (uiState.isMicListening) {
                        viewModel.stopListening()
                    } else {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasPermission) {
                            viewModel.startListening()
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                },
                isTtsSpeaking = uiState.isTtsSpeaking,
                onStopAudio = { viewModel.stopSpeaking() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .testTag("conversation_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header: Compact Robot avatar + Topic & Level status bar
            Card(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Color(0xFF1E293B),
                        RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedRobotAvatar(
                            robotState = uiState.robotState,
                            personality = uiState.selectedPersonality,
                            size = 72.dp,
                            showStatusBadge = false
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.selectedPersonality.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RobotCyanPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (uiState.isTtsSpeaking) {
                                    AudioVisualizerBar(
                                        isActive = true,
                                        activeColor = RobotCyanPrimary
                                    )
                                } else if (uiState.isMicListening) {
                                    AudioVisualizerBar(
                                        isActive = true,
                                        activeColor = SuccessEmerald
                                    )
                                }
                            }

                            Text(
                                text = when (uiState.robotState) {
                                    RobotState.IDLE -> "Tap mic to speak • কথা বলতে মাইকে চাপুন"
                                    RobotState.LISTENING -> "Listening to you... • কথা বলুন"
                                    RobotState.THINKING -> "Thinking reply... • ভাবছি"
                                    RobotState.SPEAKING -> "Speaking aloud... • কথা বলছি"
                                    RobotState.CELEBRATING -> "Great job! • দারুণ হয়েছে!"
                                    RobotState.EXPLAINING -> "Explaining grammar • ব্যাখ্যা দিচ্ছি"
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Level selector chip
                    DifficultyDropdown(
                        currentDifficulty = uiState.currentLevel,
                        onSelectDifficulty = { viewModel.setDifficulty(it) }
                    )
                }

                // Instant Correction Toggle row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Instant Bengali Correction (তাত্ক্ষণিক সংশোধন)",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = uiState.instantCorrectionEnabled,
                        onCheckedChange = { viewModel.toggleInstantCorrection() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = RobotCyanPrimary,
                            checkedTrackColor = RobotPurple.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Error banner if any
            if (uiState.errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ErrorRose.copy(alpha = 0.2f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = ErrorRose,
                        fontSize = 12.sp
                    )
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.chatMessages, key = { it.id }) { message ->
                    ChatMessageBubble(
                        message = message,
                        isSpeaking = uiState.currentlySpeakingId == message.id,
                        onRepeatAudio = {
                            viewModel.speakText(message.id, message.text)
                        }
                    )
                }
            }
        }
    }

    // Topic selector dialog
    if (showTopicDialog) {
        AlertDialog(
            onDismissRequest = { showTopicDialog = false },
            title = {
                Text(
                    text = "Select Conversation Topic • বিষয় বাছুন",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CurriculumData.TOPICS) { topic ->
                        val isSelected = topic.id == uiState.selectedTopic.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) RobotPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (isSelected) RobotCyanPrimary else Color(0xFF1E293B),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.setTopic(topic)
                                    showTopicDialog = false
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = topic.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) RobotCyanPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = topic.bengaliTitle,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTopicDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun DifficultyDropdown(
    currentDifficulty: DifficultyLevel,
    onSelectDifficulty: (DifficultyLevel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilterChip(
            selected = true,
            onClick = { expanded = true },
            label = {
                Text(
                    text = currentDifficulty.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF1E293B),
                selectedLabelColor = RobotCyanPrimary
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DifficultyLevel.values().forEach { level ->
                DropdownMenuItem(
                    text = {
                        Text("${level.label} (${level.bengaliLabel})")
                    },
                    onClick = {
                        onSelectDifficulty(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    isSpeaking: Boolean,
    onRepeatAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBengaliTranslation by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(if (message.isUser) "user_message_bubble" else "robot_message_bubble"),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        if (!message.isUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(RobotCyanPrimary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "RoboSpeak",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RobotCyanPrimary
                )
            }
        }

        // Bubble card
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 2.dp,
                bottomEnd = if (message.isUser) 2.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) RobotPurpleDark else Color(0xFF161F34)
            ),
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .border(
                    width = 1.dp,
                    color = if (message.isUser) RobotPurple.copy(alpha = 0.5f) else Color(0xFF263353),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 2.dp,
                        bottomEnd = if (message.isUser) 2.dp else 16.dp
                    )
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color.White
                )

                // Bengali Translation Box (if toggled)
                if (showBengaliTranslation && !message.bengaliTranslation.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "বাংলা: ${message.bengaliTranslation}",
                            fontSize = 13.sp,
                            color = Color(0xFF93C5FD),
                            lineHeight = 18.sp
                        )
                    }
                }

                // Bubble action buttons for robot messages
                if (!message.isUser) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Bengali translation toggle
                        if (!message.bengaliTranslation.isNullOrBlank()) {
                            TextButton(
                                onClick = { showBengaliTranslation = !showBengaliTranslation },
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = RobotCyanPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (showBengaliTranslation) "Hide Bengali" else "বাংলা অর্থ",
                                    fontSize = 12.sp,
                                    color = RobotCyanPrimary
                                )
                            }
                        }

                        // Audio speak / repeat button
                        IconButton(
                            onClick = onRepeatAudio,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = "Play Audio",
                                tint = if (isSpeaking) WarningAmber else RobotCyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Show correction card if robot detected a user mistake
        if (!message.isUser && message.correction != null) {
            Spacer(modifier = Modifier.height(8.dp))
            CorrectionCard(
                correction = message.correction,
                modifier = Modifier.fillMaxWidth(0.92f)
            )
        }
    }
}

private val RobotPurpleDark = Color(0xFF5B21B6)

@Composable
fun ConversationBottomBar(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    isMicListening: Boolean,
    onMicClick: () -> Unit,
    isTtsSpeaking: Boolean,
    onStopAudio: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color(0xFF1E293B),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Audio stop control if robot is currently speaking
            if (isTtsSpeaking) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onStopAudio,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarningAmber.copy(alpha = 0.2f),
                            contentColor = WarningAmber
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop voice",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Stop Robot Voice", fontSize = 12.sp)
                    }
                }
            }

            // Input Row: TextField + Mic Button + Send Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    placeholder = {
                        Text(
                            text = if (isMicListening) "Listening to your voice..." else "Type or tap mic to speak...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_textfield"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RobotCyanPrimary,
                        unfocusedBorderColor = Color(0xFF263353),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    maxLines = 3
                )

                // Large Microphone Button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isMicListening) SuccessEmerald else RobotCyanPrimary
                        )
                        .clickable { onMicClick() }
                        .testTag("mic_record_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isMicListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Microphone",
                        tint = Color(0xFF0A0E1A),
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Send Button (active when text typed)
                if (inputText.isNotBlank()) {
                    IconButton(
                        onClick = onSend,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(RobotPurple)
                            .testTag("send_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
