package ga.rugal.intellij.monitor.messaging.listener

import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class EnvironmentDetector : ProjectActivity {
  private val LOG = Logger.getInstance(this::class.java)

  override suspend fun execute(project: Project) {
    RunOnceUtil.runOnceForProject(project, "CloudEnvironmentDetection") {
      LOG.trace("Determine execution environment")
    }
  }
}
