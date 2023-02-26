package ga.rugal.intellij.common.service

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import org.jetbrains.annotations.NonNls

@NonNls
private const val BUNDLE = "plugin"

object PluginPropertyService : DynamicBundle(BUNDLE) {

  @Suppress("SpreadOperator")
  @JvmStatic
  fun get(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String = getMessage(key, *params)

  @Suppress("SpreadOperator", "unused")
  @JvmStatic
  fun getPointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
    getLazyMessage(key, *params)
}
