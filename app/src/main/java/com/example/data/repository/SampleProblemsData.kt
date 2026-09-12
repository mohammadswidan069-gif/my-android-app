package com.example.data.repository

import com.example.data.model.Problem
import com.example.data.model.TestCase

object SampleProblemsData {

    val problems: List<Problem> = listOf(
        Problem(
            id = "4A",
            contestId = 4,
            index = "A",
            titleAr = "البطيخة (Watermelon)",
            titleEn = "Watermelon",
            rating = 800,
            tags = listOf("رياضيات", "قوة عمياء", "مبتدئ"),
            statementAr = "في يوم صيفي حار، قرر صديقان يدعيان بيتي وبيلي شراء بطيخة واحدة وزنها w كيلو غرام. يريدان تقسيم البطيخة إلى جزأين بحيث يكون وزن كل جزء عدداً زوجياً موجباً من الكيلوغرامات (ليس بالضرورة أن يتساوى الجزءان). هل يمكنهما تحقيق ذلك؟",
            inputAr = "سطر واحد يحتوي على عدد صحيح w (1 ≤ w ≤ 100) يمثل وزن البطيخة بالكيلوغرام.",
            outputAr = "اطبع YES إذا كان بإمكانهما تقسيم البطيخة لجزأين زوجيين، أو NO في حال العكس.",
            solutionLogicAr = """
خطوات التفكير المنطقي:
1. لنفترض أننا قسمنا البطيخة إلى وزنين x و y بحيث x + y = w.
2. الشرط هو أن يكون x عدداً زوجياً موجباً (x ≥ 2 و x % 2 == 0)، وأن يكون y عدداً زوجياً موجباً (y ≥ 2 و y % 2 == 0).
3. مجموع أي عددين زوجيين هو دائماً عدد زوجي! بالتالي إذا كان وزن البطيخة w فردياً، فمن المستحيل رياضياً تقسيمه لعددين زوجيين.
4. ماذا لو كان w زوجياً؟
   - أصغر عدد زوجي موجب هو 2. لو كان w = 2، فإن التقسيم الوحيد الممكن هو 1 و 1، وكلاهما فردي! لذلك الرقم 2 حالة خاصة نتيجتها NO.
   - لأي عدد زوجي w > 2 (مثل 4 = 2 + 2، أو 6 = 2 + 4، أو 8 = 2 + 6...)، دائماً يمكننا إعطاء الأول 2 وإعطاء الثاني (w - 2)، وكلاهما زوجيان موجبان!
5. الخلاصة: الشرط هو (w > 2 && w % 2 == 0).
التعقيد الزمني: O(1)
التعقيد المكاني: O(1)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>

using namespace std;

int main() {
    // تسريع عمليات الإدخال والإخراج
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int w;
    cin >> w;

    // الشرط: الوزن زوجي وأكبر تماماً من 2
    if (w > 2 && w % 2 == 0) {
        cout << "YES\n";
    } else {
        cout << "NO\n";
    }

    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int w;
    cin >> w;

    // اكتب منطق الحل هنا
    

    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "8", "YES", "يمكن تقسيم 8 إلى 2 و 6 أو 4 و 4"),
                TestCase(2, "2", "NO", "التقسيم الوحيد هو 1 و 1 وهما ليسا زوجيين"),
                TestCase(3, "5", "NO", "العدد فردي مستحيل"),
                TestCase(4, "100", "YES", "يمكن تقسيمها إلى 2 و 98")
            )
        ),

        Problem(
            id = "71A",
            contestId = 71,
            index = "A",
            titleAr = "كلمات طويلة جداً (Way Too Long Words)",
            titleEn = "Way Too Long Words",
            rating = 800,
            tags = listOf("سلاسل نصية", "محاكاة"),
            statementAr = "تعتبر الكلمة طويلة جداً إذا زاد طولها عن 10 أحرف. يتم اختصار مثل هذه الكلمات بالشكل التالي: الحرف الأول، يليه عدد الأحرف الموجودة بين الحرف الأول والأخير، ثم الحرف الأخير. الكلمات التي طولها 10 أحرف أو أقل تبقى كما هي بدون تغيير.",
            inputAr = "السطر الأول يحتوي على عدد الكلمات n (1 ≤ n ≤ 100). تليها n أسطر، كل سطر يحتوي على كلمة مكونة من حروف إنجليزية صغيرة بطول بين 1 و 100 حرف.",
            outputAr = "اطبع كل كلمة بعد الاختصار (إن كانت تتطلب ذلك) في سطر منفصل.",
            solutionLogicAr = """
خطوات التفكير المنطقي:
1. نقرأ عدد الكلمات n بواسطة حلقة تكرارية.
2. لكل كلمة s، نتحقق من طولها باستخدام s.length().
3. إذا كان الطول > 10:
   - الحرف الأول هو s[0]
   - الحرف الأخير هو s[len - 1]
   - عدد الأحرف الداخلية هو (len - 2)
   - نطبع: s[0] + (len - 2) + s[len - 1]
4. إذا كان الطول ≤ 10، نطبع الكلمة s كما هي.
التعقيد الزمني: O(N * L) حيث L طول السلسلة، وهو لحظي.
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>
#include <string>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    while (n--) {
        string s;
        cin >> s;
        int len = s.length();
        if (len > 10) {
            cout << s[0] << (len - 2) << s[len - 1] << "\n";
        } else {
            cout << s << "\n";
        }
    }
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    while (n--) {
        string s;
        cin >> s;
        // منطق الحل
    }

    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "4\nword\nlocalization\ninternationalization\npneumonoultramicroscopicsilicovolcanoconiosis", "word\nl10n\ni18n\np43s", "اختصار الكلمات الأطول من 10 حروف")
            )
        ),

        Problem(
            id = "231A",
            contestId = 231,
            index = "A",
            titleAr = "الفريق (Team)",
            titleEn = "Team",
            rating = 800,
            tags = listOf("قوة عمياء", "بحث بسيط"),
            statementAr = "ثلاثة أصدقاء (بتيا وفاسيا وتونيا) يشاركون في مسابقة برمجية. يقرر الفريق حل المسألة فقط إذا كان اثنان منهم على الأقل متأكدين من حلها. معطى آراء الأصدقاء حول n مسألة (1 تعني متأكد، 0 تعني غير متأكد). كم مسألة سيقوم الفريق بحلها؟",
            inputAr = "السطر الأول يحتوي على عدد المسائل n (1 ≤ n ≤ 1000). في كل سطر من n التالية ثلاثة أرقام (0 أو 1).",
            outputAr = "اطبع عدداً واحداً يمثل عدد المسائل التي سيحلها الفريق.",
            solutionLogicAr = """
خطوات التفكير المنطقي:
1. نهيئ متغير عداد ans = 0.
2. نكرر n مرة: نقرأ 3 أرقام a و b و c.
3. المجموع (a + b + c) يعبر عن عدد الأشخاص المتأكدين.
4. إذا كان المجموع ≥ 2، نزيد ans بمقدار 1.
5. نطبع الناتج النهائي ans.
التعقيد الزمني: O(N)
التعقيد المكاني: O(1)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    int solvedCount = 0;

    for (int i = 0; i < n; i++) {
        int a, b, c;
        cin >> a >> b >> c;
        if (a + b + c >= 2) {
            solvedCount++;
        }
    }

    cout << solvedCount << "\n";
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    // أكمل الكود
    
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "3\n1 1 0\n1 1 1\n1 0 0", "2", "في المسألتين الأولى والثانية اثنان أو ثلاثة متأكدون"),
                TestCase(2, "2\n1 0 0\n0 1 1", "1", "فقط مسألة واحدة تأكد منها 2")
            )
        ),

        Problem(
            id = "282A",
            contestId = 282,
            index = "A",
            titleAr = "لغة بت بلس بلس (Bit++)",
            titleEn = "Bit++",
            rating = 800,
            tags = listOf("محاكاة", "سلاسل نصية"),
            statementAr = "اللغة الكلاسيكية Bit++ تحتوي على متغير واحد اسمه x قيمته الابتدائية 0. تدعم عمليتين: الزيادة (++X أو X++) والنقصان (--X أو X--). بعد تنفيذ سلسلة من n عملية، ما هي القيمة النهائية لـ x؟",
            inputAr = "السطر الأول n (1 ≤ n ≤ 150). تليها n أسطر، كل سطر يحتوي على إحدى العمليات الأربع.",
            outputAr = "اطبع قيمة المتغير x بعد كل العمليات.",
            solutionLogicAr = """
خطوات التفكير:
1. نبدأ بـ x = 0.
2. لكل سطر نصي s، نلاحظ أن العملية الإيجابية تحتوي دوماً على الرمز '+' (سواء كانت ++X أو X++).
3. العملية السلبية تحتوي دوماً على الرمز '-'.
4. يكفي فحص وجود '+' في الكلمة (أو تحديداً s[1] == '+')؛ إذا وُجد نزيد x بمقدار 1، وإلا ننقص x بمقدار 1.
5. نطبع الناتج.
التعقيد: O(N)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>
#include <string>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    int x = 0;

    while (n--) {
        string op;
        cin >> op;
        if (op[1] == '+') {
            x++;
        } else {
            x--;
        }
    }

    cout << x << "\n";
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    int x = 0;
    // أكمل منطق العمليات
    
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "1\n++X", "1", "زيادة واحدة"),
                TestCase(2, "2\nX++\n--X", "0", "زيادة ثم نقصان")
            )
        ),

        Problem(
            id = "158A",
            contestId = 158,
            index = "A",
            titleAr = "الجولة التالية (Next Round)",
            titleEn = "Next Round",
            rating = 800,
            tags = listOf("مصفوفات", "محاكاة"),
            statementAr = "يتأهل المتسابق إلى الجولة التالية إذا حصل على درجة مساوية أو أكبر من درجة المتسابق صاحب المركز k، بشرط أن تكون درجته موجبة تماماً (أكبر من الصفر). معطى درجات n متسابق مرتبة تنازلياً. كم متسابق سيتأهل؟",
            inputAr = "السطر الأول: n و k (1 ≤ k ≤ n ≤ 50). السطر الثاني: n أعداد صحيحة تمثل الدرجات مرتبة تنازلياً (0 ≤ a_i ≤ 100).",
            outputAr = "اطبع عدد المتسابقين المتأهلين.",
            solutionLogicAr = """
خطوات التفكير:
1. نقرأ n و k، ثم نقرأ مصفوفة الدرجات a.
2. درجة العتبة هي درجة المتسابق رقم k (أي a[k-1] في فهرسة المصفوفة من 0).
3. نمر على جميع المتسابقين:
   - لكي يتأهل المتسابق i، يجب أن يتحقق شرطان معاً:
     a[i] >= a[k-1]  و  a[i] > 0
4. نعد كم متسابقاً يحقق هذا الشرط ونطبع المجموع.
التعقيد: O(N)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n, k;
    cin >> n >> k;
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }

    int threshold = a[k - 1];
    int count = 0;
    for (int i = 0; i < n; i++) {
        if (a[i] >= threshold && a[i] > 0) {
            count++;
        }
    }

    cout << count << "\n";
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n, k;
    cin >> n >> k;
    // أكمل الكود
    
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "8 5\n10 9 8 7 7 7 5 5", "6", "الدرجة عند المركز الخامس 7، والمتسابق السادس نال 7 أيضاً"),
                TestCase(2, "4 2\n0 0 0 0", "0", "لا يوجد أحد بدرجة موجبة")
            )
        ),

        Problem(
            id = "1328A",
            contestId = 1328,
            index = "A",
            titleAr = "مسألة قابلية القسمة (Divisibility Problem)",
            titleEn = "Divisibility Problem",
            rating = 900,
            tags = listOf("رياضيات", "حساب نمطي"),
            statementAr = "معطى عددان موجبان a و b. في كل خطوة يمكنك زيادة a بمقدار 1 (أي a = a + 1). ما هو أقل عدد من الخطوات المطلوبة ليصبح a قابلاً للقسمة على b؟",
            inputAr = "السطر الأول يحتوي على t (1 ≤ t ≤ 10^4) عدد حالات الاختبار. كل حالة تحتوي على a و b (1 ≤ a, b ≤ 10^9).",
            outputAr = "لكل حالة اطبع أقل عدد من الخطوات.",
            solutionLogicAr = """
خطوات التفكير والتحليل الرياضي:
1. انتبه! قيمة t تصل لـ 10^4 و a, b يصل لـ 10^9. استخدام حلقة while (a % b != 0) a++; سيؤدي إلى Time Limit Exceeded (TLE) حتماً!
2. الحل الرياضي المباشر O(1):
   - نحسب باقي قسمة a على b: rem = a % b.
   - إذا كان rem == 0، فالعدد قابل للقسمة بالفعل، والخطوات المطلوبة = 0.
   - إذا كان rem != 0، فإن المسافة حتى المضاعف التالي للعدد b هي: (b - rem).
3. هذه الصيغة تعطي الإجابة الصحيحة مباشرة بتعقيد O(1) لكل حالة اختبار!
التعقيد الزمني الإجمالي: O(t)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int t;
    cin >> t;
    while (t--) {
        long long a, b;
        cin >> a >> b;
        long long rem = a % b;
        if (rem == 0) {
            cout << 0 << "\n";
        } else {
            cout << (b - rem) << "\n";
        }
    }
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

void solve() {
    long long a, b;
    cin >> a >> b;
    // تجنب الحلقات واستخدم الحساب النمطي O(1)
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int t;
    cin >> t;
    while (t--) solve();
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "5\n10 4\n13 9\n100 13\n123 456\n92 46", "2\n5\n4\n333\n0", "أقل خطوات للمضاعف التالي")
            )
        ),

        Problem(
            id = "1A",
            contestId = 1,
            index = "A",
            titleAr = "ساحة المسرح (Theatre Square)",
            titleEn = "Theatre Square",
            rating = 1000,
            tags = listOf("رياضيات", "حسابات دقيقة", "Overflow"),
            statementAr = "ساحة المسرح في العاصمة مستطيلة الشكل أبعادها n × m متراً. يُراد رصف الساحة ببلاط جرانيتي مربع طول ضلعه a متراً. ما هو أقل عدد من البلاطات لتغطية كامل الساحة، علماً أنه لا يجوز كسر البلاط ويُسمح بأن تتجاوز البلاطات حدود الساحة قليلاً؟",
            inputAr = "سطر واحد يحتوي على 3 أعداد صحيحة: n, m, a (1 ≤ n, m, a ≤ 10^9).",
            outputAr = "اطبع أقل عدد من البلاطات المطلوبة.",
            solutionLogicAr = """
فخاخ شائعة وخطوات التفكير:
1. فخ Integer Overflow: الأبعاد n و m و a تصل إلى 10^9. حاصل ضرب عدد البلاطات قد يصل إلى 10^18، وهو ما يتجاوز الحد الأقصى لـ 32-bit int (حوالي 2 × 10^9). بالتالي يجب حتماً استخدام long long!
2. لحساب عدد البلاطات على طول البعد n:
   - نحتاج ceil(n / a) أي السقف الرياضي.
   - في C++ الصحيح (integer division)، يمكن حساب السقف الرياضي بدون دوال float دقيقة بالصيغة:
     (n + a - 1) / a
3. بالمثل على طول البعد m:
   - عدد البلاطات = (m + a - 1) / a
4. الإجمالي = ((n + a - 1) / a) * ((m + a - 1) / a).
التعقيد الزمني: O(1)
التعقيد المكاني: O(1)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    long long n, m, a;
    cin >> n >> m >> a;

    long long tilesAlongN = (n + a - 1) / a;
    long long tilesAlongM = (m + a - 1) / a;

    long long totalTiles = tilesAlongN * tilesAlongM;
    cout << totalTiles << "\n";

    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    // تذكر استخدام long long لتفادي الـ overflow
    long long n, m, a;
    cin >> n >> m >> a;

    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "6 6 4", "4", "2 بلاطة في كل اتجاه فالمجموع 4"),
                TestCase(2, "1 1 1", "1", "بلاطة واحدة تغطي الساحة"),
                TestCase(3, "1000000000 1000000000 1", "1000000000000000000", "اختبار الأعداد الضخمة والـ Overflow")
            )
        ),

        Problem(
            id = "1352C",
            contestId = 1352,
            index = "C",
            titleAr = "العدد رقم k غير القابل للقسمة على n",
            titleEn = "K-th Not Divisible by n",
            rating = 1200,
            tags = listOf("رياضيات", "بحث ثنائي", "منطق تحليلي"),
            statementAr = "معطى عددان صحيحان موجبان n و k. اكتب برنامجاً يجد العدد الصحيح الموجب رقم k الذي لا يقبل القسمة على n في متتالية الأعداد الطبيعية (1, 2, 3...).",
            inputAr = "السطر الأول t (1 ≤ t ≤ 10^4). كل سطر يحتوي على n و k (2 ≤ n ≤ 10^9, 1 ≤ k ≤ 10^9).",
            outputAr = "لكل حالة، اطبع العدد رقم k غير القابل للقسمة على n.",
            solutionLogicAr = """
خطوات التفكير المنطقي المتقدم:
1. في كل كتلة طولها n من الأعداد المتتالية، يوجد بالضبط (n - 1) أرقام غير قابلة للقسمة على n، ورقم واحد فقط مضاعف لـ n.
2. نريد إيجاد العدد رقم k غير القابل للقسمة.
3. عدد الكتل الكاملة السابقة = (k - 1) / (n - 1).
4. في كل كتلة كاملة تم تخطي رقم واحد مضاعف لـ n. بالتالي نزحف بمقدار هذا العدد من المضاعفات.
5. الصيغة المباشرة O(1):
   الإجابة = k + ((k - 1) / (n - 1))
6. مثال توضيحي: n = 3, k = 7
   - الأعداد غير القابلة للقسمة: 1, 2, 4, 5, 7, 8, 10
   - العدد السابع هو 10.
   - بالصيغة: 7 + ((7 - 1) / (3 - 1)) = 7 + (6 / 2) = 7 + 3 = 10! دقيقة 100%.
التعقيد: O(1) لكل حالة، ممتاز لـ t = 10^4.
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>

using namespace std;

void solve() {
    long long n, k;
    cin >> n >> k;
    long long ans = k + (k - 1) / (n - 1);
    cout << ans << "\n";
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int t;
    cin >> t;
    while (t--) {
        solve();
    }
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

void solve() {
    long long n, k;
    cin >> n >> k;
    // حل رياضي O(1)
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    int t;
    cin >> t;
    while (t--) solve();
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "6\n3 7\n4 12\n2 1000000000\n7 97\n1000000000 1000000000\n2 1", "10\n15\n1999999999\n113\n1000000001\n1", "حالات متنوعة واختبار الحدود العليا")
            )
        ),

        Problem(
            id = "489C",
            contestId = 489,
            index = "C",
            titleAr = "بناء عدد بمعلومية طوله ومجموع أرقامه",
            titleEn = "Given Length and Sum of Digits...",
            rating = 1400,
            tags = listOf("خوارزميات جشعة Greedy", "تراكيب أعداد"),
            statementAr = "لديك طول العدد m ومجموع خاناته s. أوجد أصغر وأكبر عدد موجب مكون من m خانة بالضبط ولا يبدأ بالصفر، بحيث يكون مجموع أرقام خاناته مساوياً لـ s تماماً. إذا استحال تكوين عدد بهذه المواصفات، اطبع -1 -1.",
            inputAr = "سطر واحد يحتوي على عددين صحيحين m و s (1 ≤ m ≤ 100, 0 ≤ s ≤ 900).",
            outputAr = "اطبع أصغر عدد ثم أكبر عدد مفصولين بمسافة، أو -1 -1 إن استحال ذلك.",
            solutionLogicAr = """
خطوات التفكير والتحليل الجشع (Greedy Approach):
1. شروط الاستحالة:
   - إذا كان s = 0: الحل الوحيد هو m = 1 والعدد هو "0". إذا كان m > 1 و s = 0 فالجواب مستحيل لأن العدد لا يجوز أن يبدأ بصفر.
   - أقصى مجموع يمكن تكوينه لعدد طوله m هو (9 * m). إذا كان s > 9 * m، فالجواب مستحيل!
2. لإيجاد أكبر عدد (Greedy Max):
   - نريد وضع أكبر رقم ممكن في الخانات اليسرى (الأكثر أهمية).
   - نضع 9 طالما تبقى مجموع كافٍ، ثم الباقي، ثم أصفار في باقي الخانات.
3. لإيجاد أصغر عدد (Greedy Min):
   - الخانة الأولى (اليسرى) يجب أن تكون 1 على الأقل (حتى لا يبدأ بصفر).
   - نريد وضع أصغر أرقام ممكنة في اليسار، وأكبر أرقام (تصل إلى 9) في أقصى اليمين (الخانات الأقل وزناً).
   - نطرح 1 مؤقتاً من s للخانة الأولى، ثم نملأ الخانات من اليمين إلى اليسار بوضع min(9, remSum)، ونضيف 1 إلى الخانة الأولى في النهاية.
التعقيد الزمني: O(m)
التعقيد المكاني: O(m)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int m, s;
    cin >> m >> s;

    // الحالة الخاصة: s = 0
    if (s == 0) {
        if (m == 1) {
            cout << "0 0\n";
        } else {
            cout << "-1 -1\n";
        }
        return 0;
    }

    // استحالة: المجموع أكبر من 9 * m
    if (s > 9 * m) {
        cout << "-1 -1\n";
        return 0;
    }

    // 1. بناء أكبر عدد
    string maxNum = "";
    int remaining = s;
    for (int i = 0; i < m; i++) {
        int d = min(9, remaining);
        maxNum += to_string(d);
        remaining -= d;
    }

    // 2. بناء أصغر عدد
    string minNum = "";
    remaining = s - 1; // نحجز 1 للخانة الأولى
    for (int i = 0; i < m - 1; i++) {
        int d = min(9, remaining);
        minNum += to_string(d);
        remaining -= d;
    }
    minNum += to_string(remaining + 1);
    reverse(minNum.begin(), minNum.end());

    cout << minNum << " " << maxNum << "\n";
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int m, s;
    cin >> m >> s;

    // خوارزمية جشعة لبناء أصغر وأكبر عدد
    
    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "2 15", "69 96", "أصغر عدد 69 وأكبر عدد 96"),
                TestCase(2, "3 0", "-1 -1", "مستحيل تكوين عدد من 3 خانات مجموعه صفر دون أن يبدأ بصفر")
            )
        ),

        Problem(
            id = "466C",
            contestId = 466,
            index = "C",
            titleAr = "عدد الطرق لتقسيم المصفوفة إلى 3 أجزاء متساوية",
            titleEn = "Number of Ways",
            rating = 1500,
            tags = listOf("برمجة ديناميكية", "مصفوفات البادئات Prefix Sums", "تراكيب ثنائية"),
            statementAr = "معطى مصفوفة a مكونة من n عدد صحيح. احسب عدد الطرق لاختيار مؤشرين i و j بحيث 1 ≤ i < j ≤ n-1، ويتم تقسيم المصفوفة إلى 3 أجزاء غير فارغة متتالية، بحيث يكون مجموع عناصر كل جزء متساوياً تماماً.",
            inputAr = "السطر الأول n (1 ≤ n ≤ 5 · 10^5). السطر الثاني n أعداد صحيحة (-10^9 ≤ a_i ≤ 10^9).",
            outputAr = "اطبع عدداً واحداً يمثل عدد طرق التقسيم الممكنة.",
            solutionLogicAr = """
خطوات التفكير والتحليل الدقيق:
1. لنحسب المجموع الكلي للمصفوفة S.
2. إذا كان S غير قابل للقسمة على 3 (أي S % 3 != 0)، فمن المستحيل تقسيمها إلى 3 أجزاء متساوية، الناتج فوراً 0.
3. كل جزء من الأجزاء الثلاثة يجب أن يكون مجموعه مساوياً لـ target = S / 3.
4. الجزء الأول ينتهي عند مؤشر i بحيث مجموع البادئة prefix_sum[i] == target.
5. الجزء الثاني ينتهي عند مؤشر j بحيث مجموع البادئة prefix_sum[j] == 2 * target.
6. الجزء الثالث تلقائياً سيكون مجموعه target لأن المجموع الكلي S = 3 * target.
7. لتفادي حلقة O(N^2)، نمر مرة واحدة:
   - نحتفظ بعداد cnt1 لعدد المؤشرات السابقة التي كان مجموع بادئتها مساوياً لـ target.
   - عندما نصل إلى مؤشر j (مع 1 ≤ j < n-1) وكان مجموع بادئته 2 * target، فإن كل مؤشر i سابق كان مجموعه target يشكل طريقة تقسيم صالحة! فنضيف cnt1 إلى المجموع الإجمالي ans.
8. انتبه: النتيجة ans قد تصل إلى N * (N-1) / 2 = 1.25 × 10^11، لذا يجب أن يكون نوع المتغير ans هو long long!
التعقيد الزمني: O(N)
التعقيد المكاني: O(N) أو O(1)
            """.trimIndent(),
            cppModelSolution = """
#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    vector<long long> a(n);
    long long totalSum = 0;
    for (int i = 0; i < n; i++) {
        cin >> a[i];
        totalSum += a[i];
    }

    // إذا لم يقبل المجموع القسمة على 3
    if (totalSum % 3 != 0 || n < 3) {
        cout << 0 << "\n";
        return 0;
    }

    long long target = totalSum / 3;
    long long currentSum = 0;
    long long countFirstPart = 0;
    long long totalWays = 0;

    // نمر حتى n - 2 لأن الجزء الثالث يجب أن يحتوي على عنصر واحد على الأقل
    for (int i = 0; i < n - 1; i++) {
        currentSum += a[i];
        if (currentSum == 2 * target && i >= 1) {
            totalWays += countFirstPart;
        }
        if (currentSum == target) {
            countFirstPart++;
        }
    }

    cout << totalWays << "\n";
    return 0;
}
            """.trimIndent(),
            starterCode = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;
    vector<long long> a(n);
    for (int i = 0; i < n; i++) cin >> a[i];

    // استخدم prefix sum و target = totalSum / 3
    // تذكر استخدام long long للعداد النهائي

    return 0;
}
            """.trimIndent(),
            testCases = listOf(
                TestCase(1, "5\n1 2 3 0 3", "2", "طريقتان للتقسيم بمجموع 3 لكل جزء"),
                TestCase(2, "4\n0 1 -1 0", "1", "تقسيم وحيد بمجموع 0 لكل جزء"),
                TestCase(3, "2\n4 1", "0", "مصفوفة أقل من 3 عناصر")
            )
        )
    )
}
