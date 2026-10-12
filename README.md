# RoboSpeak AI — English Speaking with Robot 🤖🇧🇩

**RoboSpeak AI** is a complete, modern Android English learning application designed specifically for Bengali-speaking beginners and intermediate learners in Bangladesh. It features an interactive 3D-styled animated robot tutor, speech recognition, speech-to-text and text-to-speech feedback, Bengali grammar corrections, pronunciation coaching, and daily streak gamification.

---

## 🌟 Key Features

1. **Futuristic Animated Robot Character**
   - Expressive 3D-styled animated robot tutor with dynamic states: **Idle**, **Listening**, **Thinking**, **Speaking**, **Celebrating**, and **Explaining Grammar**.
   - Futuristic sci-fi UI with electric cyan, neon purple, and glowing visual cues.
   - Light and Dark mode support with full Material 3 dynamic theming.

2. **9 Core Real-World Conversation Topics**
   - **Introducing Yourself** (নিজের পরিচয় দেওয়া)
   - **Talking with Friends** (বন্ধুদের সাথে আড্ডা)
   - **School & Classroom** (স্কুল ও ক্লাসরুম কথোপকথন)
   - **Shopping & Restaurants** (কেনাকাটা ও রেস্তোরাঁ)
   - **Travelling & Airports** (ভ্রমণ ও এয়ারপোর্ট)
   - **Job Interviews** (চাকরির ইন্টারভিউ প্রস্তুতি)
   - **Daily Life Routine** (দৈনন্দিন জীবন ও রুটিন)
   - **IELTS Speaking Practice** (IELTS স্পিকিং টেস্ট)
   - **Talking with Foreigners** (বিদেশিদের সাথে সাবলীল কথা)

3. **Selectable Robot Personalities**
   - **Friendly Robot**: Patient, encouraging, and gentle for beginners.
   - **Strict Teacher Robot**: Grammatically precise and disciplined.
   - **Funny Robot**: Playful banter, humor, and casual banter.
   - **IELTS Coach Robot**: Band 7+ vocabulary, connectors, and cue-card strategies.

4. **"Correct My English" with Bengali Explanations**
   - Instant grammar detection for common Bengali mistakes (e.g., *"I goes to school"* ➔ *"I go to school"*; *"I am agree"* ➔ *"I agree"*; *"Did you saw"* ➔ *"Did you see"*).
   - Clear explanations in Bengali (*"I-এর সঙ্গে go বসে, goes নয়"*).
   - Natural phrasing alternatives and pronunciation tips.

5. **Pronunciation Trainer for Bengali Speakers**
   - Phonetic breakdown for sounds difficult for Bengali speakers (*v* vs *b*, *th* /θ/, silent letters in *Wednesday*, *Comfortable*, *Schedule*).
   - Model audio playback with adjustable speed and accent (US / UK).
   - Voice recording with real-time speech recognition feedback.

6. **Daily Learning System & Gamification**
   - Word of the Day cards with audio and Bengali examples.
   - 3D Flip Flashcards with mastery tracking.
   - Multiple-choice grammar quizzes with instant Bengali feedback.
   - Sentence builder exercises (drag and arrange scrambled English words for Bengali sentences).
   - Streak counter 🔥, speaking minutes, words learned counter, and XP levels.

7. **Voice & Accessibility**
   - Android SpeechRecognizer with runtime `RECORD_AUDIO` permission handling.
   - Android TextToSpeech engine with speech rate, pitch, and US/UK voice accents.
   - Transparent microphone data and privacy disclosures.

---

## 🚀 Running & Building the App

### Requirements
- Android SDK 36 (minSdk: 24, targetSdk: 36)
- JDK 17 or 21
- Gradle (with Kotlin DSL)

### Compilation & Tests
Run local unit and Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```

Compile and assemble debug APK:
```bash
gradle assembleDebug
```

---

## 🔑 AI Integration & Gemini API Setup

RoboSpeak AI supports **Google Gemini 3.5 Flash** for dynamic, real-time conversations and corrections.

1. In AI Studio, open the **Secrets** panel.
2. Add your `GEMINI_API_KEY`.
3. Rebuild the application; the Secrets Gradle Plugin will inject it into `BuildConfig.GEMINI_API_KEY`.
4. *Demo & Offline Mode:* If no API key is provided, the app automatically runs in intelligent offline demo mode with pre-programmed conversational topics and grammar rules, clearly displaying the status in the UI.

---

## 📱 Google Play Store Release Preparation

### 1. Generating a Signed Android App Bundle (.aab)
```bash
gradle :app:bundleRelease
```
The output `.aab` file will be located in `app/build/outputs/bundle/release/app-release.aab`.

### 2. Play Store Listing Guidelines
- **App Title:** RoboSpeak AI (25 characters, adheres to the 30-character Play Policy limit).
- **Target Audience:** Bengali speakers learning English in Bangladesh and worldwide.
- **Content Rating:** Everyone (Educational).
- **Data Safety:**
  - *Audio Data:* Collected for conversational English practice and processed with user consent.
  - *Data Deletion:* Users can reset and delete all local chat history and learning progress anytime in Settings.
