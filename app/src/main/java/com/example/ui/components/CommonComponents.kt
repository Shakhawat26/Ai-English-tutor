package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CorrectionResult
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.RobotAccentPink
import com.example.ui.theme.RobotCyanPrimary
import com.example.ui.theme.RobotPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.WarningAmber

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = RobotCyanPrimary,
    shapeRadius: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickableModifier = if (onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    Box(
        modifier = clickableModifier
            .clip(RoundedCornerShape(shapeRadius))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        glowColor.copy(alpha = 0.5f),
                        RobotPurple.copy(alpha = 0.3f),
                        glowColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(shapeRadius)
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}

@Composable
fun BengaliBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = RobotPurple.copy(alpha = 0.15f),
    textColor: Color = RobotPurple
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun AudioVisualizerBar(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 7,
    activeColor: Color = SuccessEmerald
) {
    val transition = rememberInfiniteTransition(label = "audio_bars")
    val heights = (0 until barCount).map { index ->
        transition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(250 + (index * 70), easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar_$index"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { animHeight ->
            val scale = if (isActive) animHeight.value else 0.2f
            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height((24 * scale).dp.coerceAtLeast(4.dp))
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else Color.Gray.copy(alpha = 0.4f))
            )
        }
    }
}

@Composable
fun CorrectionCard(
    correction: CorrectionResult,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF161E31)
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, WarningAmber.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("correction_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Correction",
                        tint = WarningAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Correct My English • সঠিক রূপ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = WarningAmber
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF94A3B8)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    // Original vs Corrected
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "What you said:",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = correction.originalText,
                                fontSize = 13.sp,
                                color = ErrorRose,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Correct English:",
                                fontSize = 11.sp,
                                color = SuccessEmerald
                            )
                            Text(
                                text = correction.correctedText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SuccessEmerald
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bengali Explanation Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E293B))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.TipsAndUpdates,
                                contentDescription = "Bengali Explanation",
                                tint = RobotCyanPrimary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "বাংলা ব্যাখ্যা (Bengali Rule):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RobotCyanPrimary
                                )
                                Text(
                                    text = correction.bengaliExplanation,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE2E8F0),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Natural way if present
                    if (correction.naturalAlternative.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "More natural: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "\"${correction.naturalAlternative}\"",
                                fontSize = 12.sp,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }

                    // Pronunciation tip if present
                    if (!correction.pronunciationTip.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "Pronunciation Tip",
                                tint = RobotAccentPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = correction.pronunciationTip,
                                fontSize = 12.sp,
                                color = RobotAccentPink
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApiKeyStatusIndicator(
    isLiveAi: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isLiveAi) SuccessEmerald.copy(alpha = 0.15f)
                else WarningAmber.copy(alpha = 0.15f)
            )
            .border(
                1.dp,
                if (isLiveAi) SuccessEmerald.copy(alpha = 0.4f)
                else WarningAmber.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isLiveAi) SuccessEmerald else WarningAmber)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isLiveAi) "Live Gemini AI Active" else "Demo & Offline Tutor Mode",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isLiveAi) SuccessEmerald else WarningAmber
            )
        }
    }
}
