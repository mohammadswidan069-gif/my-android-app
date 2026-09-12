package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiResponse
import com.example.ai.CodeAnalyzer
import com.example.ai.CodeDiagnostic
import com.example.ai.GeminiService
import com.example.data.local.AppDatabase
import com.example.data.local.ProblemProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.DailyChallenge
import com.example.data.model.LeaderboardUser
import com.example.data.model.Lesson
import com.example.data.model.Problem
import com.example.data.model.TestCase
import com.example.data.model.TestExecutionResult
import com.example.data.repository.ProblemRepository
import com.example.engine.TestRunner
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val selectedProblem: Problem,
    val currentCode: String = "",
    val activeWorkspaceTab: Int = 1, // 0: المسألة والمنطق، 1: محرر C++، 2: الاختبارات والـ AI
    val testResults: List<TestExecutionResult> = emptyList(),
    val isRunningTests: Boolean = false,
    val staticDiagnostics: List<CodeDiagnostic> = emptyList(),
    val aiResponseText: String = "",
    val isAiLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedRatingFilter: Int? = null,
    val selectedTagFilter: String? = null,
    val customInput: String = "",
    val customOutput: String = "",
    val isCustomTestPassed: Boolean? = null,
    val xpRewardMessage: String? = null,
    val cfHandleInput: String = "",
    val cfLinkStatus: String? = null,
    val isCfLinking: Boolean = false,
    val selectedLesson: Lesson? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ProblemRepository(db.appDao())
    private val geminiService = GeminiService()

    val allProblems: List<Problem> = repository.allProblems
    val allLessons: List<Lesson> = repository.allLessons

    private val _uiState = MutableStateFlow(
        UiState(
            selectedProblem = allProblems.first(),
            currentCode = allProblems.first().starterCode
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val progressMap: StateFlow<Map<String, ProblemProgressEntity>> = repository.progressFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            repository.initializeDefaultUser()
            loadProblem(allProblems.first())
        }
    }

    fun selectProblem(problem: Problem) {
        viewModelScope.launch {
            loadProblem(problem)
        }
    }

    private suspend fun loadProblem(problem: Problem) {
        val saved = repository.getProgress(problem.id)
        val codeToUse = if (!saved?.userCode.isNullOrBlank()) {
            saved!!.userCode
        } else {
            problem.starterCode
        }

        _uiState.update {
            it.copy(
                selectedProblem = problem,
                currentCode = codeToUse,
                testResults = emptyList(),
                staticDiagnostics = CodeAnalyzer.analyzeCpp(codeToUse, problem),
                aiResponseText = "",
                customInput = problem.testCases.firstOrNull()?.input ?: "",
                customOutput = ""
            )
        }
    }

    fun onCodeChanged(newCode: String) {
        val currentProblem = _uiState.value.selectedProblem
        _uiState.update {
            it.copy(
                currentCode = newCode,
                staticDiagnostics = CodeAnalyzer.analyzeCpp(newCode, currentProblem)
            )
        }
        viewModelScope.launch {
            repository.saveCode(currentProblem.id, newCode)
        }
    }

    fun resetToStarterCode() {
        val problem = _uiState.value.selectedProblem
        onCodeChanged(problem.starterCode)
    }

    fun loadModelSolution() {
        val problem = _uiState.value.selectedProblem
        onCodeChanged(problem.cppModelSolution)
    }

    fun setWorkspaceTab(index: Int) {
        _uiState.update { it.copy(activeWorkspaceTab = index) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setRatingFilter(rating: Int?) {
        _uiState.update { it.copy(selectedRatingFilter = rating) }
    }

    fun setTagFilter(tag: String?) {
        _uiState.update { it.copy(selectedTagFilter = tag) }
    }

    fun setCustomInput(input: String) {
        _uiState.update { it.copy(customInput = input) }
    }

    fun selectLesson(lesson: Lesson?) {
        _uiState.update { it.copy(selectedLesson = lesson) }
    }

    fun clearRewardMessage() {
        _uiState.update { it.copy(xpRewardMessage = null) }
    }

    fun setCfHandleInput(handle: String) {
        _uiState.update { it.copy(cfHandleInput = handle) }
    }

    // Run official test cases
    fun runAllTests() {
        val state = _uiState.value
        _uiState.update { it.copy(isRunningTests = true) }

        viewModelScope.launch {
            val results = TestRunner.runTests(state.currentCode, state.selectedProblem)
            val allPassed = results.isNotEmpty() && results.all { it.isPassed }

            var xpGain = 0
            if (allPassed) {
                xpGain = repository.markProblemSolved(
                    state.selectedProblem.id,
                    state.currentCode,
                    state.selectedProblem.rating
                )
            }

            _uiState.update {
                it.copy(
                    testResults = results,
                    isRunningTests = false,
                    xpRewardMessage = if (allPassed && xpGain > 0) {
                        "🎉 إجابة صحيحة Accepted! ربحت +$xpGain نقطة خبرة XP وارتفع تقييمك!"
                    } else if (allPassed) {
                        "✅ تم اجتياز جميع الاختبارات بنجاح!"
                    } else null
                )
            }
        }
    }

    // Run custom user test case
    fun runCustomTest() {
        val state = _uiState.value
        val customCase = TestCase(
            id = 999,
            input = state.customInput,
            expectedOutput = "",
            explanation = "اختبار مخصص من المستخدم"
        )
        viewModelScope.launch {
            val results = TestRunner.runTests(state.currentCode, state.selectedProblem, customCase)
            val result = results.firstOrNull()
            _uiState.update {
                it.copy(
                    customOutput = result?.actualOutput ?: "No output",
                    isCustomTestPassed = result?.isPassed
                )
            }
        }
    }

    // AI Diagnostics & Auto-fix
    fun requestAiDiagnosis() {
        val state = _uiState.value
        _uiState.update { it.copy(isAiLoading = true, activeWorkspaceTab = 2) }

        viewModelScope.launch {
            val failedTest = state.testResults.firstOrNull { !it.isPassed }
            val errorDescription = if (failedTest != null) {
                "المدخل: ${failedTest.input}\nالمخرج المتوقع: ${failedTest.expectedOutput}\nالمخرج الفعلي: ${failedTest.actualOutput}\nالخطأ: ${failedTest.errorMessage ?: ""}"
            } else ""

            val response = geminiService.analyzeAndFixCode(
                code = state.currentCode,
                problem = state.selectedProblem,
                userErrorDescription = errorDescription
            )

            _uiState.update {
                it.copy(
                    isAiLoading = false,
                    aiResponseText = when (response) {
                        is AiResponse.Success -> response.text
                        is AiResponse.Error -> "عذراً: ${response.message}"
                    }
                )
            }
        }
    }

    // AI Performance Optimization
    fun requestAiOptimization() {
        val state = _uiState.value
        _uiState.update { it.copy(isAiLoading = true, activeWorkspaceTab = 2) }

        viewModelScope.launch {
            val response = geminiService.suggestPerformanceOptimizations(
                code = state.currentCode,
                problem = state.selectedProblem
            )

            _uiState.update {
                it.copy(
                    isAiLoading = false,
                    aiResponseText = when (response) {
                        is AiResponse.Success -> response.text
                        is AiResponse.Error -> "عذراً: ${response.message}"
                    }
                )
            }
        }
    }

    // AI Step by Step Hint
    fun requestAiHint(level: Int) {
        val state = _uiState.value
        _uiState.update { it.copy(isAiLoading = true, activeWorkspaceTab = 2) }

        viewModelScope.launch {
            val response = geminiService.getStepByStepHint(
                problem = state.selectedProblem,
                hintLevel = level
            )

            _uiState.update {
                it.copy(
                    isAiLoading = false,
                    aiResponseText = when (response) {
                        is AiResponse.Success -> response.text
                        is AiResponse.Error -> "عذراً: ${response.message}"
                    }
                )
            }
        }
    }

    // Codeforces Handle Linking
    fun linkCodeforcesAccount() {
        val handle = _uiState.value.cfHandleInput
        if (handle.isBlank()) return

        _uiState.update { it.copy(isCfLinking = true, cfLinkStatus = null) }
        viewModelScope.launch {
            val result = repository.linkCodeforcesHandle(handle)
            result.onSuccess { msg ->
                _uiState.update { it.copy(isCfLinking = false, cfLinkStatus = msg) }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isCfLinking = false,
                        cfLinkStatus = "فشل الربط: ${err.localizedMessage}"
                    )
                }
            }
        }
    }

    fun getDailyChallenge(): DailyChallenge {
        val rating = userProfile.value?.rating ?: 1200
        return repository.getDailyChallenge(rating)
    }

    fun getLeaderboard(): List<LeaderboardUser> {
        val rating = userProfile.value?.rating ?: 1240
        val solvedCount = progressMap.value.values.count { it.isSolved }
        return repository.getCommunityLeaderboard(rating, solvedCount)
    }
}
