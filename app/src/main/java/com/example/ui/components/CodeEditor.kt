package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onResetTemplate: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    // C++ Library snippets
    val librarySnippets = listOf(
        "مكتبة شاملة" to "#include <bits/stdc++.h>\n",
        "iostream" to "#include <iostream>\n",
        "vector" to "#include <vector>\n",
        "algorithm" to "#include <algorithm>\n",
        "map & set" to "#include <map>\n#include <set>\n",
        "queue & stack" to "#include <queue>\n#include <stack>\n",
        "cmath" to "#include <cmath>\n",
        "Fast I/O" to "    ios_base::sync_with_stdio(false);\n    cin.tie(NULL);\n",
        "قالب رئيسي كامل" to """
#include <bits/stdc++.h>
using namespace std;

#define ll long long
#define all(v) (v).begin(), (v).end()

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    // اكتب حلك هنا
    

    return 0;
}
        """.trimIndent()
    )

    // Quick symbol toolbar
    val symbols = listOf("{", "}", "(", ")", "[", "]", ";", "<<", ">>", "=", "==", "!=", "<", ">", "+", "-", "*", "/", "%", "&&", "||", "->", "cin >>", "cout <<", "\\n", "    ")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(EditorBackground, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // 1. Editor Header (Actions & Language indicator)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = BrandBlueDark,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "C++20 (GNU G++)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${code.lines().size} سطور",
                    color = EditorGutterText,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Row {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(code))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ الكود",
                        tint = EditorText,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onResetTemplate,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة ضبط القالب",
                        tint = BrandCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { onCodeChange("") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "مسح الكود",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // 2. C++ Libraries Insertion Bar (Horizontal Scroll)
        Text(
            text = "إدراج مكتبات وقوالب C++ التنافسية بضغطة واحدة:",
            color = EditorGutterText,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            librarySnippets.forEach { (label, snippet) ->
                SuggestionChip(
                    onClick = {
                        val newCode = if (snippet.startsWith("#include") && !code.contains(snippet.trim())) {
                            snippet + code
                        } else if (label == "قالب رئيسي كامل") {
                            snippet
                        } else {
                            code + "\n" + snippet
                        }
                        onCodeChange(newCode)
                    },
                    label = {
                        Text(
                            text = "+ $label",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = EditorType
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = EditorGutter
                    ),
                    border = null,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        // 3. Quick Symbol Bar & 4. Code Canvas with Line Numbers (in LTR)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .background(EditorGutter, RoundedCornerShape(6.dp))
                        .padding(horizontal = 4.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    symbols.forEach { sym ->
                        Surface(
                            onClick = {
                                onCodeChange(code + sym)
                            },
                            color = EditorBackground,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = if (sym == "    ") "TAB" else sym,
                                    color = BrandCyan,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 4. Code Canvas with Line Numbers
                val lineCount = code.lines().size.coerceAtLeast(1)
                val lineNumbersText = (1..lineCount).joinToString("\n")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 220.dp, max = 380.dp)
                        .background(EditorBackground, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Line numbers gutter
                        Text(
                            text = lineNumbersText,
                            color = EditorGutterText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .background(EditorGutter, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                .widthIn(min = 28.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Editable code text field with syntax coloring transformation
                        BasicTextField(
                            value = code,
                            onValueChange = onCodeChange,
                            textStyle = TextStyle(
                                color = EditorText,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(BrandCyan),
                            visualTransformation = CppSyntaxVisualTransformation(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * VisualTransformation that provides rich C++ syntax highlighting in real-time.
 */
class CppSyntaxVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val annotated = buildAnnotatedString {
            append(raw)

            // 1. Highlight #include and preprocessors
            val includeRegex = Regex("(#include\\s*<[^>]+>|#define\\s+\\w+)")
            includeRegex.findAll(raw).forEach { match ->
                addStyle(
                    SpanStyle(color = EditorInclude, fontWeight = FontWeight.Bold),
                    match.range.first,
                    match.range.last + 1
                )
            }

            // 2. C++ Keywords
            val keywords = listOf(
                "int", "long", "void", "char", "bool", "double", "float", "auto",
                "return", "if", "else", "for", "while", "do", "break", "continue",
                "struct", "class", "public", "private", "using", "namespace", "std",
                "cin", "cout", "vector", "string", "map", "set", "queue", "stack",
                "pair", "sort", "reverse", "min", "max", "true", "false"
            )
            val keywordsRegex = Regex("\\b(" + keywords.joinToString("|") + ")\\b")
            keywordsRegex.findAll(raw).forEach { match ->
                val word = match.value
                val color = when (word) {
                    "int", "long", "void", "bool", "char", "double", "float", "auto" -> EditorType
                    "cin", "cout" -> BrandCyan
                    "vector", "string", "map", "set", "queue", "stack", "pair" -> EditorKeyword
                    else -> EditorKeyword
                }
                addStyle(
                    SpanStyle(color = color, fontWeight = FontWeight.Bold),
                    match.range.first,
                    match.range.last + 1
                )
            }

            // 3. String literals "..."
            val stringRegex = Regex("\"(\\\\.|[^\"])*\"")
            stringRegex.findAll(raw).forEach { match ->
                addStyle(
                    SpanStyle(color = EditorString),
                    match.range.first,
                    match.range.last + 1
                )
            }

            // 4. Numbers
            val numberRegex = Regex("\\b\\d+\\b")
            numberRegex.findAll(raw).forEach { match ->
                addStyle(
                    SpanStyle(color = EditorNumber),
                    match.range.first,
                    match.range.last + 1
                )
            }

            // 5. Comments // ...
            val commentRegex = Regex("//.*")
            commentRegex.findAll(raw).forEach { match ->
                addStyle(
                    SpanStyle(color = EditorComment, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    match.range.first,
                    match.range.last + 1
                )
            }
        }
        return TransformedText(annotated, OffsetMapping.Identity)
    }
}
