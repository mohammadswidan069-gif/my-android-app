package com.example.ai

import com.example.BuildConfig
import com.example.data.model.Problem
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

sealed class AiResponse {
    data class Success(val text: String) : AiResponse()
    data class Error(val message: String) : AiResponse()
}

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    private val model = "gemini-3.5-flash"

    suspend fun analyzeAndFixCode(
        code: String,
        problem: Problem,
        userErrorDescription: String = ""
    ): AiResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        val prompt = """
أنت خبير ومدرب أولمبياد المعلوماتية والبرمجة التنافسية (Competitive Programming) في Codeforces.
المسألة: ${problem.id} - ${problem.titleAr} (${problem.titleEn})
التقييم (Rating): ${problem.rating}
نص المسألة:
${problem.statementAr}
قيود الإدخال:
${problem.inputAr}

كود المتسابق الحالي (C++):
```cpp
$code
```
${if (userErrorDescription.isNotBlank()) "ملاحظة أو نتيجة الخطأ: $userErrorDescription" else ""}

المطلوب بدقة وباللغة العربية الفصحى الواضحة والملهمة:
1. تشخيص الخطأ أو سبب الرفض (Wrong Answer أو TLE أو Compilation Error أو Runtime Error أو Overflow).
2. خطوات التفكير المنطقي الصحيحة لحل المسألة وما فات المتسابق.
3. كود C++ مصحح ومثالي مع تعليقات عربية توضيحية.
4. تحليل التعقيد الزمني والمكاني (Big-O) للحل المقترح.
        """.trimIndent()

        callGemini(prompt, apiKey)
    }

    suspend fun suggestPerformanceOptimizations(
        code: String,
        problem: Problem
    ): AiResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        val prompt = """
أنت مستشار تحسين الأداء (Performance & Complexity Optimization) للبرمجة التنافسية في Codeforces.
مسألة: ${problem.id} - ${problem.titleAr} (Rating: ${problem.rating})
الحد الزمني: ${problem.timeLimit}، حد الذاكرة: ${problem.memoryLimit}

كود C++ الحالي:
```cpp
$code
```

المطلوب باللغة العربية:
1. قياس التعقيد الزمني (Time Complexity) والمكاني (Space Complexity) للكود الحالي.
2. هل الكود الحالي معرض لخطر TLE (تجاوز الحد الزمني) عند الحالات القصوى (Worst Case)؟ ولماذا؟
3. هل توجد فخاخ شائعة (مثل Integer Overflow، بطء I/O، أو تخصيص زائد للذاكرة)؟
4. تقديم 3 نصائح عملية ومحددة لتسريع الكود واستخدام دوال ومكتبات STL المثلى (مثل unordered_map مقابل map، أو priority_queue، أو bitwise operations).
        """.trimIndent()

        callGemini(prompt, apiKey)
    }

    suspend fun getStepByStepHint(
        problem: Problem,
        hintLevel: Int // 1: تلميح بسيط، 2: فكرة الخوارزمية، 3: شبه الحل
    ): AiResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        val hintType = when (hintLevel) {
            1 -> "تلميح ذكي خفيف لتحفيز التفكير دون حرق الفكرة إطلاقاً"
            2 -> "شرح الخوارزمية أو القاعدة الرياضية العامة المناسبة دون إعطاء الكود"
            else -> "توضيح الخطوات شبه النهائية وهيكل كود C++ المطلوب"
        }

        val prompt = """
أنت مدرب برمجة تنافسية تشجع المتدرب على الوصول للحل بنفسه.
المسألة: ${problem.id} - ${problem.titleAr}
المطلوب تقديم: $hintType.
باللغة العربية بأسلوب مشجع وتعليمي.
        """.trimIndent()

        callGemini(prompt, apiKey)
    }

    private fun callGemini(prompt: String, apiKey: String): AiResponse {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Fallback smart response if API key is not configured yet
            return AiResponse.Success(
                """
ℹ️ [تحليل الذكاء الاصطناعي الذكي]:
• يمكنك إضافة مفتاح Gemini API الخاص بك من لوحة Secrets في AI Studio لتفعيل الإجابات الفورية التوليدية.
• التحليل المنطقي السريع لمسألتك:
1. تأكد دائماً من مطابقة قيود المسألة لنوع البيانات (استخدم long long إذا كانت القيم تصل لـ 10^9 وتتضمن ضرباً).
2. استخدم Fast I/O:
   ios_base::sync_with_stdio(false);
   cin.tie(NULL);
3. لا تطبع endl داخل الحلقات واستبدلها بـ '\n'.
4. راجع تبويب 'خطوات التفكير المنطقي' في المسألة للاطلاع على الشرح الرياضي والخوارزمي الشامل!
                """.trimIndent()
            )
        }

        return try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return AiResponse.Error("خطأ في الاتصال بالذكاء الاصطناعي: كود ${response.code}")
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val generatedText = parts?.optJSONObject(0)?.optString("text")

            if (!generatedText.isNullOrBlank()) {
                AiResponse.Success(generatedText)
            } else {
                AiResponse.Error("لم يتم استلام نص من النموذج.")
            }
        } catch (e: Exception) {
            AiResponse.Error("تعذر الاتصال بخدمة الذكاء الاصطناعي: ${e.localizedMessage}")
        }
    }
}
