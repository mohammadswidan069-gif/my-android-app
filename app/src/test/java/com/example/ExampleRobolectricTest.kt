package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ai.CodeAnalyzer
import com.example.ai.DiagnosticType
import com.example.data.repository.SampleProblemsData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("كود فورس عربي", appName)
  }

  @Test
  fun `code analyzer detects missing main`() {
    val diagnostics = CodeAnalyzer.analyzeCpp("int a = 5;", null)
    assertTrue(diagnostics.any { it.type == DiagnosticType.ERROR && it.titleAr.contains("main") })
  }

  @Test
  fun `code analyzer detects missing fast io`() {
    val code = "int main() { return 0; }"
    val diagnostics = CodeAnalyzer.analyzeCpp(code, null)
    assertTrue(diagnostics.any { it.type == DiagnosticType.WARNING && it.titleAr.contains("Fast I/O") })
  }

  @Test
  fun `problems data is sorted and contains essential problems`() {
    val problems = SampleProblemsData.problems
    assertTrue(problems.isNotEmpty())
    assertEquals("4A", problems.first().id)
  }
}
