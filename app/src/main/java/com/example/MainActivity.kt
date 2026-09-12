package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.ProblemsScreen
import com.example.ui.screens.ProfileLeaderboardScreen
import com.example.ui.screens.WorkspaceScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CodeforcesArabicApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeforcesArabicApp(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val progressMap by viewModel.progressMap.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableIntStateOf(0) } // 0: Problems, 1: Workspace, 2: Lessons, 3: Leaderboard
    val snackbarHostState = remember { SnackbarHostState() }

    // Show Snackbar on XP reward
    LaunchedEffect(uiState.xpRewardMessage) {
        uiState.xpRewardMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearRewardMessage()
        }
    }

    val rating = userProfile?.rating ?: 1240
    val ratingColor = when {
        rating < 1000 -> CfNewbie
        rating < 1200 -> CfPupil
        rating < 1400 -> CfSpecialist
        rating < 1600 -> CfExpert
        else -> CfCandidateMaster
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = BrandCyan,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "CF",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "كود فورس عربي",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "حل وتصحيح مسائل C++ بالذكاء الاصطناعي",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Rating & Streak chips in TopBar
                    Surface(
                        color = ratingColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "★ $rating",
                                color = ratingColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFFF97316).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔥 ${userProfile?.streakDays ?: 5}d",
                                color = Color(0xFFF97316),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == 0,
                    onClick = { currentScreen = 0 },
                    icon = { Icon(Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = "المسائل") },
                    label = { Text("المسائل", fontSize = 11.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == 1,
                    onClick = { currentScreen = 1 },
                    icon = {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = BrandCyan) {
                                    Text(
                                        text = uiState.selectedProblem.id,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        ) {
                            Icon(Icons.Default.Code, contentDescription = "التكويد والمحرر")
                        }
                    },
                    label = { Text("المحرر الذكي", fontSize = 11.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == 2,
                    onClick = { currentScreen = 2 },
                    icon = { Icon(Icons.Default.School, contentDescription = "المكتبة") },
                    label = { Text("الدروس", fontSize = 11.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == 3,
                    onClick = { currentScreen = 3 },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "المتصدرون") },
                    label = { Text("المتصدرون", fontSize = 11.sp) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                0 -> ProblemsScreen(
                    problems = viewModel.allProblems,
                    progressMap = progressMap,
                    dailyChallenge = viewModel.getDailyChallenge(),
                    selectedProblemId = uiState.selectedProblem.id,
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::setSearchQuery,
                    selectedRating = uiState.selectedRatingFilter,
                    onRatingSelect = viewModel::setRatingFilter,
                    onProblemClick = { problem ->
                        viewModel.selectProblem(problem)
                        currentScreen = 1 // Navigate directly to workspace/editor
                    }
                )

                1 -> WorkspaceScreen(
                    problem = uiState.selectedProblem,
                    code = uiState.currentCode,
                    onCodeChange = viewModel::onCodeChanged,
                    activeTab = uiState.activeWorkspaceTab,
                    onTabChange = viewModel::setWorkspaceTab,
                    testResults = uiState.testResults,
                    isRunningTests = uiState.isRunningTests,
                    onRunTests = viewModel::runAllTests,
                    staticDiagnostics = uiState.staticDiagnostics,
                    aiResponseText = uiState.aiResponseText,
                    isAiLoading = uiState.isAiLoading,
                    onRequestAiDiagnosis = viewModel::requestAiDiagnosis,
                    onRequestAiOptimization = viewModel::requestAiOptimization,
                    onRequestAiHint = viewModel::requestAiHint,
                    onLoadModelSolution = viewModel::loadModelSolution,
                    onResetCode = viewModel::resetToStarterCode,
                    customInput = uiState.customInput,
                    onCustomInputChange = viewModel::setCustomInput,
                    customOutput = uiState.customOutput,
                    isCustomTestPassed = uiState.isCustomTestPassed,
                    onRunCustomTest = viewModel::runCustomTest
                )

                2 -> LessonsScreen(
                    lessons = viewModel.allLessons,
                    selectedLesson = uiState.selectedLesson,
                    onSelectLesson = viewModel::selectLesson,
                    onSolveRelatedProblem = { probId ->
                        val problem = viewModel.allProblems.firstOrNull { it.id == probId }
                        if (problem != null) {
                            viewModel.selectProblem(problem)
                            currentScreen = 1
                        }
                    }
                )

                3 -> ProfileLeaderboardScreen(
                    userProfile = userProfile,
                    solvedCount = progressMap.values.count { it.isSolved },
                    totalProblemsCount = viewModel.allProblems.size,
                    leaderboardUsers = viewModel.getLeaderboard(),
                    cfHandleInput = uiState.cfHandleInput,
                    onCfHandleInputChange = viewModel::setCfHandleInput,
                    onLinkHandle = viewModel::linkCodeforcesAccount,
                    cfLinkStatus = uiState.cfLinkStatus,
                    isCfLinking = uiState.isCfLinking
                )
            }
        }
    }
}
