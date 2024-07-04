package ga.rugal.intellij.common.service

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import org.jetbrains.annotations.NonNls

@NonNls
private const val BUNDLE_MESSAGE = "messages"

object Messages : DynamicBundle(BUNDLE_MESSAGE) {

  operator fun get(@PropertyKey(resourceBundle = BUNDLE_MESSAGE) key: String, vararg params: Any): String =
    getMessage(key, *params)

  @Suppress("SpreadOperator", "unused")
  fun getPointer(@PropertyKey(resourceBundle = BUNDLE_MESSAGE) key: String, vararg params: Any) =
    getLazyMessage(key, *params)
}
