package ga.rugal.intellij.sample.ui.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod

private const val PREFIX = "org.springframework.web.bind.annotation"

class RugalAction : AnAction("Copy REST Path") {
  private val LOG = Logger.getInstance(this::class.java)

  private val annotations = listOf(
    "GetMapping",
    "DeleteMapping",
    "PostMapping",
    "PutMapping",
    "PatchMapping",
    "RequestMapping",
  )

  private fun hasRequestAnnotation(method: PsiMethod): Boolean = this.annotations.any {
    method.getAnnotation("${PREFIX}.${it}") != null
  }

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.isEnabled = when (val element = e.dataContext.getData(CommonDataKeys.PSI_ELEMENT)) {
      is PsiMethod -> this.hasRequestAnnotation(element)
      else -> false
    }
  }

  override fun actionPerformed(e: AnActionEvent) {
    val dataContext = e.dataContext
    val editor = CommonDataKeys.EDITOR.getData(dataContext)
    val project = CommonDataKeys.PROJECT.getData(dataContext)
    val element = dataContext.getData(CommonDataKeys.PSI_ELEMENT)
    element as PsiMethod

    // get its class, with the @RequestMapping annotation
    val containingClass: PsiClass? = element.containingClass

    // combine them into real path
  }
}
