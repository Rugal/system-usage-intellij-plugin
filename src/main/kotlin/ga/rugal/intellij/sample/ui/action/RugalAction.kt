package ga.rugal.intellij.sample.ui.action

import ga.rugal.intellij.sample.configuration.Icon
import ga.rugal.intellij.sample.service.NotificationService
import ga.rugal.intellij.sample.service.PsiService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod

object RugalAction : AnAction("Copy REST Path", "Copy corresponding REST path", Icon.ROCKET_ICON) {
  private val LOG = Logger.getInstance(this::class.java)

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.isEnabled = when (val element = e.dataContext.getData(CommonDataKeys.PSI_ELEMENT)) {
      is PsiMethod -> PsiService.hasRequestAnnotation(element)
      else -> false
    }
  }

  override fun actionPerformed(e: AnActionEvent) {
    val dataContext = e.dataContext
    val editor = CommonDataKeys.EDITOR.getData(dataContext)
    val project = CommonDataKeys.PROJECT.getData(dataContext)
    val element = dataContext.getData(CommonDataKeys.PSI_ELEMENT)
    element as PsiMethod

    PsiService.test(element)

    // combine them into real path
    NotificationService.notify("REST path copied", e.project!!)
  }
}
