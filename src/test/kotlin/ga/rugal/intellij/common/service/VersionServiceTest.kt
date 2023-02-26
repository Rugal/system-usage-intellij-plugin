package ga.rugal.intellij.common.service

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class VersionServiceTest : BasePlatformTestCase() {

  fun testVersion() {
    assertEquals("1.0.0-snapshot", VersionService.pluginVersion("cloudide.hotcode"))
  }
}
