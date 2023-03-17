val pluginName: String by settings

rootProject.name = pluginName

pluginManagement {
  repositories {
    mavenLocal()
    maven {
      url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    gradlePluginPortal()
  }
}
