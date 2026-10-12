package com.example.data.model

enum class RobotState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    CELEBRATING,
    EXPLAINING
}

enum class RobotPersonality(
    val title: String,
    val bengaliTitle: String,
    val subtitle: String,
    val tonePrompt: String
) {
    FRIENDLY(
        title = "Friendly Robot",
        bengaliTitle = "বন্ধুত্বপূর্ণ রোবট",
        subtitle = "Patient, warm & encouraging for beginners",
        tonePrompt = "You are a warm, kind, and ultra-encouraging English tutor robot named RoboSpeak. You speak simple, clear English. You motivate the learner constantly with gentle encouragement."
    ),
    STRICT(
        title = "Strict Teacher Robot",
        bengaliTitle = "কড়া শিক্ষক রোবট",
        subtitle = "Grammar focus & high accuracy",
        tonePrompt = "You are an analytical and disciplined English teacher robot. You focus heavily on correct grammar, accurate sentence structures, and professional vocabulary."
    ),
    FUNNY(
        title = "Funny Robot",
        bengaliTitle = "মজার রোবট",
        subtitle = "Playful banter, jokes & casual chat",
        tonePrompt = "You are an entertaining, humorous, and witty robot tutor. You tell light jokes, use fun analogies, and make speaking English lively and enjoyable."
    ),
    IELTS(
        title = "IELTS Coach Robot",
        bengaliTitle = "IELTS কোচ রোবট",
        subtitle = "Band 7+ speaking strategies & feedback",
        tonePrompt = "You are an expert IELTS Speaking coach robot. You practice Part 1, 2, and 3 style questions, push for complex structures, idiomatic vocabulary, and structured responses."
    )
}

enum class DifficultyLevel(val label: String, val bengaliLabel: String) {
    BEGINNER("Beginner", "সহজ (প্রাথমিক)"),
    INTERMEDIATE("Intermediate", "মধ্যম"),
    ADVANCED("Advanced", "উন্নত")
}

data class Topic(
    val id: String,
    val title: String,
    val bengaliTitle: String,
    val iconName: String,
    val starterPrompt: String,
    val robotGreeting: String,
    val bengaliGreeting: String
)

data class CorrectionResult(
    val originalText: String,
    val correctedText: String,
    val bengaliExplanation: String,
    val naturalAlternative: String,
    val pronunciationTip: String? = null
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val bengaliTranslation: String? = null,
    val correction: CorrectionResult? = null,
    val isDemoMessage: Boolean = false
)

data class PronunciationItem(
    val id: String,
    val word: String,
    val ipa: String,
    val bengaliPhonetic: String,
    val bengaliMeaning: String,
    val sampleSentence: String,
    val phoneticTip: String,
    val commonMistake: String
)

data class VocabWord(
    val id: String,
    val englishWord: String,
    val bengaliMeaning: String,
    val phonetic: String,
    val partOfSpeech: String,
    val exampleSentenceEn: String,
    val exampleSentenceBn: String,
    val category: String,
    val isLearned: Boolean = false
)

data class GrammarQuestion(
    val id: String,
    val ruleTitle: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val bengaliExplanation: String,
    val commonBengaliError: String
)

data class SentenceBuilderExercise(
    val id: String,
    val bengaliMeaning: String,
    val scrambledWords: List<String>,
    val correctSentence: String,
    val grammarNote: String
)

data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val bengaliPrompt: String,
    val targetPhrase: String,
    val xpReward: Int,
    val isCompleted: Boolean = false
)
