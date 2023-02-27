package ga.rugal.intellij.monitor.service.github

import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class IssueServiceTest : BasePlatformTestCase() {

  fun testCreate() {
    val issue = IssueService.create(IdeaLoggingEvent("rugal message", Exception()), "test title")
    assertNotNull(issue)
  }
}
