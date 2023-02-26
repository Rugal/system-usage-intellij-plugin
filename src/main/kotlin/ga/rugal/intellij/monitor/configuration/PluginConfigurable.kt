package ga.rugal.intellij.monitor.configuration

import javax.swing.JComponent
import ga.rugal.intellij.common.service.PluginPropertyService
import ga.rugal.intellij.monitor.configuration.ui.PreferenceUI
import ga.rugal.intellij.monitor.messaging.DebugModeChangeNotifier
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.options.Configurable

class PluginConfigurable : Configurable {
  private val LOG = Logger.getInstance(this::class.java)

  override fun createComponent(): JComponent = PreferenceUI

  override fun isModified(): Boolean = Setting.I.state.debugMode != PreferenceUI.debugMode

  override fun getDisplayName(): String = PluginPropertyService.get("name")

  override fun apply() {
    LOG.debug("Apply storage ${Setting.I.state.debugMode} UI ${PreferenceUI.debugMode}")
    Setting.I.state.debugMode = PreferenceUI.debugMode
    val bus = ApplicationManager.getApplication().messageBus
    LOG.debug("Publish message for [debugMode]")
    bus.syncPublisher(DebugModeChangeNotifier.DebugModeTopic).update(Setting.I.state.debugMode)
  }
}
