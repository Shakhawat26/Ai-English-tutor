package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RobotPersonality
import com.example.data.model.RobotState
import com.example.ui.theme.ErrorRose
import com.example.ui.theme.RobotAccentPink
import com.example.ui.theme.RobotCyanPrimary
import com.example.ui.theme.RobotPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.XpGold
import kotlin.math.sin

@Composable
fun AnimatedRobotAvatar(
    robotState: RobotState,
    personality: RobotPersonality = RobotPersonality.FRIENDLY,
    size: Dp = 140.dp,
    showStatusBadge: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "robot_anim")

    // Gentle floating offset
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    // Pulsing aura glow
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Speaking mouth animation
    val mouthScale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(240, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mouth"
    )

    // Eye blinking
    val blinkProgress by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )

    // Rotation for thinking
    val thinkingSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    val (glowColor, statusText, statusBengali) = when (robotState) {
        RobotState.IDLE -> Triple(RobotCyanPrimary, "Ready to Speak", "কথা বলতে প্রস্তুত")
        RobotState.LISTENING -> Triple(SuccessEmerald, "Listening to you...", "আপনার কথা শুনছি...")
        RobotState.THINKING -> Triple(RobotPurple, "Thinking...", "রোবট চিন্তা করছে...")
        RobotState.SPEAKING -> Triple(RobotCyanPrimary, "Speaking...", "রোবট কথা বলছে...")
        RobotState.CELEBRATING -> Triple(XpGold, "Excellent Job! 🎉", "অসাধারণ হয়েছে! 🎉")
        RobotState.EXPLAINING -> Triple(WarningAmber, "Explaining...", "ব্যাখ্যা দিচ্ছি...")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.testTag("animated_robot_avatar")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .offset(y = floatOffset.dp)
        ) {
            // Neon Glow halo ring
            Box(
                modifier = Modifier
                    .size(size * 0.95f)
                    .scale(auraScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.45f),
                                glowColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Robot Canvas Avatar
            Canvas(
                modifier = Modifier.size(size * 0.85f)
            ) {
                val w = this.size.width
                val h = this.size.height

                // Antenna
                val antennaTop = Offset(w * 0.5f, h * 0.08f)
                drawLine(
                    color = Color(0xFF64748B),
                    start = Offset(w * 0.5f, h * 0.22f),
                    end = antennaTop,
                    strokeWidth = w * 0.04f
                )
                // Antenna light bulb
                drawCircle(
                    color = glowColor,
                    radius = w * 0.06f,
                    center = antennaTop
                )

                // Head outer casing (High-tech white metallic with rounded curve)
                val headBounds = Size(w * 0.82f, h * 0.65f)
                val headTopLeft = Offset(w * 0.09f, h * 0.22f)

                // Outer shadow/border
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE2E8F0))
                    ),
                    topLeft = headTopLeft,
                    size = headBounds,
                    cornerRadius = CornerRadius(w * 0.28f, w * 0.28f)
                )

                // Head casing neon border outline
                drawRoundRect(
                    brush = Brush.sweepGradient(
                        colors = listOf(RobotCyanPrimary, RobotPurple, RobotCyanPrimary)
                    ),
                    topLeft = headTopLeft,
                    size = headBounds,
                    cornerRadius = CornerRadius(w * 0.28f, w * 0.28f),
                    style = Stroke(width = w * 0.025f)
                )

                // Ear headphones (left & right)
                val earRadius = w * 0.11f
                // Left ear
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = earRadius,
                    center = Offset(w * 0.09f, h * 0.52f)
                )
                drawCircle(
                    color = glowColor,
                    radius = earRadius * 0.5f,
                    center = Offset(w * 0.09f, h * 0.52f)
                )

                // Right ear
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = earRadius,
                    center = Offset(w * 0.91f, h * 0.52f)
                )
                drawCircle(
                    color = glowColor,
                    radius = earRadius * 0.5f,
                    center = Offset(w * 0.91f, h * 0.52f)
                )

                // Visor / Screen Face (Dark sleek glass screen)
                val visorTopLeft = Offset(w * 0.17f, h * 0.32f)
                val visorSize = Size(w * 0.66f, h * 0.45f)
                drawRoundRect(
                    color = Color(0xFF0A0F1D),
                    topLeft = visorTopLeft,
                    size = visorSize,
                    cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
                )

                // Draw Eyes on the screen
                val eyeWidth = w * 0.15f
                val eyeLeftCenter = Offset(w * 0.36f, h * 0.49f)
                val eyeRightCenter = Offset(w * 0.64f, h * 0.49f)

                when (robotState) {
                    RobotState.CELEBRATING -> {
                        // Star/arc happy eyes
                        val leftEyePath = Path().apply {
                            moveTo(eyeLeftCenter.x - eyeWidth * 0.4f, eyeLeftCenter.y + eyeWidth * 0.2f)
                            quadraticTo(
                                eyeLeftCenter.x,
                                eyeLeftCenter.y - eyeWidth * 0.5f,
                                eyeLeftCenter.x + eyeWidth * 0.4f,
                                eyeLeftCenter.y + eyeWidth * 0.2f
                            )
                        }
                        val rightEyePath = Path().apply {
                            moveTo(eyeRightCenter.x - eyeWidth * 0.4f, eyeRightCenter.y + eyeWidth * 0.2f)
                            quadraticTo(
                                eyeRightCenter.x,
                                eyeRightCenter.y - eyeWidth * 0.5f,
                                eyeRightCenter.x + eyeWidth * 0.4f,
                                eyeRightCenter.y + eyeWidth * 0.2f
                            )
                        }
                        drawPath(leftEyePath, XpGold, style = Stroke(width = w * 0.04f))
                        drawPath(rightEyePath, XpGold, style = Stroke(width = w * 0.04f))
                    }
                    RobotState.THINKING -> {
                        // Cyan glowing circular dots with radar pulse
                        drawCircle(color = RobotPurple, radius = eyeWidth * 0.35f, center = eyeLeftCenter)
                        drawCircle(color = RobotPurple, radius = eyeWidth * 0.35f, center = eyeRightCenter)
                        drawCircle(
                            color = RobotCyanPrimary,
                            radius = eyeWidth * 0.45f,
                            center = eyeLeftCenter,
                            style = Stroke(width = 3f)
                        )
                        drawCircle(
                            color = RobotCyanPrimary,
                            radius = eyeWidth * 0.45f,
                            center = eyeRightCenter,
                            style = Stroke(width = 3f)
                        )
                    }
                    else -> {
                        // Digital glowing eyes with subtle blink
                        val eyeHeight = (eyeWidth * 0.7f) * if (robotState == RobotState.IDLE) blinkProgress else 1.0f
                        drawRoundRect(
                            color = glowColor,
                            topLeft = Offset(eyeLeftCenter.x - eyeWidth * 0.4f, eyeLeftCenter.y - eyeHeight * 0.5f),
                            size = Size(eyeWidth * 0.8f, eyeHeight),
                            cornerRadius = CornerRadius(eyeWidth * 0.35f, eyeWidth * 0.35f)
                        )
                        drawRoundRect(
                            color = glowColor,
                            topLeft = Offset(eyeRightCenter.x - eyeWidth * 0.4f, eyeRightCenter.y - eyeHeight * 0.5f),
                            size = Size(eyeWidth * 0.8f, eyeHeight),
                            cornerRadius = CornerRadius(eyeWidth * 0.35f, eyeWidth * 0.35f)
                        )
                    }
                }

                // Draw Mouth
                val mouthCenter = Offset(w * 0.5f, h * 0.65f)
                when (robotState) {
                    RobotState.SPEAKING -> {
                        // Expanding digital speech mouth
                        val currentMouthH = (w * 0.08f) * mouthScale
                        drawRoundRect(
                            color = glowColor,
                            topLeft = Offset(mouthCenter.x - w * 0.16f, mouthCenter.y - currentMouthH * 0.5f),
                            size = Size(w * 0.32f, currentMouthH),
                            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
                        )
                    }
                    RobotState.CELEBRATING -> {
                        // Big smile arc
                        val smilePath = Path().apply {
                            moveTo(mouthCenter.x - w * 0.16f, mouthCenter.y - w * 0.02f)
                            quadraticTo(
                                mouthCenter.x,
                                mouthCenter.y + w * 0.10f,
                                mouthCenter.x + w * 0.16f,
                                mouthCenter.y - w * 0.02f
                            )
                        }
                        drawPath(smilePath, color = glowColor, style = Stroke(width = w * 0.035f))
                    }
                    RobotState.EXPLAINING -> {
                        // Thoughtful small o-shape
                        drawCircle(
                            color = WarningAmber,
                            radius = w * 0.045f,
                            center = mouthCenter
                        )
                    }
                    else -> {
                        // Gentle friendly smile
                        val smilePath = Path().apply {
                            moveTo(mouthCenter.x - w * 0.12f, mouthCenter.y - w * 0.01f)
                            quadraticTo(
                                mouthCenter.x,
                                mouthCenter.y + w * 0.05f,
                                mouthCenter.x + w * 0.12f,
                                mouthCenter.y - w * 0.01f
                            )
                        }
                        drawPath(smilePath, color = glowColor, style = Stroke(width = w * 0.03f))
                    }
                }
            }
        }

        if (showStatusBadge) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.85f))
                    .border(1.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = statusText,
                        color = glowColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = statusBengali,
                        color = Color(0xFFCBD5E1),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
