package ga.rugal.intellij.monitor.exception

import java.awt.Component
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.util.Consumer

class ErrorSubmitter : ErrorReportSubmitter() {
  private val LOG = Logger.getInstance(this::class.java)

  override fun getReportActionText(): String = "Submit to Rugal Bernstein"

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<in SubmittedReportInfo>
  ): Boolean {
    ProgressManager.getInstance().run {
      runProcessWithProgressAsynchronously(
        object : Task.Backgroundable(null, "问题上报至疑修库", false) {
          override fun run(indicator: ProgressIndicator) {
          }
        },
        BackgroundableProcessIndicator(null, "问题上报中", "cancel", "tooltips", false)
      )
    }
    return true
  }
}
