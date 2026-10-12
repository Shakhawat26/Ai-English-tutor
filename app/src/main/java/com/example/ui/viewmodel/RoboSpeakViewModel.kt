package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.database.AppDatabase
import com.example.data.database.SavedVocabEntity
import com.example.data.database.UserProgressEntity
import com.example.data.model.ChatMessage
import com.example.data.model.CorrectionResult
import com.example.data.model.DailyChallenge
import com.example.data.model.DifficultyLevel
import com.example.data.model.GrammarQuestion
import com.example.data.model.PronunciationItem
import com.example.data.model.RobotPersonality
import com.example.data.model.RobotState
import com.example.data.model.SentenceBuilderExercise
import com.example.data.model.Topic
import com.example.data.model.VocabWord
import com.example.data.repository.CurriculumData
import com.example.speech.VoiceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

data class UiState(
    val learnerName: String = "Shakhawat",
    val streakDays: Int = 5,
    val speakingMinutes: Int = 42,
    val wordsLearned: Int = 128,
    val totalXp: Int = 850,
    val currentLevel: DifficultyLevel = DifficultyLevel.BEGINNER,
    val selectedPersonality: RobotPersonality = RobotPersonality.FRIENDLY,
    val selectedTopic: Topic = CurriculumData.TOPICS[0],
    val robotState: RobotState = RobotState.IDLE,
    val chatMessages: List<ChatMessage> = emptyList(),
    val isMicListening: Boolean = false,
    val micRms: Float = 0f,
    val isTtsSpeaking: Boolean = false,
    val currentlySpeakingId: String? = null,
    val voiceSpeechRate: Float = 0.95f,
    val voicePitch: Float = 1.0f,
    val voiceAccent: String = "US",
    val instantCorrectionEnabled: Boolean = true,
    val isLiveAiConnected: Boolean = false,
    val errorMessage: String? = null,
    val isDarkMode: Boolean = true,
    // Pronunciation Trainer
    val currentPronunciationIndex: Int = 0,
    val pronunciationFeedback: String? = null,
    val pronunciationSuccess: Boolean? = null,
    val userSpokenPronunciation: String? = null,
    // Grammar Quiz
    val currentGrammarIndex: Int = 0,
    val selectedGrammarAnswer: Int? = null,
    val grammarExplanationVisible: Boolean = false,
    val grammarScore: Int = 0,
    // Sentence Builder
    val currentSentenceBuilderIndex: Int = 0,
    val sentenceBuilderSelectedWords: List<String> = emptyList(),
    val sentenceBuilderSuccess: Boolean? = null,
    // Daily Challenge
    val dailyChallengeCompleted: Boolean = false,
    // Vocab Flashcard
    val currentVocabIndex: Int = 0,
    val isVocabFlipped: Boolean = false
)

class RoboSpeakViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db.dao()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var voiceManager: VoiceManager? = null

    init {
        initVoiceManager(application)
        loadUserProgress()
        checkLiveAi()
        initConversation(CurriculumData.TOPICS[0])
    }

    private fun checkLiveAi() {
        _uiState.value = _uiState.value.copy(
            isLiveAiConnected = GeminiClient.isApiKeyConfigured()
        )
    }

    private fun initVoiceManager(context: Application) {
        voiceManager = VoiceManager(context) { isReady ->
            // Voice ready
        }.apply {
            onListeningStateChanged = { listening ->
                _uiState.value = _uiState.value.copy(
                    isMicListening = listening,
                    robotState = if (listening) RobotState.LISTENING else RobotState.IDLE
                )
            }
            onRmsChangedListener = { rms ->
                _uiState.value = _uiState.value.copy(micRms = rms)
            }
            onSpeechResultListener = { recognizedText ->
                handleUserSpokenText(recognizedText)
            }
            onSpeechErrorListener = { errorMsg ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = errorMsg,
                    robotState = RobotState.IDLE
                )
            }
            onTtsPlaybackChanged = { isSpeaking ->
                _uiState.value = _uiState.value.copy(
                    isTtsSpeaking = isSpeaking,
                    robotState = if (isSpeaking) RobotState.SPEAKING else RobotState.IDLE,
                    currentlySpeakingId = if (isSpeaking) _uiState.value.currentlySpeakingId else null
                )
            }
        }
    }

    private fun loadUserProgress() {
        viewModelScope.launch {
            val progress = dao.getUserProgressOnce()
            if (progress != null) {
                _uiState.value = _uiState.value.copy(
                    learnerName = progress.learnerName,
                    streakDays = progress.streakDays,
                    speakingMinutes = (progress.totalSpeakingSeconds / 60).toInt(),
                    wordsLearned = progress.totalWordsLearned,
                    totalXp = progress.totalXp,
                    currentLevel = try { DifficultyLevel.valueOf(progress.currentLevel) } catch (e: Exception) { DifficultyLevel.BEGINNER },
                    selectedPersonality = try { RobotPersonality.valueOf(progress.preferredPersonality) } catch (e: Exception) { RobotPersonality.FRIENDLY },
                    instantCorrectionEnabled = progress.instantCorrectionEnabled,
                    voiceSpeechRate = progress.voiceSpeechRate,
                    voicePitch = progress.voicePitch,
                    voiceAccent = progress.voiceAccent,
                    isDarkMode = progress.isDarkMode
                )
            } else {
                // Initialize default record
                dao.insertUserProgress(UserProgressEntity())
            }
        }
    }

    fun initConversation(topic: Topic) {
        voiceManager?.stopSpeaking()
        val initialGreeting = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = topic.robotGreeting,
            isUser = false,
            bengaliTranslation = topic.bengaliGreeting,
            isDemoMessage = !_uiState.value.isLiveAiConnected
        )
        _uiState.value = _uiState.value.copy(
            selectedTopic = topic,
            chatMessages = listOf(initialGreeting),
            robotState = RobotState.IDLE
        )
        // Automatically speak robot greeting
        viewModelScope.launch {
            delay(300)
            speakText(initialGreeting.id, initialGreeting.text)
        }
    }

    fun setTopic(topic: Topic) {
        initConversation(topic)
    }

    fun setPersonality(personality: RobotPersonality) {
        _uiState.value = _uiState.value.copy(selectedPersonality = personality)
        persistProgress()
    }

    fun setDifficulty(difficulty: DifficultyLevel) {
        _uiState.value = _uiState.value.copy(currentLevel = difficulty)
        persistProgress()
    }

    fun toggleDarkMode() {
        val newMode = !_uiState.value.isDarkMode
        _uiState.value = _uiState.value.copy(isDarkMode = newMode)
        persistProgress()
    }

    fun toggleInstantCorrection() {
        val newSetting = !_uiState.value.instantCorrectionEnabled
        _uiState.value = _uiState.value.copy(instantCorrectionEnabled = newSetting)
        persistProgress()
    }

    fun updateVoiceSettings(rate: Float, pitch: Float, accent: String) {
        _uiState.value = _uiState.value.copy(
            voiceSpeechRate = rate,
            voicePitch = pitch,
            voiceAccent = accent
        )
        persistProgress()
    }

    fun startListening() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
        voiceManager?.startListening()
    }

    fun stopListening() {
        voiceManager?.stopListening()
    }

    fun handleUserSpokenText(text: String) {
        if (text.isBlank()) return
        sendMessage(text)
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = userText.trim(),
            isUser = true
        )

        val updatedList = _uiState.value.chatMessages + userMessage
        _uiState.value = _uiState.value.copy(
            chatMessages = updatedList,
            robotState = RobotState.THINKING
        )

        viewModelScope.launch {
            // Build history pairs
            val history = updatedList.map {
                (if (it.isUser) "user" else "model") to it.text
            }

            val tutorResponse = GeminiClient.getTutorResponse(
                userMessage = userText,
                conversationHistory = history,
                topicTitle = _uiState.value.selectedTopic.title,
                personality = _uiState.value.selectedPersonality,
                difficulty = _uiState.value.currentLevel
            )

            val robotMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = tutorResponse.englishReply,
                isUser = false,
                bengaliTranslation = tutorResponse.bengaliTranslation,
                correction = if (_uiState.value.instantCorrectionEnabled) tutorResponse.correction else null,
                isDemoMessage = tutorResponse.isDemo
            )

            val finalList = _uiState.value.chatMessages + robotMessage
            val hasCorrection = robotMessage.correction != null

            _uiState.value = _uiState.value.copy(
                chatMessages = finalList,
                robotState = if (hasCorrection) RobotState.EXPLAINING else RobotState.SPEAKING,
                totalXp = _uiState.value.totalXp + 15,
                speakingMinutes = _uiState.value.speakingMinutes + 1
            )

            persistProgress()

            // Play voice reply
            speakText(robotMessage.id, robotMessage.text)
        }
    }

    fun speakText(messageId: String, text: String) {
        val accentLocale = if (_uiState.value.voiceAccent == "UK") Locale.UK else Locale.US
        _uiState.value = _uiState.value.copy(
            currentlySpeakingId = messageId,
            robotState = RobotState.SPEAKING
        )
        voiceManager?.speak(
            text = text,
            speechRate = _uiState.value.voiceSpeechRate,
            pitch = _uiState.value.voicePitch,
            accentLocale = accentLocale
        )
    }

    fun stopSpeaking() {
        voiceManager?.stopSpeaking()
        _uiState.value = _uiState.value.copy(
            isTtsSpeaking = false,
            currentlySpeakingId = null,
            robotState = RobotState.IDLE
        )
    }

    // Pronunciation Practice
    fun getCurrentPronunciationItem(): PronunciationItem {
        return CurriculumData.PRONUNCIATION_ITEMS[_uiState.value.currentPronunciationIndex]
    }

    fun playModelPronunciation() {
        val item = getCurrentPronunciationItem()
        speakText("model_pronunciation", item.word)
    }

    fun playModelSentence() {
        val item = getCurrentPronunciationItem()
        speakText("model_sentence", item.sampleSentence)
    }

    fun evaluateSpokenPronunciation(spokenText: String) {
        val target = getCurrentPronunciationItem().word.lowercase().trim()
        val spoken = spokenText.lowercase().trim()
        val isMatch = spoken.contains(target) || target.contains(spoken)

        val feedback = if (isMatch) {
            "Excellent! You pronounced '${getCurrentPronunciationItem().word}' accurately! 🎉\nউচ্চারণ চমৎকার ও স্পষ্ট হয়েছে!"
        } else {
            "Almost there! Recognized: \"$spokenText\". Focus on: ${getCurrentPronunciationItem().bengaliPhonetic}"
        }

        _uiState.value = _uiState.value.copy(
            userSpokenPronunciation = spokenText,
            pronunciationFeedback = feedback,
            pronunciationSuccess = isMatch,
            robotState = if (isMatch) RobotState.CELEBRATING else RobotState.EXPLAINING,
            totalXp = if (isMatch) _uiState.value.totalXp + 20 else _uiState.value.totalXp + 5
        )
        persistProgress()
    }

    fun nextPronunciation() {
        val nextIdx = (_uiState.value.currentPronunciationIndex + 1) % CurriculumData.PRONUNCIATION_ITEMS.size
        _uiState.value = _uiState.value.copy(
            currentPronunciationIndex = nextIdx,
            pronunciationFeedback = null,
            pronunciationSuccess = null,
            userSpokenPronunciation = null,
            robotState = RobotState.IDLE
        )
    }

    fun prevPronunciation() {
        val count = CurriculumData.PRONUNCIATION_ITEMS.size
        val prevIdx = (_uiState.value.currentPronunciationIndex - 1 + count) % count
        _uiState.value = _uiState.value.copy(
            currentPronunciationIndex = prevIdx,
            pronunciationFeedback = null,
            pronunciationSuccess = null,
            userSpokenPronunciation = null,
            robotState = RobotState.IDLE
        )
    }

    // Grammar Quiz
    fun getCurrentGrammarQuestion(): GrammarQuestion {
        return CurriculumData.GRAMMAR_QUESTIONS[_uiState.value.currentGrammarIndex]
    }

    fun selectGrammarAnswer(optionIndex: Int) {
        if (_uiState.value.selectedGrammarAnswer != null) return
        val currentQ = getCurrentGrammarQuestion()
        val isCorrect = optionIndex == currentQ.correctIndex

        _uiState.value = _uiState.value.copy(
            selectedGrammarAnswer = optionIndex,
            grammarExplanationVisible = true,
            grammarScore = if (isCorrect) _uiState.value.grammarScore + 1 else _uiState.value.grammarScore,
            robotState = if (isCorrect) RobotState.CELEBRATING else RobotState.EXPLAINING,
            totalXp = if (isCorrect) _uiState.value.totalXp + 25 else _uiState.value.totalXp + 5
        )
        persistProgress()
    }

    fun nextGrammarQuestion() {
        val nextIdx = (_uiState.value.currentGrammarIndex + 1) % CurriculumData.GRAMMAR_QUESTIONS.size
        _uiState.value = _uiState.value.copy(
            currentGrammarIndex = nextIdx,
            selectedGrammarAnswer = null,
            grammarExplanationVisible = false,
            robotState = RobotState.IDLE
        )
    }

    // Sentence Builder
    fun getCurrentSentenceBuilder(): SentenceBuilderExercise {
        return CurriculumData.SENTENCE_BUILDERS[_uiState.value.currentSentenceBuilderIndex]
    }

    fun tapSentenceBuilderWord(word: String) {
        val currentList = _uiState.value.sentenceBuilderSelectedWords
        val updated = if (currentList.contains(word)) {
            currentList - word
        } else {
            currentList + word
        }
        _uiState.value = _uiState.value.copy(
            sentenceBuilderSelectedWords = updated,
            sentenceBuilderSuccess = null
        )
    }

    fun checkSentenceBuilder() {
        val exercise = getCurrentSentenceBuilder()
        val constructed = _uiState.value.sentenceBuilderSelectedWords.joinToString(" ").trim().lowercase()
        val correct = exercise.correctSentence.trim().lowercase()
        val isSuccess = constructed == correct

        _uiState.value = _uiState.value.copy(
            sentenceBuilderSuccess = isSuccess,
            robotState = if (isSuccess) RobotState.CELEBRATING else RobotState.EXPLAINING,
            totalXp = if (isSuccess) _uiState.value.totalXp + 30 else _uiState.value.totalXp
        )
        persistProgress()
    }

    fun nextSentenceBuilder() {
        val nextIdx = (_uiState.value.currentSentenceBuilderIndex + 1) % CurriculumData.SENTENCE_BUILDERS.size
        _uiState.value = _uiState.value.copy(
            currentSentenceBuilderIndex = nextIdx,
            sentenceBuilderSelectedWords = emptyList(),
            sentenceBuilderSuccess = null,
            robotState = RobotState.IDLE
        )
    }

    // Vocabulary
    fun getCurrentVocab(): VocabWord {
        return CurriculumData.VOCABULARY_LIST[_uiState.value.currentVocabIndex]
    }

    fun flipVocabCard() {
        _uiState.value = _uiState.value.copy(isVocabFlipped = !_uiState.value.isVocabFlipped)
    }

    fun nextVocab() {
        val nextIdx = (_uiState.value.currentVocabIndex + 1) % CurriculumData.VOCABULARY_LIST.size
        _uiState.value = _uiState.value.copy(
            currentVocabIndex = nextIdx,
            isVocabFlipped = false
        )
    }

    fun prevVocab() {
        val count = CurriculumData.VOCABULARY_LIST.size
        val prevIdx = (_uiState.value.currentVocabIndex - 1 + count) % count
        _uiState.value = _uiState.value.copy(
            currentVocabIndex = prevIdx,
            isVocabFlipped = false
        )
    }

    fun markVocabLearned(wordId: String) {
        viewModelScope.launch {
            val vocab = getCurrentVocab()
            dao.upsertSavedVocab(
                SavedVocabEntity(
                    wordId = vocab.id,
                    englishWord = vocab.englishWord,
                    bengaliMeaning = vocab.bengaliMeaning,
                    isLearned = true
                )
            )
            _uiState.value = _uiState.value.copy(
                wordsLearned = _uiState.value.wordsLearned + 1,
                totalXp = _uiState.value.totalXp + 10,
                robotState = RobotState.CELEBRATING
            )
            persistProgress()
            delay(1200)
            _uiState.value = _uiState.value.copy(robotState = RobotState.IDLE)
        }
    }

    // Daily Challenge
    fun completeDailyChallenge() {
        _uiState.value = _uiState.value.copy(
            dailyChallengeCompleted = true,
            totalXp = _uiState.value.totalXp + 50,
            streakDays = _uiState.value.streakDays + 1,
            robotState = RobotState.CELEBRATING
        )
        persistProgress()
    }

    fun resetDailyChallenge() {
        _uiState.value = _uiState.value.copy(dailyChallengeCompleted = false)
    }

    fun clearLocalData() {
        viewModelScope.launch {
            dao.clearChatHistory()
            val resetProgress = UserProgressEntity(
                learnerName = "Shakhawat",
                streakDays = 1,
                totalSpeakingSeconds = 0,
                totalWordsLearned = 0,
                totalXp = 100
            )
            dao.insertUserProgress(resetProgress)
            _uiState.value = _uiState.value.copy(
                streakDays = 1,
                speakingMinutes = 0,
                wordsLearned = 0,
                totalXp = 100,
                chatMessages = emptyList()
            )
            initConversation(_uiState.value.selectedTopic)
        }
    }

    private fun persistProgress() {
        viewModelScope.launch {
            val s = _uiState.value
            val entity = UserProgressEntity(
                id = 1,
                learnerName = s.learnerName,
                streakDays = s.streakDays,
                totalSpeakingSeconds = s.speakingMinutes * 60L,
                totalWordsLearned = s.wordsLearned,
                totalXp = s.totalXp,
                currentLevel = s.currentLevel.name,
                preferredPersonality = s.selectedPersonality.name,
                instantCorrectionEnabled = s.instantCorrectionEnabled,
                voiceSpeechRate = s.voiceSpeechRate,
                voicePitch = s.voicePitch,
                voiceAccent = s.voiceAccent,
                isDarkMode = s.isDarkMode
            )
            dao.insertUserProgress(entity)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager?.destroy()
    }
}
