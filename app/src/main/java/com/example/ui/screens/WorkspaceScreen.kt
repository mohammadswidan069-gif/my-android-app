package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.CodeDiagnostic
import com.example.ai.DiagnosticType
import com.example.data.model.Problem
import com.example.data.model.TestExecutionResult
import com.example.ui.components.CodeEditor
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    problem: Problem,
    code: String,
    onCodeChange: (String) -> Unit,
    activeTab: Int,
    onTabChange: (Int) -> Unit,
    testResults: List<TestExecutionResult>,
    isRunningTests: Boolean,
    onRunTests: () -> Unit,
    staticDiagnostics: List<CodeDiagnostic>,
    aiResponseText: String,
    isAiLoading: Boolean,
    onRequestAiDiagnosis: () -> Unit,
    onRequestAiOptimization: () -> Unit,
    onRequestAiHint: (Int) -> Unit,
    onLoadModelSolution: () -> Unit,
    onResetCode: () -> Unit,
    customInput: String,
    onCustomInputChange: (String) -> Unit,
    customOutput: String,
    isCustomTestPassed: Boolean?,
    onRunCustomTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ratingColor = when {
        problem.rating < 1000 -> CfNewbie
        problem.rating < 1200 -> CfPupil
        problem.rating < 1400 -> CfSpecialist
        problem.rating < 1600 -> CfExpert
        else -> CfCandidateMaster
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Problem Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = ratingColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${problem.id}",
                                fontWeight = FontWeight.Bold,
                                color = ratingColor,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = problem.titleAr,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${problem.titleEn} • ${problem.difficultyLevelAr}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Rating badge
                    Surface(
                        color = ratingColor,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "★ ${problem.rating}",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time and memory limits
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "⏱️ الحد الزمني: ${problem.timeLimit}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "💾 حد الذاكرة: ${problem.memoryLimit}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 2. Navigation Tabs
        PrimaryTabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { onTabChange(0) },
                text = { Text("نص المسألة والمنطق", fontSize = 13.sp) },
                icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { onTabChange(1) },
                text = { Text("محرر C++", fontSize = 13.sp) },
                icon = { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { onTabChange(2) },
                text = { Text("الاختبارات والـ AI", fontSize = 13.sp) },
                icon = { Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        // 3. Tab Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (activeTab) {
                0 -> ProblemStatementAndLogicView(
                    problem = problem,
                    onOpenEditor = { onTabChange(1) },
                    onLoadSolution = {
                        onLoadModelSolution()
                        onTabChange(1)
                    }
                )

                1 -> CppEditorView(
                    code = code,
                    onCodeChange = onCodeChange,
                    onResetCode = onResetCode,
                    onLoadModelSolution = onLoadModelSolution,
                    staticDiagnostics = staticDiagnostics,
                    onRunTests = onRunTests,
                    onRequestAiDiagnosis = onRequestAiDiagnosis,
                    isRunningTests = isRunningTests
                )

                2 -> TestsAndAiAssistantView(
                    problem = problem,
                    testResults = testResults,
                    isRunningTests = isRunningTests,
                    onRunTests = onRunTests,
                    staticDiagnostics = staticDiagnostics,
                    aiResponseText = aiResponseText,
                    isAiLoading = isAiLoading,
                    onRequestAiDiagnosis = onRequestAiDiagnosis,
                    onRequestAiOptimization = onRequestAiOptimization,
                    onRequestAiHint = onRequestAiHint,
                    customInput = customInput,
                    onCustomInputChange = onCustomInputChange,
                    customOutput = customOutput,
                    onRunCustomTest = onRunCustomTest
                )
            }
        }
    }
}

@Composable
fun ProblemStatementAndLogicView(
    problem: Problem,
    onOpenEditor: () -> Unit,
    onLoadSolution: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Arabic Problem Statement
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "نص المسألة بالعربية",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = problem.statementAr,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 24.sp
                )
            }
        }

        // Inputs & Outputs format
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📥 مواصفات الإدخال (Input)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problem.inputAr,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "📤 مواصفات الإخراج (Output)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problem.outputAr,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
            }
        }

        // Detailed Logical Thinking and Algorithm Steps (The core feature requested!)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BrandCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = BrandCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "خطوات التفكير المنطقي وتحليل الحل",
                            color = BrandCyan,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        color = BrandCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "شرح تفصيلي",
                            color = BrandCyan,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = problem.solutionLogicAr,
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onLoadSolution,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تحميل كود الحل النموذجي", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onOpenEditor,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("انتقل للتكويد", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Sample Test Cases Preview
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🧪 أمثلة توضيحية (Sample Tests)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                problem.testCases.forEach { tc ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "مثال #${tc.id}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                if (tc.explanation.isNotBlank()) {
                                    Text(text = tc.explanation, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "المدخل: ${tc.input}", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            Text(text = "المخرج: ${tc.expectedOutput}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CppEditorView(
    code: String,
    onCodeChange: (String) -> Unit,
    onResetCode: () -> Unit,
    onLoadModelSolution: () -> Unit,
    staticDiagnostics: List<CodeDiagnostic>,
    onRunTests: () -> Unit,
    onRequestAiDiagnosis: () -> Unit,
    isRunningTests: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // Quick Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRunTests,
                enabled = !isRunningTests,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (isRunningTests) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تشغيل الاختبارات", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            FilledTonalButton(
                onClick = onRequestAiDiagnosis,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = BrandCyan.copy(alpha = 0.15f)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = BrandCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("تصحيح تلقائي بالـ AI", color = BrandCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Full acodeasst-inspired C++ Code Editor
        CodeEditor(
            code = code,
            onCodeChange = onCodeChange,
            onResetTemplate = onResetCode,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Static Diagnostics Summary Bar
        val errors = staticDiagnostics.filter { it.type == DiagnosticType.ERROR }
        val warnings = staticDiagnostics.filter { it.type == DiagnosticType.WARNING }

        if (errors.isNotEmpty() || warnings.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (errors.isNotEmpty()) Color(0xFF7F1D1D) else Color(0xFF78350F)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    val firstDiag = errors.firstOrNull() ?: warnings.first()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (errors.isNotEmpty()) Icons.Default.Error else Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = firstDiag.titleAr,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = firstDiag.messageAr,
                        color = Color(0xFFF1F5F9),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "💡 نصيحة: ${firstDiag.suggestionAr}",
                        color = BrandAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Model Solution Helper
        OutlinedButton(
            onClick = onLoadModelSolution,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("هل تشعر بالتعثر؟ حمّل الحل النموذجي المصحوب بالشرح الكامل", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TestsAndAiAssistantView(
    problem: Problem,
    testResults: List<TestExecutionResult>,
    isRunningTests: Boolean,
    onRunTests: () -> Unit,
    staticDiagnostics: List<CodeDiagnostic>,
    aiResponseText: String,
    isAiLoading: Boolean,
    onRequestAiDiagnosis: () -> Unit,
    onRequestAiOptimization: () -> Unit,
    onRequestAiHint: (Int) -> Unit,
    customInput: String,
    onCustomInputChange: (String) -> Unit,
    customOutput: String,
    onRunCustomTest: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // AI Control Header
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color(0xFF818CF8))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "نظام الذكاء الاصطناعي لتصحيح وتحسين الكود",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "يساعدك على اكتشاف الأخطاء البرمجية تلقائياً، واقتراح تعقيد زمني مثالي، وتجنب فخاخ TLE و Overflow.",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // AI Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = onRequestAiDiagnosis,
                        enabled = !isAiLoading,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("تصحيح الأخطاء", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    FilledTonalButton(
                        onClick = onRequestAiOptimization,
                        enabled = !isAiLoading,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("تحسين الأداء", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { onRequestAiHint(1) },
                        enabled = !isAiLoading,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(0.9f)
                    ) {
                        Text("تلميح", fontSize = 11.sp)
                    }
                }
            }
        }

        // AI Response Output (if available or loading)
        if (isAiLoading) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("جاري فحص وتحليل كود C++ بواسطة الذكاء الاصطناعي...", fontSize = 13.sp)
                }
            }
        } else if (aiResponseText.isNotBlank()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF38BDF8), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF38BDF8))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تقرير التحليل والتصحيح:",
                                color = Color(0xFF38BDF8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        IconButton(
                            onClick = { clipboardManager.setText(AnnotatedString(aiResponseText)) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = aiResponseText,
                        color = Color(0xFFF1F5F9),
                        fontSize = 13.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Test Cases Results
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نتائج الاختبارات التجريبية (${testResults.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Button(
                        onClick = onRunTests,
                        enabled = !isRunningTests,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تشغيل الكل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (testResults.isEmpty()) {
                    Text(
                        text = "اضغط على 'تشغيل الكل' لاختبار الكود مقابل حالات الاختبار الرسمية.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                } else {
                    testResults.forEach { res ->
                        Surface(
                            color = if (res.isPassed) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFFEF4444).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (res.isPassed) Color(0xFF10B981).copy(alpha = 0.4f) else Color(0xFFEF4444).copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "اختبار #${res.testCaseId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )

                                    Surface(
                                        color = if (res.isPassed) Color(0xFF10B981) else Color(0xFFEF4444),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (res.isPassed) "ACCEPTED ✓" else "WRONG ANSWER ✗",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "المدخل: ${res.input}", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                Text(text = "المتوقع: ${res.expectedOutput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color(0xFF10B981))
                                Text(text = "الفعلي: ${res.actualOutput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = if (res.isPassed) Color(0xFF10B981) else Color(0xFFEF4444))

                                if (res.errorMessage != null) {
                                    Text(
                                        text = "⚠️ ${res.errorMessage}",
                                        color = Color(0xFFEF4444),
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Test Runner (User entered inputs)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🧪 تجربة مدخلات مخصصة (Custom Input)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customInput,
                    onValueChange = onCustomInputChange,
                    label = { Text("المدخلات المخصصة") },
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRunCustomTest,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("تنفيذ على المدخل المخصص", fontSize = 12.sp)
                }

                if (customOutput.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFF1E1E2E),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "المخرجات الناتجة:", color = BrandCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = customOutput, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
