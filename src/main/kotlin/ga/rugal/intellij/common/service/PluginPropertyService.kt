package ga.rugal.intellij.common.service

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import org.jetbrains.annotations.NonNls

@NonNls
private const val BUNDLE_PLUGIN = "plugin"

object PluginPropertyService : DynamicBundle(BUNDLE_PLUGIN) {

  @Suppress("SpreadOperator")
  fun get(@PropertyKey(resourceBundle = BUNDLE_PLUGIN) key: String, vararg params: Any): String = getMessage(key, *params)

  @Suppress("SpreadOperator", "unused")
  fun getPointer(@PropertyKey(resourceBundle = BUNDLE_PLUGIN) key: String, vararg params: Any) =
    getLazyMessage(key, *params)
}
