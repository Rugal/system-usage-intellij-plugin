package ga.rugal.intellij.common.service

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.diagnostic.Logger

object VersionService {
  private val LOG = Logger.getInstance(this::class.java)

  val platformVersion: String
    get() = ApplicationInfo.getInstance().build.toString()

  fun pluginVersion(name: String): String = PluginManagerCore.plugins.first { it.name.contains(name) }.version
}
