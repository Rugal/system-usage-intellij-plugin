package ga.rugal.intellij.monitor.configuration

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager

@State(name = "SystemStatusMonitorSetting", storages = [(Storage(value = StoragePathMacros.WORKSPACE_FILE))])
class Setting(val project: Project) : PersistentStateComponent<Setting.State> {
  companion object {
    val I: Setting
      get() = ProjectManager.getInstance().defaultProject.getService(Setting::class.java)
  }

  private var myState: State = State()

  override fun getState(): State = this.myState

  override fun loadState(state: State) {
    this.myState = state
  }

  data class State(
    var debugMode: Boolean = false,
  ) {
  }
}
