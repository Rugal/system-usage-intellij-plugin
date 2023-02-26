package ga.rugal.intellij.common.service

import com.intellij.diagnostic.DebugLogManager
import com.intellij.openapi.diagnostic.Logger

object LogService {
  private val LOG = Logger.getInstance(this::class.java)

  private fun remove(name: String) {
    val manager = DebugLogManager.getInstance()
    val newList = manager.getSavedCategories().toMutableList()
    LOG.trace("Remove [${name}] from log setting")
    newList.removeIf { it.category == name }
    manager.saveCategories(newList)
    LOG.debug("Log Setting updated")
  }

  private fun search(name: String): DebugLogManager.Category? {
    return DebugLogManager.getInstance().getSavedCategories().firstOrNull { it.category == name }
  }

  private fun add(name: String, level: DebugLogManager.DebugLogLevel) {
    val manager = DebugLogManager.getInstance()
    LOG.trace("Add [${name}:${level}] to log setting")
    val newList = manager.getSavedCategories().toMutableList()
    newList.add(DebugLogManager.Category(name, level))
    manager.saveCategories(newList)
    LOG.debug("Log Setting updated")
  }

  /**
   * Add new logger or update existing one if level does not match.
   */
  fun tryAdd(name: String, level: DebugLogManager.DebugLogLevel) {
    val category: DebugLogManager.Category? = search(name)
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
