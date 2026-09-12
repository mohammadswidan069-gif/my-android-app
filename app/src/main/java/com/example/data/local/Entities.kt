package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "problem_progress")
data class ProblemProgressEntity(
    @PrimaryKey val problemId: String,
    val isSolved: Boolean = false,
    val solvedAt: Long = 0L,
    val userCode: String = "",
    val attemptsCount: Int = 0,
    val notes: String = ""
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val handle: String = "CodeMaster_Ar",
    val name: String = "المبرمج العربي",
    val rating: Int = 1240,
    val maxRating: Int = 1310,
    val xp: Int = 850,
    val level: Int = 4,
    val streakDays: Int = 5,
    val lastActiveDate: String = "2026-09-10"
)
