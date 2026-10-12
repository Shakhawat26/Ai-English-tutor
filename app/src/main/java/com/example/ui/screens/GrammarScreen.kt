package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CurriculumData
import com.example.ui.components.AnimatedRobotAvatar
import com.example.ui.components.BengaliBadge
import com.example.ui.components.GlowCard
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.RobotCyanPrimary
import com.example.ui.theme.RobotPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.XpGold
import com.example.ui.viewmodel.RoboSpeakViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarScreen(
    viewModel: RoboSpeakViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Grammar Practice",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "ব্যাকরণ অনুশীলন ও সংশোধন",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .testTag("grammar_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RobotCyanPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = RobotCyanPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Grammar Quiz (কুইজ)",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Sentence Builder (বাক্য গঠন)",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                GrammarQuizContent(viewModel = viewModel)
            } else {
                SentenceBuilderContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GrammarQuizContent(viewModel: RoboSpeakViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val question = viewModel.getCurrentGrammarQuestion()
    val totalCount = CurriculumData.GRAMMAR_QUESTIONS.size
    val currentIndex = uiState.currentGrammarIndex + 1

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question $currentIndex of $totalCount",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = RobotCyanPrimary
                )
                BengaliBadge(
                    text = "Score: ${uiState.grammarScore}",
                    backgroundColor = XpGold.copy(alpha = 0.2f),
                    textColor = XpGold
                )
            }
        }

        // Question Card
        item {
            GlowCard(
                glowColor = RobotCyanPrimary,
                shapeRadius = 20.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = question.ruleTitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RobotPurple
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.question,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Options List
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                question.options.forEachIndexed { index, optionText ->
                    val isSelected = uiState.selectedGrammarAnswer == index
                    val isCorrect = index == question.correctIndex
                    val hasAnswered = uiState.selectedGrammarAnswer != null

                    val (containerColor, borderColor, textColor) = when {
                        !hasAnswered -> Triple(
                            MaterialTheme.colorScheme.surface,
                            Color(0xFF263353),
                            MaterialTheme.colorScheme.onBackground
                        )
                        isCorrect -> Triple(
                            SuccessEmerald.copy(alpha = 0.15f),
                            SuccessEmerald,
                            SuccessEmerald
                        )
                        isSelected && !isCorrect -> Triple(
                            ErrorRose.copy(alpha = 0.15f),
                            ErrorRose,
                            ErrorRose
                        )
                        else -> Triple(
                            MaterialTheme.colorScheme.surface,
                            Color(0xFF263353),
                            MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable(enabled = !hasAnswered) {
                                viewModel.selectGrammarAnswer(index)
                            }
                            .testTag("grammar_option_$index")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(borderColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A' + index).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = textColor
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            if (hasAnswered && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = SuccessEmerald
                                )
                            } else if (hasAnswered && isSelected && !isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Wrong",
                                    tint = ErrorRose
                                )
                            }
                        }
                    }
                }
            }
        }

        // Detailed Bengali Explanation Box
        if (uiState.grammarExplanationVisible) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161E31)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, RobotCyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TipsAndUpdates,
                                contentDescription = null,
                                tint = RobotCyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "কেন এই উত্তরটি সঠিক? (Bengali Explanation)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = RobotCyanPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.bengaliExplanation,
                            fontSize = 13.sp,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "সাধারণ ভুল: ${question.commonBengaliError}",
                            fontSize = 12.sp,
                            color = WarningAmber
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.nextGrammarQuestion() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RobotPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("next_grammar_button")
                ) {
                    Text("Next Question • পরবর্তী প্রশ্ন", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderContent(viewModel: RoboSpeakViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val exercise = viewModel.getCurrentSentenceBuilder()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Sentence Builder • বাক্য তৈরি করুন",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "নিচের বাংলা বাক্যটির সঠিক ইংরেজি বাক্য তৈরি করতে শব্দগুলোতে চাপুন",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Bengali Target Sentence Box
        item {
            GlowCard(
                glowColor = RobotPurple,
                shapeRadius = 18.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "বাংলা বাক্য (Bengali Sentence):",
                        fontSize = 12.sp,
                        color = RobotCyanPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.bengaliMeaning,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Assembled Sentence Area
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF263353), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Your English Sentence (আপনার সাজানো বাক্য):",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (uiState.sentenceBuilderSelectedWords.isEmpty()) {
                        Text(
                            text = "Tap words below to arrange them here...",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B)
                        )
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            uiState.sentenceBuilderSelectedWords.forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(RobotCyanPrimary)
                                        .clickable { viewModel.tapSentenceBuilderWord(word) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = word,
                                        color = Color(0xFF0A0E1A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Available Scrambled Words
        item {
            Text(
                text = "Available Words • শব্দসমূহ:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                exercise.scrambledWords.forEach { word ->
                    val isUsed = uiState.sentenceBuilderSelectedWords.contains(word)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isUsed) Color(0xFF1E293B) else RobotPurple.copy(alpha = 0.2f))
                            .border(
                                1.dp,
                                if (isUsed) Color(0xFF334155) else RobotPurple,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable(enabled = !isUsed) {
                                viewModel.tapSentenceBuilderWord(word)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = word,
                            color = if (isUsed) Color(0xFF64748B) else Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Check Action
        item {
            Button(
                onClick = { viewModel.checkSentenceBuilder() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RobotCyanPrimary,
                    contentColor = Color(0xFF0A0E1A)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Check Sentence • যাচাই করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Feedback Result
        if (uiState.sentenceBuilderSuccess != null) {
            item {
                val isSuccess = uiState.sentenceBuilderSuccess == true
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccess) SuccessEmerald.copy(alpha = 0.15f) else ErrorRose.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = if (isSuccess) SuccessEmerald else ErrorRose
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSuccess) "Perfect! +30 XP earned 🎉" else "Not quite right. Try again!",
                                fontWeight = FontWeight.Bold,
                                color = if (isSuccess) SuccessEmerald else ErrorRose
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "সঠিক বাক্য: \"${exercise.correctSentence}\"",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "নিয়ম: ${exercise.grammarNote}",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.nextSentenceBuilder() },
                    colors = ButtonDefaults.buttonColors(containerColor = RobotPurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next Exercise • পরবর্তী বাক্য")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
