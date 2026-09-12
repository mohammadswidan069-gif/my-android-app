package com.example.data.model

data class TestCase(
    val id: Int,
    val input: String,
    val expectedOutput: String,
    val explanation: String = ""
)

data class Problem(
    val id: String, // e.g., "4A", "71A", "231A"
    val contestId: Int,
    val index: String,
    val titleAr: String,
    val titleEn: String,
    val rating: Int, // 800, 900, 1000, 1200, 1400...
    val tags: List<String>,
    val statementAr: String,
    val inputAr: String,
    val outputAr: String,
    val solutionLogicAr: String,
    val cppModelSolution: String,
    val starterCode: String,
    val testCases: List<TestCase>,
    val timeLimit: String = "1.0 ثانية",
    val memoryLimit: String = "256 ميغابايت",
    val difficultyLevelAr: String = when {
        rating < 1000 -> "مبتدئ جداً"
        rating < 1200 -> "مبتدئ"
        rating < 1400 -> "متوسط"
        rating < 1600 -> "متقدم"
        else -> "خبير"
    }
)

data class DailyChallenge(
    val id: String,
    val dateString: String,
    val problemId: String,
    val targetLevelAr: String,
    val bonusXp: Int = 150,
    val isCompleted: Boolean = false
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val handle: String,
    val rating: Int,
    val solvedCount: Int,
    val xp: Int,
    val badge: String,
    val countryFlag: String = "🇸🇦",
    val isCurrentUser: Boolean = false
)

data class Lesson(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val category: String,
    val levelAr: String,
    val readTimeAr: String,
    val summaryAr: String,
    val contentAr: String,
    val cppCodeExample: String,
    val relatedProblemIds: List<String> = emptyList()
)

data class TestExecutionResult(
    val testCaseId: Int,
    val input: String,
    val expectedOutput: String,
    val actualOutput: String,
    val isPassed: Boolean,
    val executionTimeMs: Long,
    val errorMessage: String? = null
)
