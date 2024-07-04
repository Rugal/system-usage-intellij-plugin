package ga.rugal.intellij.rest.messaging

import com.intellij.util.messages.Topic

interface DebugModeChangeNotifier {

  companion object {
    val DebugModeTopic = Topic("调试模式变更", DebugModeChangeNotifier::class.java)
  }

  fun update(debugMode: Boolean)
}
