package ga.rugal.intellij.monitor.ui

import java.awt.Component
import java.awt.event.MouseEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import oshi.SystemInfo

class SystemStatusBar : StatusBarWidget {
  private val LOG = Logger.getInstance(this::class.java)

  override fun dispose() {
  }

  override fun ID(): String = "rugal.monitor.status-bar.widget"

  override fun install(statusBar: StatusBar) {
    LOG.info("Invoke [install]")
  }

  override fun getPresentation(): StatusBarWidget.TextPresentation {
    return object : StatusBarWidget.TextPresentation {
      override fun getTooltipText(): String = "Click to switch"

      override fun getClickConsumer(): Consumer<MouseEvent>? {
        LOG.info("Invoke [getClickConsumer]")
        return null
      }

      override fun getText(): String {
        val gigaByte = 1024 * 1024 * 1024
        val memoryText = SystemInfo().hardware.memory.let {
          val available: Double = ((it.total - it.available) / gigaByte).toDouble()
          val total: Double = (it.total / gigaByte).toDouble()
          val availableString = "%.2f".format(available)
          val totalString = "%.2f".format(total)
          "$availableString / $totalString"
        }
        return "Memory: $memoryText"
      }

      override fun getAlignment(): Float {
        return Component.LEFT_ALIGNMENT
      }
    }
  }
}
