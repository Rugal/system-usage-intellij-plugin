package ga.rugal.intellij.sample.ui.action

import java.awt.datatransfer.StringSelection
import ga.rugal.intellij.sample.configuration.Icon
import ga.rugal.intellij.sample.service.NotificationService
import ga.rugal.intellij.sample.service.PsiService
import ga.rugal.intellij.sample.service.PsiService.httpServletRequest
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.PsiMethod

class RugalAction : AnAction("Copy REST Path", "Copy corresponding REST path", Icon.PARTICLE_ICON) {
  private val LOG = Logger.getInstance(this::class.java)

  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.isEnabled = when (val element = e.dataContext.getData(CommonDataKeys.PSI_ELEMENT)) {
      is PsiMethod -> PsiService.hasRequestAnnotation(element)
      else -> false
    }
  }

  override fun actionPerformed(e: AnActionEvent) {
    val dataContext = e.dataContext
    val element = dataContext.getData(CommonDataKeys.PSI_ELEMENT)
    element as PsiMethod

    try {
      val request = element.httpServletRequest
      LOG.info("Resolve request method [${request.method}] path [${request.servletPath}]")

      CopyPasteManager.getInstance().setContents(StringSelection(request.servletPath))
      // combine them into real path
      NotificationService.notify("REST path copied", e.project!!)
    } catch (_: NoSuchElementException) {
      NotificationService
        .createNotificationObject("No REST mapping found", NotificationType.WARNING)
        .notify(e.project)
    }
  }
}
