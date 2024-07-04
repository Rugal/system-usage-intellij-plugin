package ga.rugal.intellij.rest.exception

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
import ga.rugal.intellij.rest.service.github.IssueService

class ErrorSubmitter : ErrorReportSubmitter() {
  private val LOG = Logger.getInstance(this::class.java)

  override fun getReportActionText(): String = "Submit to Github"

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<in SubmittedReportInfo>
  ): Boolean {
    ProgressManager.getInstance().run {
      runProcessWithProgressAsynchronously(
        object : Task.Backgroundable(null, "Report to GitHub issue", false) {
          override fun run(indicator: ProgressIndicator) {
            IssueService.create(events[0], additionalInfo ?: "Plugin user report")
          }
        },
        BackgroundableProcessIndicator(null, "Reporting", "cancel", "tooltips", false)
      )
    }
    return true
  }
}
