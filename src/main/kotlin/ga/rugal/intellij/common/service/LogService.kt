package ga.rugal.intellij.common.service

import com.intellij.diagnostic.logs.DebugLogLevel
import com.intellij.diagnostic.logs.LogCategory
import com.intellij.diagnostic.logs.LogLevelConfigurationManager
import com.intellij.openapi.diagnostic.Logger

object LogService {
  private val LOG = Logger.getInstance(this::class.java)

  private fun remove(name: String) {
    val manager = LogLevelConfigurationManager.getInstance()
    val newList = manager.getCategories().toMutableList()
    LOG.trace("Remove [${name}] from log setting")
    newList.removeIf { it.category == name }
    manager.setCategories(newList)
    LOG.debug("Log Setting updated")
  }

  private fun search(name: String): LogCategory? =
    LogLevelConfigurationManager.getInstance().getCategories().firstOrNull { it.category == name }

  private fun add(name: String, level: DebugLogLevel) {
    val manager = LogLevelConfigurationManager.getInstance()
    LOG.trace("Add [${name}:${level}] to log setting")

    val newList = manager.getCategories().toMutableList()
    newList.add(LogCategory(name, level))
    manager.setCategories(newList)
    LOG.debug("Log Setting updated")
  }

  /**
   * Add new logger or update existing one if level does not match.
   */
  fun tryAdd(name: String, level: DebugLogLevel) {
    val category: LogCategory? = search(name)
    if (null != category) {
      LOG.trace("Log setting for [${name}] does exist")
      if (category.level == level) {
        LOG.trace("Log setting for [${name}:${level}] does exist as well, do nothing")
        return
      }
      LOG.info("Log settings for [${name}] does exist but with different level, remove it")
      remove(name)
    }
    add(name, level)
  }

  /**
   * Remove the named logger if it exists.
   */
  fun tryRemove(name: String) {
    search(name)?.let {
      remove(name)
    }
  }
}
