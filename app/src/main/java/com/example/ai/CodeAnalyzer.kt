package com.example.ai

import com.example.data.model.Problem

data class CodeDiagnostic(
    val type: DiagnosticType,
    val titleAr: String,
    val messageAr: String,
    val suggestionAr: String,
    val lineNumber: Int? = null
)

enum class DiagnosticType {
    ERROR,
    WARNING,
    OPTIMIZATION,
    INFO
}

object CodeAnalyzer {

    fun analyzeCpp(code: String, problem: Problem?): List<CodeDiagnostic> {
        val diagnostics = mutableListOf<CodeDiagnostic>()

        if (code.isBlank()) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "المحرر فارغ",
                    messageAr = "لم تقم بكتابة أي كود C++ حتى الآن.",
                    suggestionAr = "اضغط على زر 'قالب C++' لإدراج الهيكل الأساسي."
                )
            )
            return diagnostics
        }

        // 1. Check main function
        if (!code.contains("int main") && !code.contains("void main")) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "دالة main مفقودة",
                    messageAr = "كل برنامج C++ يجب أن يحتوي على دالة int main() كنقطة بداية للتنفيذ.",
                    suggestionAr = "أضف int main() { ... return 0; }"
                )
            )
        }

        // 2. Check brackets balance
        var openBrace = 0
        var openParen = 0
        for (char in code) {
            when (char) {
                '{' -> openBrace++
                '}' -> openBrace--
                '(' -> openParen++
                ')' -> openParen--
            }
        }
        if (openBrace > 0) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "أقواس معقوفة غير مغلقة { }",
                    messageAr = "يوجد $openBrace قوس معقوف مفتوح لم يتم إغلاقه '}'.",
                    suggestionAr = "تأكد من إغلاق كافة كتل الشيفرة البرمجية بالأقواس المقابلة."
                )
            )
        } else if (openBrace < 0) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "أقواس معقوفة زائدة",
                    messageAr = "يوجد إغلاق زائد للأقواس المعقوفة '}'.",
                    suggestionAr = "احذف القوس الإضافي الزائد."
                )
            )
        }

        if (openParen != 0) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "عدم تطابق الأقواس الدائرية ( )",
                    messageAr = "الأقواس الدائرية غير متوازنة، تحقق من شروط if أو حلقات for.",
                    suggestionAr = "راجع أسطر الشروط والحلقات وتأكد من إغلاق جميع الأقواس."
                )
            )
        }

        // 3. Check for Fast I/O
        val hasFastIo = code.contains("ios_base::sync_with_stdio(false)") || code.contains("cin.tie")
        if (!hasFastIo) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.WARNING,
                    titleAr = "تسريع الإدخال والإخراج مفقود (Fast I/O)",
                    messageAr = "في مسائل البرمجة التنافسية، عدم تسريع الإدخال قد يسبب بطء التنفيذ Time Limit Exceeded (TLE).",
                    suggestionAr = "أضف ios_base::sync_with_stdio(false); cin.tie(NULL); في بداية main()."
                )
            )
        }

        // 4. Check for endl in loops
        if (code.contains("endl") && (code.contains("for") || code.contains("while"))) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.OPTIMIZATION,
                    titleAr = "استخدام endl داخل حلقة تكرارية",
                    messageAr = "استخدام std::endl يجبر النظام على تفريغ الـ buffer في كل دورة مما يبطئ الكود بشكل كبير.",
                    suggestionAr = "استبدل endl بالرمز '\\n' لطباعة سطر جديد بسرعة عالية."
                )
            )
        }

        // 5. Check for Integer Overflow (Classic Codeforces Trap)
        val problemRating = problem?.rating ?: 1000
        val needsLongLong = problem?.id in listOf("1A", "1328A", "1352C", "466C") ||
                code.contains("1000000000") || code.contains("1e9") || code.contains("1e18")
        if (needsLongLong && !code.contains("long long") && !code.contains("ll")) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.WARNING,
                    titleAr = "تحذير: احتمال حدوث Integer Overflow!",
                    messageAr = "قيود هذه المسألة تصل إلى 10^9 أو تتضمن عمليات ضرب/جمع تراكمي تتجاوز سعة int (2 × 10^9).",
                    suggestionAr = "استخدم نوع البيانات long long لمتغيرات الأبعاد والمجموع لتجنب الأرقام العشوائية السالبة."
                )
            )
        }

        // 6. Check for vector / header inclusion
        if (code.contains("vector<") && !code.contains("<vector>") && !code.contains("<bits/stdc++.h>")) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.ERROR,
                    titleAr = "مكتبة vector غير مدرجة",
                    messageAr = "أنت تستخدم vector دون استدعاء المكتبة الخاصة به.",
                    suggestionAr = "أضف #include <vector> أو #include <bits/stdc++.h> في بداية الملف."
                )
            )
        }

        // If no major errors
        if (diagnostics.none { it.type == DiagnosticType.ERROR }) {
            diagnostics.add(
                CodeDiagnostic(
                    type = DiagnosticType.INFO,
                    titleAr = "البنية البرمجية الأساسية سليمة",
                    messageAr = "تم التحقق من الأقواس، دالة main، والمكتبات المستدعاة. الكود جاهز للاختبار التجريبي.",
                    suggestionAr = "اضغط على 'تشغيل الاختبارات التجريبية' للتحقق من دقة النتائج."
                )
            )
        }

        return diagnostics
    }
}
