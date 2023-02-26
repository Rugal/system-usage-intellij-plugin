package ga.rugal.intellij.monitor.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class SystemStatusBarFactory : StatusBarWidgetFactory {
  override fun getId(): String = "rugal.monitor.status-bar.factory"

  override fun getDisplayName(): String = "System Resource Usage"

  override fun isAvailable(project: Project): Boolean = true

  override fun createWidget(project: Project): StatusBarWidget {
    return SystemStatusBar()
  }

  override fun disposeWidget(widget: StatusBarWidget) {
  }

  override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
