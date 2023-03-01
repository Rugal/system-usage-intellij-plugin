package ga.rugal.intellij.monitor.ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.swing.JComponent
import kotlin.math.min
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget.WidgetPresentation
import com.intellij.openapi.wm.impl.status.TextPanel
import com.intellij.ui.ClickListener
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.util.concurrency.EdtExecutorService
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.update.Activatable
import com.intellij.util.ui.update.UiNotifyConnector
import ga.rugal.intellij.common.service.PluginPropertyService
import oshi.SystemInfo

class SystemUsagePanel : TextPanel(), CustomStatusBarWidget, Activatable {
  private val LOG = Logger.getInstance(this::class.java)

  private val myUsedColor: Color = JBColor.namedColor("MemoryIndicator.usedBackground", JBColor(Gray._185, Gray._110))
  private val myUnusedColor: Color =
    JBColor.namedColor("MemoryIndicator.allocatedBackground", JBColor(Gray._215, Gray._90))
  private val myMaxMemory = min(Runtime.getRuntime().maxMemory() shr 20, 9999)
  private var myLastAllocated: Long = -1
  private var myLastUsed: Long = -1
  private var myFuture: ScheduledFuture<*>? = null
  private var showPercentage = true

  init {
    isFocusable = false
    this.setTextAlignment(CENTER_ALIGNMENT)
    object : ClickListener() {
      override fun onClick(event: MouseEvent, clickCount: Int): Boolean {
        System.gc()
        showPercentage = !showPercentage
        updateState()
        return true
      }
    }.installOn(this, true)
    border = JBUI.Borders.empty(0, 2)
    updateUI()
    UiNotifyConnector(this, this)
  }

  override fun showNotify() {
    myFuture = EdtExecutorService.getScheduledExecutorInstance()
      .scheduleWithFixedDelay({ updateState() }, 1, 5, TimeUnit.SECONDS)
  }

  override fun hideNotify() {
    if (myFuture != null) {
      myFuture!!.cancel(true)
      myFuture = null
    }
  }

  override fun install(statusBar: StatusBar) {}
  override fun dispose() {}
  override fun getPresentation(): WidgetPresentation? = null

  override fun ID(): String = WIDGET_ID

  fun setShowing(showing: Boolean) {
    if (showing != isVisible) {
      isVisible = showing
      revalidate()
    }
  }

  override fun getComponent(): JComponent = this

  override fun paintComponent(g: Graphics) {
    val size: Dimension = size
    val barWidth = size.width
    val rt = Runtime.getRuntime()
    val maxMem = rt.maxMemory()
    val allocatedMem = rt.totalMemory()
    val unusedMem = rt.freeMemory()
    val usedMem = allocatedMem - unusedMem
    val usedBarLength = (barWidth * usedMem / maxMem).toInt()
    val allocatedBarLength = (barWidth * allocatedMem / maxMem).toInt()

    // background
    g.color = UIUtil.getPanelBackground()
    g.fillRect(0, 0, barWidth, size.height)

    // gauge (allocated)
    g.color = myUnusedColor
    g.fillRect(0, 0, allocatedBarLength, size.height)

    // gauge (used)
    g.color = myUsedColor
    g.fillRect(0, 0, usedBarLength, size.height)

    //text
    super.paintComponent(g)
  }

  private fun updateState() {
    if (!isShowing) {
      return
    }
    val rt = Runtime.getRuntime()
    val maxMem = rt.maxMemory() shr 20
    val allocatedMem = rt.totalMemory() shr 20
    val usedMem = allocatedMem - (rt.freeMemory() shr 20)

    val text = if (showPercentage) "%.1f".format((usedMem * 100.0F / maxMem))
    else PluginPropertyService.get("memory.usage.panel.message.text", usedMem, maxMem)

    if (allocatedMem != myLastAllocated || usedMem != myLastUsed || text != getText()) {
      myLastAllocated = allocatedMem
      myLastUsed = usedMem
      setText(text)
      toolTipText = PluginPropertyService.get("memory.usage.panel.message.tooltip", maxMem, allocatedMem, usedMem)
    }
  }

  companion object {
    const val WIDGET_ID = "rugal.monitor.status-bar.widget"
  }
}
