package ga.rugal.intellij.rest.exception

import java.awt.Component
import ga.rugal.intellij.common.service.Messages
import ga.rugal.intellij.rest.configuration.Setting
import ga.rugal.intellij.rest.service.github.IssueService
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

  override fun getReportActionText(): String = Messages["ui.error.report.action.text"]

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<in SubmittedReportInfo>
  ): Boolean {
    ProgressManager.getInstance().run {
      runProcessWithProgressAsynchronously(
        object : Task.Backgroundable(null, Messages["ui.error.report.progress.text"], false) {
          override fun run(indicator: ProgressIndicator) {
            if (null != Setting.I.state.repository) {
              IssueService.create(events[0], additionalInfo ?: "Plugin user report")
            }
          }
        },
        BackgroundableProcessIndicator(null, Messages["ui.error.report.reporting.text"], "cancel", "tooltips", false)
      )
    }
    return true
  }
}
