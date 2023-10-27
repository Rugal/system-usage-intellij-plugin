package ga.rugal.intellij.sample.configuration

import javax.swing.Icon
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil

object Icon {

  val ROCKET_ICON = resizeIcon("/image/rocket.svg")

  val LIGHTNING_ICON = resizeIcon("/image/lightning_configurationtype.svg")

  private fun load(path: String) = IconLoader.getIcon(path, javaClass)

  private fun resizeIcon(path: String): Icon {
    return IconUtil.resizeSquared(load(path), 16)
  }
}
