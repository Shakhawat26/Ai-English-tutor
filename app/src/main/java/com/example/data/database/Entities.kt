package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val learnerName: String = "Shakhawat",
    val streakDays: Int = 5,
    val lastActiveDate: String = "",
    val totalSpeakingSeconds: Long = 2520, // 42 minutes default
    val totalWordsLearned: Int = 128,
    val totalXp: Int = 850,
    val currentLevel: String = "BEGINNER",
    val preferredPersonality: String = "FRIENDLY",
    val dailyGoalMinutes: Int = 10,
    val instantCorrectionEnabled: Boolean = true,
    val preferredLanguage: String = "BENGALI",
    val voiceSpeechRate: Float = 0.95f,
    val voicePitch: Float = 1.0f,
    val voiceAccent: String = "US",
    val isDarkMode: Boolean = true
)

@Entity(tableName = "saved_vocab")
data class SavedVocabEntity(
    @PrimaryKey val wordId: String,
    val englishWord: String,
    val bengaliMeaning: String,
    val isLearned: Boolean,
    val bookmarkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_history")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: String,
    val sender: String, // "user" or "robot"
    val messageText: String,
    val bengaliText: String?,
    val timestamp: Long = System.currentTimeMillis()
)
