package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM problem_progress")
    fun getAllProgress(): Flow<List<ProblemProgressEntity>>

    @Query("SELECT * FROM problem_progress WHERE problemId = :problemId LIMIT 1")
    suspend fun getProgressForProblem(problemId: String): ProblemProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: ProblemProgressEntity)

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET xp = xp + :gainedXp, rating = rating + :ratingDelta, streakDays = :streak WHERE id = 1")
    suspend fun awardXpAndRating(gainedXp: Int, ratingDelta: Int, streak: Int)
}
