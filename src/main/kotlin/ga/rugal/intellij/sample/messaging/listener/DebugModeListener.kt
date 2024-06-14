package ga.rugal.intellij.sample.messaging.listener

import ga.rugal.intellij.common.service.LogService
import ga.rugal.intellij.common.service.PluginPropertyService
import ga.rugal.intellij.sample.messaging.DebugModeChangeNotifier
import com.intellij.diagnostic.logs.DebugLogLevel
import com.intellij.openapi.diagnostic.Logger

class DebugModeListener : DebugModeChangeNotifier {

  private val LOG = Logger.getInstance(this::class.java)

  override fun update(debugMode: Boolean) {
    val logger = PluginPropertyService.get("debugLoggerName")
    if (debugMode) {
      LOG.debug("Add debug log setting")
      LogService.tryAdd(logger, DebugLogLevel.TRACE)
    } else {
      LOG.debug("Remove debug log setting")
      LogService.tryRemove(logger)
    }
  }
}
