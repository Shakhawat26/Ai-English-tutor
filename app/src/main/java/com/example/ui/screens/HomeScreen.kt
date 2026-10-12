package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RobotPersonality
import com.example.data.model.RobotState
import com.example.data.repository.CurriculumData
import com.example.ui.components.ApiKeyStatusIndicator
import com.example.ui.components.AnimatedRobotAvatar
import com.example.ui.components.GlowCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.RobotAccentPink
import com.example.ui.theme.RobotCyanPrimary
import com.example.ui.theme.RobotPurple
import com.example.ui.theme.StreakOrange
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.XpGold
import com.example.ui.viewmodel.RoboSpeakViewModel

@Composable
fun HomeScreen(
    viewModel: RoboSpeakViewModel,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val wordOfTheDay = CurriculumData.VOCABULARY_LIST.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Top Bar with Greeting & Quick toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${uiState.learnerName}! 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Ready to speak English? • প্রস্তুত তো?",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.toggleDarkMode() },
                        modifier = Modifier.testTag("dark_mode_toggle")
                    ) {
                        Icon(
                            imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = RobotCyanPrimary
                        )
                    }
                    IconButton(
                        onClick = { onNavigate(Screen.Settings) },
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        // Live AI Status Indicator
        item {
            ApiKeyStatusIndicator(
                isLiveAi = uiState.isLiveAiConnected,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Center Hero: Animated Robot Character
        item {
            GlowCard(
                glowColor = RobotCyanPrimary,
                shapeRadius = 24.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedRobotAvatar(
                        robotState = uiState.robotState,
                        personality = uiState.selectedPersonality,
                        size = 140.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${uiState.selectedPersonality.title} (${uiState.selectedPersonality.bengaliTitle})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RobotCyanPrimary
                    )
                    Text(
                        text = uiState.selectedPersonality.subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary "Start Speaking" Action Button
                    Button(
                        onClick = { onNavigate(Screen.Conversation) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RobotCyanPrimary,
                            contentColor = Color(0xFF0A0E1A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_speaking_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Start Speaking with Robot • কথা বলুন",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Personality Switcher Chips
        item {
            Column {
                Text(
                    text = "Select Robot Personality • শিক্ষকের ধরন",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(RobotPersonality.values()) { personality ->
                        val isSelected = uiState.selectedPersonality == personality
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setPersonality(personality) },
                            label = {
                                Text(
                                    text = personality.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RobotPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Gamification Dashboard Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Progress • অগ্রগতি",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(RobotCyanPrimary.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${uiState.currentLevel.label} (${uiState.currentLevel.bengaliLabel})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RobotCyanPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            icon = "🔥",
                            title = "Streak",
                            value = "${uiState.streakDays} Days",
                            color = StreakOrange
                        )
                        StatItem(
                            icon = "⏱️",
                            title = "Speaking",
                            value = "${uiState.speakingMinutes} mins",
                            color = RobotCyanPrimary
                        )
                        StatItem(
                            icon = "📖",
                            title = "Words",
                            value = "${uiState.wordsLearned}",
                            color = SuccessEmerald
                        )
                        StatItem(
                            icon = "⚡",
                            title = "Total XP",
                            value = "${uiState.totalXp}",
                            color = XpGold
                        )
                    }
                }
            }
        }

        // Daily Challenge Teaser Card
        item {
            GlowCard(
                glowColor = WarningAmber,
                shapeRadius = 18.dp,
                onClick = { onNavigate(Screen.DailyChallenge) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(WarningAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Daily Challenge • দৈনিক চ্যালেঞ্জ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = if (uiState.dailyChallengeCompleted) "Completed today! +50 XP earned" else "Morning Routine Challenge (+50 XP)",
                                fontSize = 12.sp,
                                color = if (uiState.dailyChallengeCompleted) SuccessEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open challenge",
                        tint = WarningAmber
                    )
                }
            }
        }

        // Learning Hub 2x2 Grid
        item {
            Text(
                text = "Interactive Learning Modules • অনুশীলন বিভাগ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubActionCard(
                        title = "Vocabulary Trainer",
                        bengali = "শব্দভাণ্ডার ও ফ্ল্যাশকার্ড",
                        icon = Icons.Default.AutoAwesome,
                        color = RobotPurple,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Vocabulary) }
                    )
                    HubActionCard(
                        title = "Grammar Practice",
                        bengali = "ব্যাকরণ কুইজ ও সংশোধন",
                        icon = Icons.Default.Spellcheck,
                        color = RobotCyanPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Grammar) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubActionCard(
                        title = "Pronunciation",
                        bengali = "সঠিক উচ্চারণ ট্রেইনার",
                        icon = Icons.Default.RecordVoiceOver,
                        color = RobotAccentPink,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Pronunciation) }
                    )
                    HubActionCard(
                        title = "Daily Practice",
                        bengali = "১০ মিনিটের সেশন",
                        icon = Icons.Default.School,
                        color = SuccessEmerald,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Conversation) }
                    )
                }
            }
        }

        // Word of the Day Preview Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Word of the Day • আজকের শব্দ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = RobotPurple
                        )
                        IconButton(
                            onClick = {
                                viewModel.speakText("word_day", wordOfTheDay.englishWord)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen word",
                                tint = RobotCyanPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${wordOfTheDay.englishWord}  ${wordOfTheDay.phonetic}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "বাংলা অর্থ: ${wordOfTheDay.bengaliMeaning}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessEmerald
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${wordOfTheDay.exampleSentenceEn}\"",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatItem(
    icon: String,
    title: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
fun HubActionCard(
    title: String,
    bengali: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = bengali,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
