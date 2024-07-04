package ga.rugal.intellij.rest.configuration

import javax.swing.Icon
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil

object Icon {

  val R_ICON = resizeIcon("/image/r.svg")

  val PARTICLE_ICON = resizeIcon("/image/particle.svg", 14)

  private fun load(path: String) = IconLoader.getIcon(path, javaClass)

  private fun resizeIcon(path: String, size: Int = 16): Icon = IconUtil.resizeSquared(load(path), size)
}
