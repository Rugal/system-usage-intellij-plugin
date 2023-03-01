package ga.rugal.intellij.monitor.service

import com.intellij.openapi.diagnostic.Logger
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
    return Availability(used, available, total)
  }

  /**
   * Get FileSystem in M.
   */
  fun getFileSystem(bit: Int = 30): List<Availability> = system.operatingSystem.fileSystem.fileStores.let {
    it.map { store ->
      val available = store.freeSpace shr bit
      val total = store.totalSpace shr bit
      val used = total - available
      Availability(used, available, total, store.mount)
    }
  }
}

internal data class Availability(
  val used: Long,
  val free: Long,
  val total: Long,
  val name: String = ""
)
