package com.example.data.repository

import com.example.data.model.DailyChallenge
import com.example.data.model.GrammarQuestion
import com.example.data.model.PronunciationItem
import com.example.data.model.SentenceBuilderExercise
import com.example.data.model.Topic
import com.example.data.model.VocabWord

object CurriculumData {

    val TOPICS = listOf(
        Topic(
            id = "intro",
            title = "Introducing Yourself",
            bengaliTitle = "নিজের পরিচয় দেওয়া",
            iconName = "Person",
            starterPrompt = "Practice introducing your name, background, and goals.",
            robotGreeting = "Hello! I am RoboSpeak, your personal English tutor robot! What is your name and what do you do?",
            bengaliGreeting = "হ্যালো! আমি রোবোস্পিক, আপনার ব্যক্তিগত রোবট শিক্ষক! আপনার নাম কী এবং আপনি কী করেন?"
        ),
        Topic(
            id = "friends",
            title = "Talking with Friends",
            bengaliTitle = "বন্ধুদের সাথে আড্ডা",
            iconName = "Groups",
            starterPrompt = "Casual chit-chat, weekend plans, and hobbies.",
            robotGreeting = "Hey my friend! What are you planning to do this coming weekend?",
            bengaliGreeting = "আরে বন্ধু! আসছে সাপ্তাহিক ছুটিতে তোমার কী করার পরিকল্পনা আছে?"
        ),
        Topic(
            id = "school",
            title = "School & Classroom",
            bengaliTitle = "স্কুল ও ক্লাসরুম কথোপকথন",
            iconName = "School",
            starterPrompt = "Asking questions to teachers, group studies, exams.",
            robotGreeting = "Welcome to the academy! What is your favorite subject to study?",
            bengaliGreeting = "একাডেমিতে স্বাগতম! আপনার প্রিয় পড়ার বিষয় কোনটি?"
        ),
        Topic(
            id = "shopping",
            title = "Shopping & Restaurants",
            bengaliTitle = "কেনাকাটা ও রেস্তোরাঁয় অর্ডার",
            iconName = "Restaurant",
            starterPrompt = "Ordering food, asking for discounts, menu queries.",
            robotGreeting = "Welcome to RoboBistro! Can I take your order today or show you the specials?",
            bengaliGreeting = "রোবোবিস্ট্রোতে স্বাগতম! আপনি আজ কী অর্ডার করবেন নাকি বিশেষ মেনু দেখতে চান?"
        ),
        Topic(
            id = "travelling",
            title = "Travelling & Airports",
            bengaliTitle = "ভ্রমণ ও এয়ারপোর্ট",
            iconName = "Flight",
            starterPrompt = "Booking tickets, asking for directions, hotel check-in.",
            robotGreeting = "Hello traveler! Where are you flying today, and do you need boarding assistance?",
            bengaliGreeting = "হ্যালো পরিব্রাজক! আজ আপনি কোথায় যাচ্ছেন? বোর্ডিংয়ে কোনো সহায়তা প্রয়োজন?"
        ),
        Topic(
            id = "interview",
            title = "Job Interviews",
            bengaliTitle = "চাকরির ইন্টারভিউ প্রস্তুতি",
            iconName = "Work",
            starterPrompt = "Professional Q&A, strengths, weaknesses, salary expectations.",
            robotGreeting = "Good morning. Thank you for attending this interview. Could you please tell me about yourself?",
            bengaliGreeting = "শুভ সকাল। ইন্টারভিউতে আসার জন্য ধন্যবাদ। অনুগ্রহ করে আপনার নিজের সম্পর্কে কিছু বলুন?"
        ),
        Topic(
            id = "daily_life",
            title = "Daily Life Routine",
            bengaliTitle = "দৈনন্দিন জীবন ও রুটিন",
            iconName = "Schedule",
            starterPrompt = "Describing your morning routine, commute, and daily habits.",
            robotGreeting = "Hi! How does your typical weekday morning look like? What time do you wake up?",
            bengaliGreeting = "হাই! সাধারণ কাজের দিনে আপনার সকাল কেমন কাটে? আপনি কখন ঘুম থেকে ওঠেন?"
        ),
        Topic(
            id = "ielts",
            title = "IELTS Speaking Practice",
            bengaliTitle = "IELTS স্পিকিং টেস্ট",
            iconName = "Psychology",
            starterPrompt = "Part 1, Cue Card topics, and Part 3 follow-ups.",
            robotGreeting = "Welcome to the IELTS Speaking test simulator. In Part 1, let us discuss hometowns. Do you like living in your city?",
            bengaliGreeting = "IELTS স্পিকিং টেস্ট সিমুলেটরে স্বাগতম। পার্ট ১-এ চলুন নিজের শহর নিয়ে আলোচনা করি। আপনার শহরে বসবাস করতে কেমন লাগে?"
        ),
        Topic(
            id = "foreigners",
            title = "Talking with Foreigners",
            bengaliTitle = "বিদেশিদের সাথে সাবলীল কথা",
            iconName = "Public",
            starterPrompt = "Small talk with tourists, cultural exchange, confident speech.",
            robotGreeting = "Hello! Excuse me, could you tell me how to get to the National Museum?",
            bengaliGreeting = "হ্যালো! শুনুন, জাতীয় জাদুঘরে যাওয়ার পথটি কি একটু বলে দিতে পারেন?"
        )
    )

    val PRONUNCIATION_ITEMS = listOf(
        PronunciationItem(
            id = "p1",
            word = "Comfortable",
            ipa = "/ˈkʌmftəbl/",
            bengaliPhonetic = "কম্ফ-টেবল (কমফোর্ট-এবল নয়)",
            bengaliMeaning = "আরামদায়ক",
            sampleSentence = "This chair is very comfortable to sit on.",
            phoneticTip = "মাঝের 'or' উচ্চারণ করবেন না। এটি ৩ সিলেবল: com-for-table নয়, comf-ta-ble।",
            commonMistake = "বাংলাভাষীরা প্রায়ই 'কম-ফোর্ট-এ-বল' বলে ৪টি শব্দাংশে ভাগ করেন।"
        ),
        PronunciationItem(
            id = "p2",
            word = "Wednesday",
            ipa = "/ˈwɛnzdeɪ/",
            bengaliPhonetic = "ওয়েঞ্জ-ডে (ওয়েডনেসডে নয়)",
            bengaliMeaning = "বুধবার",
            sampleSentence = "We have our English exam on Wednesday.",
            phoneticTip = "'d' বর্ণটি সম্পূর্ণ নীরব (silent)। 'wenz-day' উচ্চারণ করুন।",
            commonMistake = "বানান দেখে 'ওয়েড-নেস-ডে' বলা ঠিক নয়।"
        ),
        PronunciationItem(
            id = "p3",
            word = "Very",
            ipa = "/ˈvɛri/",
            bengaliPhonetic = "ভেরি (উপরের দাঁত নিচের ঠোঁটে ছুঁইয়ে)",
            bengaliMeaning = "খুব / অত্যন্ত",
            sampleSentence = "I am very excited to learn English with RoboSpeak.",
            phoneticTip = "'V' বলার সময় ওপরের দাঁতের প্রান্ত নিচের ঠোঁটের ভেতরের অংশে হালকা চেপে বাতাস বের করুন (Friction)। 'ব' বা 'ভ' নয়।",
            commonMistake = "'Berry' (বেরি) বা ভাণ্ডার মতো বাংলা 'ভ' বলে ফেলা।"
        ),
        PronunciationItem(
            id = "p4",
            word = "Think",
            ipa = "/θɪŋk/",
            bengaliPhonetic = "থিঙ্ক (জিহ্বার ডগা দাঁতের মাঝে রেখে)",
            bengaliMeaning = "চিন্তা করা / মনে করা",
            sampleSentence = "I think we can achieve fluency with daily practice.",
            phoneticTip = "'Th' ধ্বনি (/θ/) তৈরি করতে জিহ্বার ডগা ওপরের এবং নিচের সামনের দাঁতের মাঝে হালকা রাখুন। 'ট' বা 'স' বলবেন না।",
            commonMistake = "'Sink' (সিংক) বা 'Tink' (টিংক) উচ্চারণ করা।"
        ),
        PronunciationItem(
            id = "p5",
            word = "Schedule",
            ipa = "/ˈskɛdʒuːl/",
            bengaliPhonetic = "স্কেজুল (US) বা শেডিউল (UK)",
            bengaliMeaning = "সময়সূচী",
            sampleSentence = "Let me check my daily practice schedule.",
            phoneticTip = "আমেরিকান উচ্চারণে 'sk' দিয়ে শুরু: sked-jool। ব্রিটিশ উচ্চারণে 'shed-yool'।",
            commonMistake = "'সি-ডিউল' বা উল্টোভাবে উচ্চারণ করা।"
        ),
        PronunciationItem(
            id = "p6",
            word = "Specific",
            ipa = "/spəˈsɪfɪk/",
            bengaliPhonetic = "স্পেসিফিক (ইস্পেসিফিক নয়)",
            bengaliMeaning = "নির্দিষ্ট / সুনির্দিষ্ট",
            sampleSentence = "Give a specific example during your speaking test.",
            phoneticTip = "শুরুতে কোনো অতিরিক্ত 'ই' (i) স্বরধ্বনি যোগ করবেন না। সরাসরি বাতাস ছেড়ে 's' দিয়ে শুরু করুন।",
            commonMistake = "বাংলা আঞ্চলিক টানে 'ইস্পেসিফিক' বা 'ইস্কুল' এর মতো অতিরিক্ত ই যুক্ত করা।"
        ),
        PronunciationItem(
            id = "p7",
            word = "Restaurant",
            ipa = "/ˈrɛstrɒnt/",
            bengaliPhonetic = "রেস্ট্রন্ট (রেস্টুরেন্ট নয়)",
            bengaliMeaning = "খাবার রেস্তোরাঁ",
            sampleSentence = "We had dinner at a cozy local restaurant.",
            phoneticTip = "'tau' অংশটি দ্রুত ও সংকুচিত হয়ে যায়: rest-ront।",
            commonMistake = "প্রতিটি অক্ষর টেনে 'রেস-টু-রেন্ট' উচ্চারণ করা।"
        ),
        PronunciationItem(
            id = "p8",
            word = "Colleague",
            ipa = "/ˈkɒliːɡ/",
            bengaliPhonetic = "কলীগ (কলিগ-উ নয়)",
            bengaliMeaning = "সহকর্মী",
            sampleSentence = "My colleague helped me prepare the presentation.",
            phoneticTip = "শেষের 'ue' নীরব। শুধুমাত্র 'koll-eeg' উচ্চারণ করুন।",
            commonMistake = "'কলিগ-উ' বা 'কলিগু' উচ্চারণ করা।"
        )
    )

    val VOCABULARY_LIST = listOf(
        VocabWord(
            id = "v1",
            englishWord = "Fluent",
            bengaliMeaning = "সাবলীল / অনর্গল",
            phonetic = "/ˈfluːənt/",
            partOfSpeech = "Adjective",
            exampleSentenceEn = "He became fluent in English within six months.",
            exampleSentenceBn = "তিনি ছয় মাসের মধ্যেই ইংরেজিতে সাবলীল হয়ে ওঠেন।",
            category = "Speaking"
        ),
        VocabWord(
            id = "v2",
            englishWord = "Hesitate",
            bengaliMeaning = "দ্বিধাবোধ করা / ইতস্তত করা",
            phonetic = "/ˈhɛzɪteɪt/",
            partOfSpeech = "Verb",
            exampleSentenceEn = "Do not hesitate to speak up even if you make mistakes.",
            exampleSentenceBn = "ভুল হলেও কথা বলতে দ্বিধাবোধ করবেন না।",
            category = "Mindset"
        ),
        VocabWord(
            id = "v3",
            englishWord = "Confident",
            bengaliMeaning = "আত্মবিশ্বাসী",
            phonetic = "/ˈkɒnfɪdənt/",
            partOfSpeech = "Adjective",
            exampleSentenceEn = "Practice in front of the mirror to feel more confident.",
            exampleSentenceBn = "আরও আত্মবিশ্বাসী হতে আয়নার সামনে অনুশীলন করুন।",
            category = "Mindset"
        ),
        VocabWord(
            id = "v4",
            englishWord = "Opportunity",
            bengaliMeaning = "সুযোগ",
            phonetic = "/ˌɒpəˈtjuːnɪti/",
            partOfSpeech = "Noun",
            exampleSentenceEn = "Every conversation is a great opportunity to learn.",
            exampleSentenceBn = "প্রতিটি কথোপকথনই শেখার দারুণ সুযোগ।",
            category = "Career"
        ),
        VocabWord(
            id = "v5",
            englishWord = "Pronounce",
            bengaliMeaning = "সঠিকভাবে উচ্চারণ করা",
            phonetic = "/prəˈnaʊns/",
            partOfSpeech = "Verb",
            exampleSentenceEn = "How do you pronounce this word correctly?",
            exampleSentenceBn = "আপনি এই শব্দটি কীভাবে সঠিকভাবে উচ্চারণ করেন?",
            category = "Speaking"
        ),
        VocabWord(
            id = "v6",
            englishWord = "Convenient",
            bengaliMeaning = "সুবিধাজনক",
            phonetic = "/kənˈviːniənt/",
            partOfSpeech = "Adjective",
            exampleSentenceEn = "Mobile learning apps are extremely convenient.",
            exampleSentenceBn = "মোবাইল লার্নিং অ্যাপ অত্যন্ত সুবিধাজনক।",
            category = "Daily Life"
        ),
        VocabWord(
            id = "v7",
            englishWord = "Essential",
            bengaliMeaning = "অপরিহার্য / অতীব গুরুত্বপূর্ণ",
            phonetic = "/ɪˈsɛnʃəl/",
            partOfSpeech = "Adjective",
            exampleSentenceEn = "Regular practice is essential for mastering any language.",
            exampleSentenceBn = "যেকোনো ভাষা আয়ত্ত করার জন্য নিয়মিত অনুশীলন অপরিহার্য।",
            category = "Daily Life"
        ),
        VocabWord(
            id = "v8",
            englishWord = "Accompany",
            bengaliMeaning = "সাথে যাওয়া / সঙ্গ দেওয়া",
            phonetic = "/əˈkʌmpəni/",
            partOfSpeech = "Verb",
            exampleSentenceEn = "Would you accompany me to the seminar?",
            exampleSentenceBn = "আপনি কি আমার সাথে সেমিনারে যাবেন?",
            category = "Travel"
        )
    )

    val GRAMMAR_QUESTIONS = listOf(
        GrammarQuestion(
            id = "g1",
            ruleTitle = "Subject-Verb Agreement (Go vs Goes)",
            question = "Which sentence is grammatically correct?",
            options = listOf(
                "I goes to office every day.",
                "I go to office every day.",
                "I am go to office every day.",
                "I going to office every day."
            ),
            correctIndex = 1,
            bengaliExplanation = "I-এর সাথে সাধারণ বর্তমানকালে verb-এর base form (go) বসে। He/She/It-এর সাথে goes বসে। 'I am go' কখনোই হয় না।",
            commonBengaliError = "I-এর সাথে s/es যুক্ত করা বা am-এর সাথে base verb বসানো।"
        ),
        GrammarQuestion(
            id = "g2",
            ruleTitle = "Expressing Agreement (Agree)",
            question = "How do you correctly say you share someone's opinion?",
            options = listOf(
                "I am agree with you.",
                "I agree with you.",
                "I am agreed with you.",
                "I have agree with you."
            ),
            correctIndex = 1,
            bengaliExplanation = "ইংরেজিতে 'agree' নিজেই একটি verb (ক্রিয়া)। তাই 'I agree with you' বলা সঠিক। 'I am agree' বলা একটি মারাত্নক ভুল।",
            commonBengaliError = "বাংলার 'আমি একমত' অনুবাদ করতে গিয়ে 'I am agree' বলা।"
        ),
        GrammarQuestion(
            id = "g3",
            ruleTitle = "Past Tense Questions (Did + Verb)",
            question = "Choose the correct question in past tense:",
            options = listOf(
                "Did you saw the match last night?",
                "Did you see the match last night?",
                "Did you seen the match last night?",
                "Do you saw the match last night?"
            ),
            correctIndex = 1,
            bengaliExplanation = "যেহেতু প্রশ্নে ইতিমধ্যে 'Did' (Past) ব্যবহার করা হয়েছে, তাই মূল verb সবসময় base form (see) হবে।",
            commonBengaliError = "Did-এর পর আবার past form (saw) ব্যবহার করা।"
        ),
        GrammarQuestion(
            id = "g4",
            ruleTitle = "Preposition with Listen",
            question = "Fill in the blank: Please ______ what the teacher says.",
            options = listOf(
                "listen",
                "listen to",
                "listening",
                "listened"
            ),
            correctIndex = 1,
            bengaliExplanation = "মনোযোগ দিয়ে কিছু শোনার ক্ষেত্রে 'listen'-এর পর সর্বদা preposition 'to' বসে। যেমন: 'listen to music', 'listen to me'।",
            commonBengaliError = "'to' বাদ দিয়ে শুধু listen বলা।"
        ),
        GrammarQuestion(
            id = "g5",
            ruleTitle = "Tense with Duration (Since vs For)",
            question = "Select the correct sentence:",
            options = listOf(
                "I am living in Dhaka for five years.",
                "I have been living in Dhaka for five years.",
                "I live in Dhaka since five years.",
                "I lived in Dhaka since five years."
            ),
            correctIndex = 1,
            bengaliExplanation = "অতীত থেকে শুরু হয়ে এখনও চলছে এমন সময় বোঝাতে Present Perfect Continuous (have been living) এবং সময়ের ব্যাপ্তির জন্য 'for' বসে।",
            commonBengaliError = "Present continuous 'am living' বা for-এর জায়গায় since ব্যবহার করা।"
        ),
        GrammarQuestion(
            id = "g6",
            ruleTitle = "Good At (Skills & Abilities)",
            question = "Fill in the blank: Shakhawat is very good ______ English.",
            options = listOf(
                "in",
                "at",
                "on",
                "with"
            ),
            correctIndex = 1,
            bengaliExplanation = "কোনো বিষয়ে পারদর্শিতা বা দক্ষতা প্রকাশ করতে 'good at' বসে। যেমন: 'good at math', 'good at speaking'।",
            commonBengaliError = "বাংলায় 'ইংরেজিতে ভালো' ভেবে 'good in' বসানো।"
        )
    )

    val SENTENCE_BUILDERS = listOf(
        SentenceBuilderExercise(
            id = "sb1",
            bengaliMeaning = "আমি প্রতিদিন সকালে ইংরেজি সংবাদপত্র পড়ি।",
            scrambledWords = listOf("every", "read", "I", "newspaper", "morning", "English"),
            correctSentence = "I read English newspaper every morning",
            grammarNote = "Subject (I) + Verb (read) + Object (English newspaper) + Time expression (every morning)."
        ),
        SentenceBuilderExercise(
            id = "sb2",
            bengaliMeaning = "আপনি কি আমাকে স্টেশন যাওয়ার পথ দেখাতে পারেন?",
            scrambledWords = listOf("to", "the", "Can", "me", "show", "station", "the", "way", "you"),
            correctSentence = "Can you show me the way to the station",
            grammarNote = "Polite request: Modal (Can) + Subject (you) + Verb (show) + Indirect Object (me) + Direct Object."
        ),
        SentenceBuilderExercise(
            id = "sb3",
            bengaliMeaning = "সে গতকাল স্কুলে আসেনি কারণ সে অসুস্থ ছিল।",
            scrambledWords = listOf("yesterday", "come", "He", "because", "he", "was", "didn't", "sick", "to", "school"),
            correctSentence = "He didn't come to school yesterday because he was sick",
            grammarNote = "Past tense negative (didn't come) এবং কারণ বোঝাতে conjunction (because)."
        )
    )

    val DAILY_CHALLENGES = listOf(
        DailyChallenge(
            id = "dc1",
            title = "Morning Routine Challenge",
            description = "Describe what you do after waking up in 3 simple sentences.",
            bengaliPrompt = "ঘুম থেকে উঠে আপনি কী করেন তা ৩টি সহজ বাক্যে বলুন।",
            targetPhrase = "I wake up early, brush my teeth, and have a cup of tea.",
            xpReward = 50
        ),
        DailyChallenge(
            id = "dc2",
            title = "Order at a Café",
            description = "Ask for a hot coffee and inquire about the price.",
            bengaliPrompt = "একটি কফি অর্ডার করুন এবং দাম জানতে চান।",
            targetPhrase = "Could I have a cup of coffee please? How much is it?",
            xpReward = 50
        ),
        DailyChallenge(
            id = "dc3",
            title = "Share Your Dream",
            description = "State what career goal you wish to achieve in the future.",
            bengaliPrompt = "ভবিষ্যতে আপনি কোন ক্যারিয়ার লক্ষ্য অর্জন করতে চান তা বলুন।",
            targetPhrase = "My goal is to become confident in English and build a great career.",
            xpReward = 60
        )
    )
}
