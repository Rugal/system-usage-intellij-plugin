package ga.rugal.intellij.monitor.service

import com.intellij.openapi.diagnostic.Logger
import ga.rugal.intellij.common.service.PluginPropertyService
import oshi.SystemInfo

internal object SystemUsageService {
  private val LOG = Logger.getInstance(this::class.java)

  private val system = SystemInfo()

  /**
   * Get memory in G.
   */
  fun getMemory(bit: Int = 30): Availability = system.hardware.memory.let {
    val total = it.total shr bit
    val available = it.available shr bit
    val used = total - available
    Availability(used.toInt(), available.toInt(), total.toInt())
  }

  fun getMemoryText(bit: Int = 30): String = this.getMemory(bit).let {
    PluginPropertyService.get("memory.usage.panel.message.text", it.used, it.total)
  }

  /**
   * Get FileSystem in G.
   */
  fun getFileSystem(bit: Int = 30): List<Availability> = system.operatingSystem.fileSystem.fileStores.let {
    it.map { store ->
      val available = store.freeSpace shr bit
      val total = store.totalSpace shr bit
      val used = total - available
      Availability(used.toInt(), available.toInt(), total.toInt(), store.mount)
    }
  }

  fun getFileSystemText(bit: Int = 30): String = this.getFileSystem(bit).let {
    """
<table>
${it.joinToString(separator = " ") { i -> "<tr><td>${i.name}</td><td>${i.used} / ${i.total} G</td></tr>" }}
</table>
""".trimIndent()
  }
}

internal data class Availability(
  val used: Int,
  val free: Int,
  val total: Int,
  val name: String = ""
)
