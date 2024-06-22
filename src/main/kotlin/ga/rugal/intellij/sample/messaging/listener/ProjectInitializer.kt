package ga.rugal.intellij.sample.messaging.listener

import ga.rugal.intellij.sample.service.EditorService
import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ProjectInitializer : ProjectActivity {
  private val LOG = Logger.getInstance(this::class.java)

  override suspend fun execute(project: Project) {

    RunOnceUtil.runOnceForProject(project, "CloudEnvironmentDetection") {
      LOG.trace("Determine execution environment")

      ApplicationManager.getApplication().invokeLater({
        if (!ApplicationManager.getApplication().isUnitTestMode) {
          EditorService.open(project, "Rugal title")
        }
      }, ModalityState.defaultModalityState())
    }
  }
}
