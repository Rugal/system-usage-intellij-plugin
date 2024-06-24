package ga.rugal.intellij.sample.service

import com.intellij.openapi.util.TextRange
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.inspections.Splitter
import org.junit.Assert.assertEquals
import org.junit.Test

class SplitterTest {

  @Test
  fun testSha256InsideText() {
    val text = "asdasd 50d858e0985ecc7f60418aaf0cc5ab587f42c2570a884095a9e8ccacd0f6545c asdasd"
    correctListToCheck(PlainTextSplitter.getInstance(), text, "asdasd", "asdasd")
  }

  @Test
  fun testJwtInsideText() {
    val text =
      "asdasd eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.dyt0CoTl4WoVjAHI9Q_CwSKhl6d_9rhM3NrXuJttkao asdasd"
    correctListToCheck(PlainTextSplitter.getInstance(), text, "asdasd", "asdasd")
  }

  private fun wordsToCheck(splitter: Splitter, text: String): List<String> {
    val words: MutableList<String> = ArrayList()
    splitter.split(text, TextRange.allOf(text)) { textRange -> words.add(textRange.substring(text)) }
    return words
  }

  private fun correctListToCheck(splitter: Splitter, text: String, vararg expected: String) {
    val words = wordsToCheck(splitter, text)
    val expectedWords: List<String> = expected
    assertEquals("Splitting:'$text'", expectedWords.toString(), words.toString())
  }
}
