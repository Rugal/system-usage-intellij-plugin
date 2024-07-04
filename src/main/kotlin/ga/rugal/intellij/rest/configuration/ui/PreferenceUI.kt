package ga.rugal.intellij.rest.configuration.ui

import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JPanel
import ga.rugal.intellij.common.service.Messages
import ga.rugal.intellij.rest.configuration.Setting

object PreferenceUI : JPanel() {
  private val debugModeCheckBox: JCheckBox = JCheckBox(Messages["ui.debug.mode.text"])

  val debugMode
    get() = debugModeCheckBox.isSelected

  init {
    this.layout = BorderLayout()
    this.add(debugModeCheckBox, BorderLayout.CENTER)

    // initialize all component startup value
    debugModeCheckBox.isSelected = Setting.I.state.debugMode
  }
}
