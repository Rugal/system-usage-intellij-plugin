val pluginName: String by settings

rootProject.name = pluginName

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
  }
}
