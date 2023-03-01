package ga.rugal.intellij.monitor.service

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SystemUsageServiceTest : BasePlatformTestCase() {

  fun testGetMemory() {
    SystemUsageService.getMemory().let {
      println(it)
      assertNotNull(it)
    }
  }

  fun testGetFileSystem() {
    SystemUsageService.getFileSystem().let {
      it.forEach { s ->
        println(s)
        assertNotNull(SystemUsageService.getMemory())
      }
    }
  }
}
