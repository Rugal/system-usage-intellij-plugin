val pluginName: String by settings

rootProject.name = pluginName

pluginManagement {
  repositories {
    mavenLocal()
    // mirror for gradle plugin in China
    maven("https://maven.aliyun.com/repository/gradle-plugin")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    gradlePluginPortal()
  }
}
