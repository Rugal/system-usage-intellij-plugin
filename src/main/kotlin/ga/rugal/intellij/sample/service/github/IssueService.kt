package ga.rugal.intellij.sample.service.github

import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.Logger
import ga.rugal.intellij.sample.configuration.Setting
import org.kohsuke.github.GHIssue

object IssueService {
  private val LOG = Logger.getInstance(this::class.java)

  fun create(event: IdeaLoggingEvent, comment: String): GHIssue {
    return GHIssue()
//    return Setting.I.state.repository
//      .createIssue(comment)
//      .body(
//        """
//```
//$event
//```
//      """.trim()
//      )
//      .assignee("Rugal")
//      .label("report")
//      .create()
  }
}
