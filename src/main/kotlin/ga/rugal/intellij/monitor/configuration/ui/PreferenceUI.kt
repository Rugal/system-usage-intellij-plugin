package ga.rugal.intellij.monitor.configuration.ui

import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JPanel
import ga.rugal.intellij.monitor.configuration.Setting

object PreferenceUI : JPanel() {
  private val debugModeCheckBox: JCheckBox = JCheckBox("调试模式")

  val debugMode
    get() = debugModeCheckBox.isSelected

  init {
    this.layout = BorderLayout()
    this.add(debugModeCheckBox, BorderLayout.CENTER)

    // initialize all component startup value
    debugModeCheckBox.isSelected = Setting.I.state.debugMode
  }
}
