package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.CorrectionResult
import com.example.data.model.DifficultyLevel
import com.example.data.model.RobotPersonality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class TutorResponse(
    val englishReply: String,
    val bengaliTranslation: String,
    val correction: CorrectionResult?,
    val isDemo: Boolean
)

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun getTutorResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>>, // role to text
        topicTitle: String,
        personality: RobotPersonality,
        difficulty: DifficultyLevel
    ): TutorResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (!isApiKeyConfigured()) {
            Log.d(TAG, "API key not configured or placeholder, using intelligent offline tutor engine")
            return@withContext FallbackConversationEngine.generateResponse(
                userMessage = userMessage,
                topicTitle = topicTitle,
                personality = personality,
                difficulty = difficulty
            )
        }

        try {
            val systemInstructionText = """
                You are RoboSpeak AI, an interactive English tutor robot helping Bengali-speaking learners in Bangladesh.
                Tutor Personality: ${personality.title}. ${personality.tonePrompt}
                Target Level: ${difficulty.label}. Topic: $topicTitle.
                
                Instructions:
                1. Reply to the learner in natural, engaging English suitable for their level (1 to 3 short spoken sentences).
                2. Provide the Bengali translation of your reply for easy understanding.
                3. Examine the learner's message for any grammatical errors or awkward phrasing common to Bengali speakers (e.g., tense, prepositions, subject-verb agreement).
                4. If there is a mistake, explain it in simple, friendly Bengali (e.g. "I-এর সাথে go বসে, goes নয়।"). If no mistake, set mistakeFound to false.
                
                Format your response STRICTLY as valid JSON with no markdown wrapping:
                {
                   "reply": "Your English reply here",
                   "bengaliTranslation": "বাংলা অর্থ এখানে",
                   "mistakeFound": true/false,
                   "correctedSentence": "Corrected English sentence if mistake, else empty",
                   "bengaliExplanation": "সহজ বাংলা ব্যাখ্যা if mistake, else empty",
                   "naturalAlternative": "A natural everyday way to say it if mistake, else empty",
                   "pronunciationTip": "Pronunciation advice if applicable, else empty"
                }
            """.trimIndent()

            val contentsArray = JSONArray()

            // Include last 6 turns for context
            val recentHistory = conversationHistory.takeLast(6)
            for ((role, text) in recentHistory) {
                val partObj = JSONObject().put("text", text)
                val contentObj = JSONObject()
                    .put("role", if (role == "user") "user" else "model")
                    .put("parts", JSONArray().put(partObj))
                contentsArray.put(contentObj)
            }

            // Current user message
            val currentUserPart = JSONObject().put("text", userMessage)
            contentsArray.put(
                JSONObject()
                    .put("role", "user")
                    .put("parts", JSONArray().put(currentUserPart))
            )

            val rootJson = JSONObject().apply {
                put("contents", contentsArray)
                put(
                    "systemInstruction",
                    JSONObject().put(
                        "parts",
                        JSONArray().put(JSONObject().put("text", systemInstructionText))
                    )
                )
                put(
                    "generationConfig",
                    JSONObject().apply {
                        put("temperature", 0.7)
                        put("responseMimeType", "application/json")
                    }
                )
            }

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini API error code: ${response.code}")
                return@withContext FallbackConversationEngine.generateResponse(
                    userMessage = userMessage,
                    topicTitle = topicTitle,
                    personality = personality,
                    difficulty = difficulty
                )
            }

            val responseBodyString = response.body?.string().orEmpty()
            val parsedJson = JSONObject(responseBodyString)
            val candidates = parsedJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val textContent = firstCandidate
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text")
                .orEmpty()

            val cleanedJsonText = textContent
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val resultObj = JSONObject(cleanedJsonText)
            val reply = resultObj.optString("reply", "That's great! Let's keep practicing.")
            val bengali = resultObj.optString("bengaliTranslation", "দারুণ! চলুন কথা বলা চালিয়ে যাই।")
            val mistakeFound = resultObj.optBoolean("mistakeFound", false)

            val correction = if (mistakeFound) {
                CorrectionResult(
                    originalText = userMessage,
                    correctedText = resultObj.optString("correctedSentence", userMessage),
                    bengaliExplanation = resultObj.optString("bengaliExplanation", "সহজ নিয়ম লক্ষ্য করুন।"),
                    naturalAlternative = resultObj.optString("naturalAlternative", ""),
                    pronunciationTip = resultObj.optString("pronunciationTip", "").takeIf { it.isNotBlank() }
                )
            } else null

            TutorResponse(
                englishReply = reply,
                bengaliTranslation = bengali,
                correction = correction,
                isDemo = false
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in getTutorResponse: ${e.message}", e)
            FallbackConversationEngine.generateResponse(
                userMessage = userMessage,
                topicTitle = topicTitle,
                personality = personality,
                difficulty = difficulty
            )
        }
    }
}
