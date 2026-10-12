package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Conversation : Screen("conversation", "AI Conversation")
    object Pronunciation : Screen("pronunciation", "Pronunciation")
    object Vocabulary : Screen("vocabulary", "Vocabulary")
    object Grammar : Screen("grammar", "Grammar")
    object DailyChallenge : Screen("daily_challenge", "Daily Challenge")
    object Settings : Screen("settings", "Settings")
}
