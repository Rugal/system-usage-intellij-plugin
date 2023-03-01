package ga.rugal.intellij.monitor.service.github

import java.util.Base64
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import ga.rugal.intellij.common.service.PluginPropertyService

class IssueServiceTest : BasePlatformTestCase() {

  fun testCreate() {
    val issue = IssueService.create(IdeaLoggingEvent("rugal message", Exception()), "test title")
    assertNotNull(issue)
  }
}
