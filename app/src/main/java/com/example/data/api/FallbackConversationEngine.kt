package com.example.data.api

import com.example.data.model.CorrectionResult
import com.example.data.model.DifficultyLevel
import com.example.data.model.RobotPersonality

object FallbackConversationEngine {

    fun generateResponse(
        userMessage: String,
        topicTitle: String,
        personality: RobotPersonality,
        difficulty: DifficultyLevel
    ): TutorResponse {
        val lower = userMessage.trim().lowercase()

        // 1. Check for common Bengali speaker grammatical errors
        val commonCorrection = checkCommonMistakes(userMessage)

        // 2. Formulate personality-based reply
        val (englishReply, bengaliTranslation) = when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
                when (personality) {
                    RobotPersonality.FRIENDLY ->
                        "Hello there! It is wonderful to talk with you today. How is your day going?" to "হ্যালো! আজ আপনার সাথে কথা বলতে পেরে খুব ভালো লাগছে। দিনটি কেমন কাটছে?"
                    RobotPersonality.STRICT ->
                        "Greetings. Please speak in complete and structured sentences today. What is our focus?" to "নমস্কার। আজ সম্পূর্ণ ও নির্ভুল বাক্যে কথা বলুন। আমাদের আলোচনার বিষয় কী?"
                    RobotPersonality.FUNNY ->
                        "Beep boop! Hey superstar! My robotic circuits are super excited to chat with you today!" to "বিপ বপ! আরে সুপারস্টার! আমার রোবট সার্কিট আজ আপনার সাথে আড্ডা দিতে ভীষণ উত্তেজিত!"
                    RobotPersonality.IELTS ->
                        "Good day. Welcome to your IELTS Speaking diagnostic session. Are you ready for Part 1 questions?" to "শুভদিন। আপনার IELTS স্পিকিং প্র্যাকটিস সেশনে স্বাগতম। আপনি কি পার্ট ১ প্রশ্নের জন্য প্রস্তুত?"
                }
            }
            lower.contains("name") || lower.contains("myself") || lower.contains("shakhawat") -> {
                when (personality) {
                    RobotPersonality.FRIENDLY ->
                        "Pleased to meet you! You can call me RoboSpeak. What hobbies do you enjoy in your free time?" to "আপনার সাথে পরিচিত হয়ে আনন্দিত হলাম! আপনি আমাকে রোবোস্পিক বলতে পারেন। অবসরে কী করতে পছন্দ করেন?"
                    RobotPersonality.STRICT ->
                        "Acknowledged. Always remember to state your full introduction clearly. What is your current profession or study?" to "শুনলাম। পরিচয় সবসময় স্পষ্টভাবে উপস্থাপন করুন। বর্তমানে আপনার পড়াশোনা বা পেশা কী?"
                    RobotPersonality.FUNNY ->
                        "Awesome name! You sound way cooler than my robot neighbors! Tell me, do you like robotics or football?" to "দারুণ নাম! আপনি আমার অন্য রোবট বন্ধুদের চেয়েও স্মার্ট! বলুন তো, আপনি রোবটিক্স নাকি ফুটবল পছন্দ করেন?"
                    RobotPersonality.IELTS ->
                        "Thank you. In the IELTS test, speaking about your background smoothly gives a great first impression. Where are you from?" to "ধন্যবাদ। IELTS পরীক্ষায় নিজের পটভূমি সাবলীলভাবে বলতে পারলে ভালো স্কোর পাওয়া যায়। আপনি কোথা থেকে এসেছেন?"
                }
            }
            lower.contains("job") || lower.contains("work") || lower.contains("career") || lower.contains("interview") -> {
                "That sounds exciting! What are your key responsibilities and your greatest strength at work?" to "শুনতে দারুণ লাগছে! কর্মক্ষেত্রে আপনার মূল দায়িত্ব এবং সবচেয়ে বড় শক্তি কোনটি?"
            }
            lower.contains("dhaka") || lower.contains("bangladesh") || lower.contains("country") -> {
                "Bangladesh has such vibrant culture and warm people! What is your favorite place to visit there?" to "বাংলাদেশের সংস্কৃতি চমৎকার এবং মানুষজন খুব আন্তরিক! সেখানে আপনার প্রিয় ভ্রমণের জায়গা কোনটি?"
            }
            lower.contains("food") || lower.contains("restaurant") || lower.contains("eat") || lower.contains("biryani") -> {
                "Delicious! Traditional dishes like Kacchi Biryani and sweet tea are legendary. What do you enjoy eating the most?" to "দারুণ সুস্বাদু! কাচ্চি বিরিয়ানি ও রং চা তো বিখ্যাত। আপনি সবচেয়ে বেশি কী খেতে ভালোবাসেন?"
            }
            lower.contains("ielts") || lower.contains("band") || lower.contains("exam") -> {
                "For a high band score, focus on lexical resource, fluency, and complex sentence connectors. Shall we try a cue-card topic?" to "উচ্চ ব্যান্ড স্কোরের জন্য শব্দভাণ্ডার, সাবলীলতা ও কানেক্টরের দিকে লক্ষ্য রাখুন। আমরা কি কিউ-কার্ড টপিক চেষ্টা করব?"
            }
            else -> {
                when (personality) {
                    RobotPersonality.FRIENDLY ->
                        "That is very interesting! Could you tell me a little bit more about that in English?" to "বিষয়টি খুবই আকর্ষণীয়! এ ব্যাপারে ইংরেজিতে আর একটু বিস্তারিত বলতে পারেন?"
                    RobotPersonality.STRICT ->
                        "Good effort. Pay close attention to your verb tenses and prepositions as you continue." to "ভালো চেষ্টা। কথা বলার সময় ক্রিয়া এবং প্রিপজিশনগুলোর দিকে বিশেষ খেয়াল রাখুন।"
                    RobotPersonality.FUNNY ->
                        "Haha, my robot brain is processing that at light speed! What happened next?" to "হাহা, আমার রোবট মস্তিষ্ক বিদ্যুতগতিতে প্রসেস করছে! তারপর কী হলো?"
                    RobotPersonality.IELTS ->
                        "Well said. Try expanding your response with specific examples to demonstrate higher fluency." to "সুন্দর বলেছেন। সাবলীলতা বাড়ানোর জন্য সুনির্দিষ্ট উদাহরণ যোগ করে উত্তর বড় করার চেষ্টা করুন।"
                }
            }
        }

        return TutorResponse(
            englishReply = englishReply,
            bengaliTranslation = bengaliTranslation,
            correction = commonCorrection,
            isDemo = true
        )
    }

    private fun checkCommonMistakes(userText: String): CorrectionResult? {
        val trimmed = userText.trim()
        val lower = trimmed.lowercase()

        return when {
            lower.contains("i goes") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("i goes", "I go", ignoreCase = true),
                bengaliExplanation = "I-এর সঙ্গে ক্রিয়াপদের base form (go) বসে, goes নয়। He/She/It এর ক্ষেত্রে goes হয়।",
                naturalAlternative = "I go to school every morning.",
                pronunciationTip = "'go' শব্দটি দীর্ঘ /ɡoʊ/ উচ্চারণ করুন।"
            )
            lower.contains("he don't") || lower.contains("she don't") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("don't", "doesn't", ignoreCase = true),
                bengaliExplanation = "Third person singular (He/She)-এর সাথে negative বাক্যে doesn't বসে, don't নয়।",
                naturalAlternative = "He doesn't know the answer.",
                pronunciationTip = "'doesn't' উচ্চারণ /'dʌznt/।"
            )
            lower.contains("i am agree") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("i am agree", "I agree", ignoreCase = true),
                bengaliExplanation = "'agree' নিজেই একটি verb। তাই 'I am agree' না বলে 'I agree' বলুন।",
                naturalAlternative = "I completely agree with you.",
                pronunciationTip = "'agree' শব্দের দ্বিতীয় সিলেবলে জোর দিন: /əˈɡriː/।"
            )
            lower.contains("myself ") && (lower.startsWith("myself") || lower.contains("i am myself")) -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("myself", "I am", ignoreCase = true),
                bengaliExplanation = "ইংরেজি আত্মপরিচয়ে 'Myself...' বলা ব্যাকরণগতভাবে ভুল। 'My name is...' বা 'I am...' বলুন।",
                naturalAlternative = "Hello, my name is Shakhawat.",
                pronunciationTip = "'Name is' একসাথে লিঙ্ক করে /neɪm ɪz/ বলুন।"
            )
            lower.contains("by walk") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("by walk", "on foot", ignoreCase = true),
                bengaliExplanation = "পায়ে হেঁটে কোথাও যাওয়া বোঝাতে 'by walk' নয়, 'on foot' বা 'walked' ব্যবহার করতে হয়।",
                naturalAlternative = "I came on foot.",
                pronunciationTip = "'foot' এর vowel সংক্ষিপ্ত /fʊt/।"
            )
            lower.contains("discuss about") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("discuss about", "discuss", ignoreCase = true),
                bengaliExplanation = "'Discuss' শব্দের পরেই সরাসরি বিষয় আসে, 'about' ব্যবহার করা অপ্রয়োজনীয়।",
                naturalAlternative = "Let us discuss the topic.",
                pronunciationTip = "'discuss' /dɪˈskʌs/।"
            )
            lower.contains("did you saw") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("did you saw", "did you see", ignoreCase = true),
                bengaliExplanation = "প্রশ্নবোধক বাক্যে 'Did' থাকলে মূল verb সর্বদা base form (see) হবে।",
                naturalAlternative = "Did you see the match yesterday?",
                pronunciationTip = "'see' দীর্ঘ /siː/।"
            )
            lower.contains("taller then") -> CorrectionResult(
                originalText = trimmed,
                correctedText = trimmed.replace("taller then", "taller than", ignoreCase = true),
                bengaliExplanation = "তুলনা করার ক্ষেত্রে 'than' বসে, 'then' (তারপর) নয়।",
                naturalAlternative = "He is taller than me.",
                pronunciationTip = "'than' উচ্চারণ /ðæn/।"
            )
            else -> null
        }
    }
}
