package ga.rugal.intellij.rest.ui.action

import java.awt.datatransfer.StringSelection
import ga.rugal.intellij.rest.configuration.Icon
import ga.rugal.intellij.rest.service.NotificationService
import ga.rugal.intellij.rest.service.PsiService
import ga.rugal.intellij.rest.service.PsiService.httpServletRequest
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.PsiMethod

class CopyAction : AnAction("Copy REST Path", "Copy corresponding REST path", Icon.PARTICLE_ICON) {
  private val LOG = Logger.getInstance(this::class.java)

  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.isEnabled = when (val element = e.dataContext.getData(CommonDataKeys.PSI_ELEMENT)) {
      is PsiMethod -> PsiService.hasRequestAnnotation(element)
      else -> false
    }
    LOG.debug("Set button presentation as enabled [${e.presentation.isEnabled}]")
  }

  override fun actionPerformed(e: AnActionEvent) {
    val dataContext = e.dataContext
    val element = dataContext.getData(CommonDataKeys.PSI_ELEMENT)
    element as PsiMethod

    try {
      val request = element.httpServletRequest
      LOG.debug("Resolve request method [${request.method}] path [${request.servletPath}]")

      CopyPasteManager.getInstance().setContents(StringSelection("${request.method} ${request.servletPath}"))
      LOG.trace("Set REST mapping into clip board")
      // combine them into real path
      NotificationService.notify("REST path copied", e.project!!)
    } catch (_: NoSuchElementException) {
      LOG.error("No REST mapping found")
      NotificationService
        .createNotificationObject("No REST mapping found", NotificationType.WARNING)
        .notify(e.project)
    }
  }
}
