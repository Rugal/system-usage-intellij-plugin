package ga.rugal.intellij.sample.configuration

import java.util.Base64
import ga.rugal.intellij.common.service.PluginPropertyService
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PsiServiceTest : BasePlatformTestCase() {

  fun testBase64() {
    val token = Base64.getDecoder().decode(PluginPropertyService.get("github.token.base64")).decodeToString().trim()
    assertTrue(token.startsWith("github_pat"))
  }
}
