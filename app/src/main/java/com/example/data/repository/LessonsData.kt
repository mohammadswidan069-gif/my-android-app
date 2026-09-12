package com.example.data.repository

import com.example.data.model.Lesson

object LessonsData {
    val lessons: List<Lesson> = listOf(
        Lesson(
            id = "lesson_fast_io",
            titleAr = "قالب C++ الخارق والإدخال السريع للبرمجة التنافسية",
            titleEn = "Fast I/O and Standard Template Library",
            category = "الأساسيات والقوالب",
            levelAr = "مبتدئ",
            readTimeAr = "7 دقائق",
            summaryAr = "كيف تتجنب أخطاء TLE الناتجة عن بطء cin/cout وكيف تستفيد من مكتبة bits/stdc++.h بفعالية.",
            contentAr = """
لماذا يعتبر الإدخال والإخراج القياسي بطيئاً في C++؟
بشكل افتراضي، يقوم C++ بمزامنة مجاري الإدخال والإخراج مع دوال C القياسية (مثل scanf و printf)، كما يقوم بعملية flush للـ buffer عند كل عملية قراءة بعد كتابة. في مسائل Codeforces التي تحتوي على 10^5 أو 10^6 عنصر، يتسبب هذا في تأخير قد يتجاوز 1.5 ثانية مسبباً Time Limit Exceeded (TLE)!

الحل السحري: سطرين في بداية دالة main:
1. ios_base::sync_with_stdio(false);
   - يفصل المزامنة بين تدفقات C و C++.
2. cin.tie(NULL);
   - يفك ارتباط cin بـ cout، فلا يتم تفريغ buffer الإخراج تلقائياً قبل كل إدخال.
3. استبدل endl بـ '\n':
   - الرمز endl يجبر النظام على تفريغ الـ buffer في كل سطر، بينما '\n' يضيف سطراً جديداً دون إبطاء.

مكتبة #include <bits/stdc++.h>:
هذا الملف الرأسي يضم كافة مكتبات لغة C++ في سطر واحد:
- iostream, vector, string, algorithm, map, set, queue, stack, cmath, cstring, bitset, numeric, unordered_map.
            """.trimIndent(),
            cppCodeExample = """
#include <bits/stdc++.h>
using namespace std;

#define ll long long
#define pb push_back
#define all(v) (v).begin(), (v).end()

void solve() {
    int n;
    cin >> n;
    vector<ll> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    sort(all(a));
    cout << a[0] << " " << a[n - 1] << '\n';
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int t = 1;
    // cin >> t; // إذا كانت هناك حالات اختبار متعددة
    while (t--) {
        solve();
    }
    return 0;
}
            """.trimIndent(),
            relatedProblemIds = listOf("4A", "71A", "1328A")
        ),

        Lesson(
            id = "lesson_complexity",
            titleAr = "تحليل التعقيد الزمني وقاعدة 10^8 عملية في الثانية",
            titleEn = "Time & Space Complexity in Competitive Programming",
            category = "تحليل الخوارزميات",
            levelAr = "مبتدئ إلى متوسط",
            readTimeAr = "10 دقائق",
            summaryAr = "كيف تقرأ قيود المسألة (Constraints) وتعرف الخوارزمية المطلوبة قبل كتابة أي سطر كود!",
            contentAr = """
القاعدة الذهبية في منصات البرمجة التنافسية (Codeforces):
معظم المسائل تعطي حداً زمنياً هو 1.0 أو 2.0 ثانية.
المعالج الحديث يستطيع تنفيذ حوالي 10^8 عملية بسيطة في الثانية الواحدة (100 مليون عملية).

جدول اختيار الخوارزمية حسب قيمة N:
- N ≤ 10: تقبل تعقيد O(N!) أو O(2^N * N) (Backtracking / Bitmask DP)
- N ≤ 20: تقبل تعقيد O(2^N) (Exponential search)
- N ≤ 100: تقبل تعقيد O(N^4) أو O(N^3) (Floyd-Warshall, Dynamic Programming)
- N ≤ 1,000: تقبل تعقيد O(N^2) (حلقات متداخلة Nested Loops)
- N ≤ 100,000 (10^5): تقبل O(N log N) أو O(N) (Sorting, Binary Search, Segment Tree)
- N ≤ 1,000,000 (10^6): تقبل O(N) أو O(N log N) سريع جداً
- N ≥ 10^9: تتطلب O(log N) أو O(1) (رياضيات، نظرية الأعداد، Binary Search)

فخاخ شائعة:
- فخ Integer Overflow: نوع int يتحمل حتى 2 * 10^9 تقريباً. إذا كان الجواب أو المجموع يتجاوز ذلك، يجب استخدام long long (حتى 9 * 10^18).
            """.trimIndent(),
            cppCodeExample = """
// مثال لتفادي Integer Overflow
long long a = 1000000000; // 10^9
long long b = 1000000000;

// خطأ شائع:
// int ans = a * b; // سيتسبب في Overflow وقيمة عشوائية سالبة!

// الصحيح:
long long correctAns = a * b; // 10^18 محفوظ بأمان في 64-bit
            """.trimIndent(),
            relatedProblemIds = listOf("1A", "1328A")
        ),

        Lesson(
            id = "lesson_two_pointers",
            titleAr = "تقنية المؤشرات المزدوجة والنافذة المنزلقة (Two Pointers & Sliding Window)",
            titleEn = "Two Pointers Technique",
            category = "تقنيات متقدمة",
            levelAr = "متوسط",
            readTimeAr = "12 دقيقة",
            summaryAr = "تحويل الخوارزميات البطيئة ذات التعقيد O(N^2) إلى O(N) أنيق باستخدام مؤشرين يتحركان بذكاء.",
            contentAr = """
فكرة التقنية:
بدلاً من فحص جميع الأزواج الممكنة بحلقتين متداخلتين O(N^2)، نستخدم مؤشرين:
1. تقابل من الأطراف (Converging Pointers): أحدهما عند البداية L = 0 والآخر عند النهاية R = N - 1 (شائع في المصفوفات المرتبة، مثل مسألة Two Sum أو Palindrome).
2. حركة في اتجاه واحد (Sliding Window): مؤشر يوسع النافذة (Right) ومؤشر يقلصها (Left) للبحث عن أطول أو أقصر مقطع فرعي يحقق شرطاً معيناً.

لماذا التعقيد O(N)؟
لأن كلاً من المؤشر الأيسر والمؤشر الأيمن يتحركان للأمام فقط ولا يرجعان للخلف أبداً، فيكون مجموع حركات المؤشرين هو 2N كحد أقصى!
            """.trimIndent(),
            cppCodeExample = """
#include <iostream>
#include <vector>
using namespace std;

// مثال: إيجاد أطول نافذة مجموع عناصرها لا يتجاوز K
int longestSubarrayWithSumK(vector<int>& arr, long long k) {
    int n = arr.size();
    int left = 0, maxLength = 0;
    long long currentSum = 0;

    for (int right = 0; right < n; right++) {
        currentSum += arr[right];

        // تقليص النافذة من اليسار طالما المجموع أكبر من k
        while (currentSum > k && left <= right) {
            currentSum -= arr[left];
            left++;
        }

        maxLength = max(maxLength, right - left + 1);
    }
    return maxLength;
}
            """.trimIndent(),
            relatedProblemIds = listOf("158A", "466C")
        ),

        Lesson(
            id = "lesson_binary_search",
            titleAr = "البحث الثنائي على فضاء الإجابات (Binary Search on Answer)",
            titleEn = "Binary Search on Answer",
            category = "خوارزميات البحث",
            levelAr = "متوسط إلى متقدم",
            readTimeAr = "15 دقيقة",
            summaryAr = "تحويل مسائل التحسين (Optimization) إلى مسائل تحقق (Decision)، وحلها في تعقيد O(log(MaxVal)).",
            contentAr = """
متى نطبق البحث الثنائي على الإجابة؟
عندما تكون المسألة تطلب:
- "أوجد أصغر قيمة تحقق الشرط X"
- "أوجد أكبر قيمة تحقق الشرط X"
وكانت الدالة تحقق خاصية الرتابة (Monotonicity):
إذا كانت القيمة mid تحقق الشرط، فإن جميع القيم الأكبر منها تحققه أيضاً (أو العكس: True, True, True, False, False).

الخطوات:
1. نحدد فضاء البحث [low, high].
2. نحسب mid = low + (high - low) / 2.
3. نكتب دالة تحقق boolean check(mid) لمعرفة هل mid صالحة أم لا.
4. إذا كانت صالحة، نحفظ الجواب ونبحث في النصف الأفضل، وإلا نستبعد النصف غير الممكن.
            """.trimIndent(),
            cppCodeExample = """
#include <iostream>
using namespace std;

bool check(long long mid, long long n, long long k) {
    // التحقق من صحة القيمة
    long long nonDivisible = mid - (mid / n);
    return nonDivisible >= k;
}

long long binarySearchAnswer(long long n, long long k) {
    long long low = 1, high = 2e9, ans = high;
    while (low <= high) {
        long long mid = low + (high - low) / 2;
        if (check(mid, n, k)) {
            ans = mid;
            high = mid - 1; // نبحث عن أصغر قيمة تحقق
        } else {
            low = mid + 1;
        }
    }
    return ans;
}
            """.trimIndent(),
            relatedProblemIds = listOf("1352C")
        ),

        Lesson(
            id = "lesson_dp",
            titleAr = "البرمجة الديناميكية: من المبتدئ للاحتراف (Dynamic Programming)",
            titleEn = "Dynamic Programming: Memoization & Tabulation",
            category = "خوارزميات متقدمة",
            levelAr = "متقدم",
            readTimeAr = "18 دقيقة",
            summaryAr = "تفكيك المسائل المعقدة إلى مسائل فرعية متداخلة وتخزين النتائج لمنع إعادة الحسابات.",
            contentAr = """
أركان البرمجة الديناميكية:
1. المسائل الفرعية المتداخلة (Overlapping Subproblems): تكرار حساب نفس الحالة مراراً.
2. البنية التحتية المثلى (Optimal Substructure): الحل الأمثل للمسألة الكبيرة يتكون من الحلول المثلى لمسائلها الفرعية.

النهجان الأساسيان:
1. من أعلى لأسفل (Top-Down with Memoization):
   - استخدام دالة العودية (Recursion) مع مصفوفة memo لتخزين ناتج كل حالة فور حسابها.
2. من أسفل لأعلى (Bottom-Up Tabulation):
   - استخدام مصفوفة dp وملؤها تكرارياً بحلقات for من الحالات القاعدية (Base Cases) وصولاً للهدف.
            """.trimIndent(),
            cppCodeExample = """
#include <iostream>
#include <vector>
using namespace std;

// مثال: مسألة حقيبة الظهر الكلاسيكية (0/1 Knapsack)
int knapsack(int W, const vector<int>& wt, const vector<int>& val, int n) {
    vector<vector<int>> dp(n + 1, vector<int>(W + 1, 0));

    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= W; w++) {
            if (wt[i - 1] <= w) {
                dp[i][w] = max(val[i - 1] + dp[i - 1][w - wt[i - 1]], dp[i - 1][w]);
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }
    return dp[n][W];
}
            """.trimIndent(),
            relatedProblemIds = listOf("466C", "489C")
        )
    )
}
