package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.FallbackConversationEngine
import com.example.data.model.DifficultyLevel
import com.example.data.model.RobotPersonality
import com.example.data.repository.CurriculumData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun testAppNameResource() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RoboSpeak AI", appName)
  }

  @Test
  fun testCurriculumTopicsAndVocabulary() {
    assertTrue(CurriculumData.TOPICS.isNotEmpty())
    assertEquals(9, CurriculumData.TOPICS.size)
    assertTrue(CurriculumData.PRONUNCIATION_ITEMS.isNotEmpty())
    assertTrue(CurriculumData.GRAMMAR_QUESTIONS.isNotEmpty())
    assertTrue(CurriculumData.VOCABULARY_LIST.isNotEmpty())
  }

  @Test
  fun testBengaliLearnerGrammarCorrectionEngine() {
    val response = FallbackConversationEngine.generateResponse(
      userMessage = "I goes to school every day",
      topicTitle = "Daily Life Routine",
      personality = RobotPersonality.FRIENDLY,
      difficulty = DifficultyLevel.BEGINNER
    )

    assertNotNull(response.correction)
    assertEquals("I go to school every day", response.correction?.correctedText)
    assertTrue(response.correction?.bengaliExplanation?.contains("go") == true)
  }
}
