package ga.rugal.intellij.sample.messaging.listener

import ga.rugal.intellij.sample.service.EditorService
import ga.rugal.intellij.sample.ui.action.RugalAction
import com.intellij.ide.util.RunOnceUtil
import com.intellij.notification.impl.NotificationGroupEP
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.Constraints
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ProjectInitializer : ProjectActivity {
  private val LOG = Logger.getInstance(this::class.java)

  private fun addAction() {
    val m = ActionManager.getInstance()

    // ignore if already registered
    if (m.getAction(RugalAction.javaClass.name) != null) return

    m.registerAction(RugalAction.javaClass.name, RugalAction)
    (m.getAction("Copy.Paste.Special") as DefaultActionGroup).also {
      it.add(RugalAction, Constraints.LAST)
    }
  }

  override suspend fun execute(project: Project) {
    this.addAction()

    RunOnceUtil.runOnceForProject(project, "CloudEnvironmentDetection") {
      LOG.trace("Determine execution environment")

      ApplicationManager.getApplication().invokeLater({
        EditorService.open(project, "Rugal title")
      }, ModalityState.defaultModalityState())
    }
  }
}
