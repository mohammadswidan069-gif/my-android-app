package com.example.data.repository

import com.example.data.api.CodeforcesApiService
import com.example.data.local.AppDao
import com.example.data.local.ProblemProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.DailyChallenge
import com.example.data.model.LeaderboardUser
import com.example.data.model.Lesson
import com.example.data.model.Problem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProblemRepository(
    private val appDao: AppDao,
    private val cfApi: CodeforcesApiService = CodeforcesApiService.create()
) {
    // All problems sorted strictly easiest to hardest
    val allProblems: List<Problem> = SampleProblemsData.problems.sortedBy { it.rating }

    val allLessons: List<Lesson> = LessonsData.lessons

    val progressFlow: Flow<Map<String, ProblemProgressEntity>> = appDao.getAllProgress().map { list ->
        list.associateBy { it.problemId }
    }

    val userProfileFlow: Flow<UserProfileEntity?> = appDao.getUserProfile()

    suspend fun getProgress(problemId: String): ProblemProgressEntity? {
        return appDao.getProgressForProblem(problemId)
    }

    suspend fun saveCode(problemId: String, code: String) {
        val existing = appDao.getProgressForProblem(problemId)
        val updated = existing?.copy(userCode = code) ?: ProblemProgressEntity(
            problemId = problemId,
            userCode = code
        )
        appDao.saveProgress(updated)
    }

    suspend fun markProblemSolved(problemId: String, code: String, rating: Int): Int {
        val existing = appDao.getProgressForProblem(problemId)
        val isFirstTime = existing == null || !existing.isSolved

        val updated = (existing ?: ProblemProgressEntity(problemId = problemId)).copy(
            isSolved = true,
            solvedAt = System.currentTimeMillis(),
            userCode = code,
            attemptsCount = (existing?.attemptsCount ?: 0) + 1
        )
        appDao.saveProgress(updated)

        if (isFirstTime) {
            val xpGain = when {
                rating < 1000 -> 50
                rating < 1200 -> 80
                rating < 1400 -> 120
                else -> 200
            }
            val ratingGain = (rating / 100) + 10
            appDao.awardXpAndRating(xpGain, ratingGain, streak = 6)
            return xpGain
        }
        return 0
    }

    suspend fun initializeDefaultUser() {
        appDao.saveUserProfile(
            UserProfileEntity(
                id = 1,
                handle = "Codeforces_Hero",
                name = "المبرمج المبدع",
                rating = 1240,
                maxRating = 1310,
                xp = 650,
                level = 3,
                streakDays = 5,
                lastActiveDate = "2026-09-10"
            )
        )
    }

    suspend fun linkCodeforcesHandle(handle: String): Result<String> {
        return try {
            val response = cfApi.getUserInfo(handle.trim())
            if (response.status == "OK" && !response.result.isNullOrEmpty()) {
                val cfUser = response.result.first()
                val currentProfile = UserProfileEntity(
                    id = 1,
                    handle = cfUser.handle,
                    name = cfUser.handle,
                    rating = cfUser.rating ?: 1200,
                    maxRating = cfUser.maxRating ?: 1200,
                    xp = (cfUser.rating ?: 1200) / 2,
                    level = ((cfUser.rating ?: 1200) / 300).coerceAtLeast(1),
                    streakDays = 7,
                    lastActiveDate = "2026-09-10"
                )
                appDao.saveUserProfile(currentProfile)
                Result.success("تم ربط الحساب بنجاح! التقييم الرسمي: ${cfUser.rating}")
            } else {
                Result.failure(Exception("لم يتم العثور على الحساب في Codeforces"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getDailyChallenge(userRating: Int): DailyChallenge {
        // Personalize daily challenge to user rating
        val targetRating = when {
            userRating < 1000 -> 800
            userRating < 1300 -> 900
            userRating < 1500 -> 1200
            else -> 1400
        }
        val problem = allProblems.firstOrNull { it.rating >= targetRating } ?: allProblems.first()
        return DailyChallenge(
            id = "daily_${problem.id}",
            dateString = "اليوم - تحدي الذكاء اليومي",
            problemId = problem.id,
            targetLevelAr = problem.difficultyLevelAr,
            bonusXp = 150,
            isCompleted = false
        )
    }

    fun getCommunityLeaderboard(userRating: Int, solvedCount: Int): List<LeaderboardUser> {
        val baseUsers = listOf(
            LeaderboardUser(1, "أحمد السوري", "Tourist_Arabia", 2450, 412, 12500, "Grandmaster 🏆", "🇸🇾"),
            LeaderboardUser(2, "عمر المصري", "CairoCoder_99", 2180, 340, 9800, "Master ⚡", "🇪🇬"),
            LeaderboardUser(3, "سارة السعودية", "Riyadh_Byte", 1950, 280, 8100, "Candidate Master 💎", "🇸🇦"),
            LeaderboardUser(4, "يوسف الأردني", "Amman_Binary", 1720, 210, 6400, "Expert 🔥", "🇯🇴"),
            LeaderboardUser(5, "نور المغربية", "Atlas_Algo", 1580, 175, 4900, "Specialist 🚀", "🇲🇦"),
            LeaderboardUser(6, "أنت (الحساب الحالي)", "Codeforces_Hero", userRating, solvedCount, userRating / 2 + solvedCount * 50, "متحدي صاعد ✨", "🇸🇦", isCurrentUser = true),
            LeaderboardUser(7, "طارق العراقي", "Baghdad_Loop", 1210, 110, 3200, "Pupil 🌱", "🇮🇶"),
            LeaderboardUser(8, "خالد الجزائري", "Oran_Code", 1090, 85, 2400, "Newbie 📘", "🇩🇿"),
            LeaderboardUser(9, "محمود التونسي", "Carthage_Dev", 980, 62, 1800, "Newbie 📘", "🇹🇳")
        )
        return baseUsers.sortedByDescending { it.rating }
            .mapIndexed { index, user -> user.copy(rank = index + 1) }
    }
}
