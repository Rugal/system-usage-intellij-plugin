package ga.rugal.intellij.rest.configuration

import java.util.Base64
import ga.rugal.intellij.common.service.PluginPropertyService
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.xmlb.annotations.Transient
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder

@Service(Service.Level.PROJECT)
@State(name = "RestResolverSetting", storages = [(Storage(value = StoragePathMacros.WORKSPACE_FILE))])
class Setting(val project: Project) : PersistentStateComponent<Setting.State> {
  private val LOG = Logger.getInstance(this::class.java)

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
    private val github: GitHub by lazy {
      GitHubBuilder()
        .withOAuthToken(
          Base64
            .getDecoder()
            .decode(PluginPropertyService.get("github.token.base64"))
            .decodeToString()
            .trim()
        )
        .build()
    }

    private val repo: GHRepository by lazy {
      github.getRepositoryById(PluginPropertyService.get("github.repository.id").toLong())
    }

    @get:Transient
    val repository: GHRepository? = runCatching { this.repo }.getOrNull()
  }
}
