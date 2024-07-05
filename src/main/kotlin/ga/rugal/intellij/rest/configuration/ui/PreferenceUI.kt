package ga.rugal.intellij.rest.configuration.ui

import javax.swing.JCheckBox
import javax.swing.JPanel
import ga.rugal.intellij.common.service.Messages
import ga.rugal.intellij.rest.configuration.Setting

object PreferenceUI : JPanel() {
  private val debugModeCheckBox: JCheckBox = JCheckBox(Messages["ui.debug.mode.text"])

  var debugMode: Boolean
    get() = debugModeCheckBox.isSelected
    set(value) {
      debugModeCheckBox.isSelected = value
    }

  init {
    this.add(debugModeCheckBox)

    // initialize all component startup value
    debugModeCheckBox.isSelected = Setting.I.state.debugMode
  }
}
