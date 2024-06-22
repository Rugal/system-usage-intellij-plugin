import net.researchgate.release.ReleaseExtension
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // kotlin running on JVM, this defines kotlin version as well
  kotlin("jvm") version "1.9.0"
  // to build into IntelliJ plugin
  id("org.jetbrains.intellij") version "1.17.3"
  // Gradle Changelog Plugin
  id("org.jetbrains.changelog") version "2.0.0"
  // release plugin
  id("net.researchgate.release") version "3.0.2"
}

// Configure project's dependencies
repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")

  implementation("org.kohsuke:github-api:1.321")

  testImplementation(kotlin("test"))
}

sourceSets {
  main {
    kotlin {
    }
  }
}

// Set the JVM language level used to compile sources and generate files - Java 11 is required since 2020.3
kotlin {
  jvmToolchain(17)
}

// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  pluginName.set(properties("pluginName"))
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
  updateSinceUntilBuild.set(true)
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
  version.set(project.version.toString())
  groups.empty()
}

if (hasProperty("buildScan")) {
  extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
  }
}

configure<ReleaseExtension> {
  tagTemplate.set("v${version}")
  ignoredSnapshotDependencies.set(listOf("net.researchgate:gradle-release"))
  with(git) {
    requireBranch.set("master")
  }
}

tasks {
  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  initializeIntelliJPlugin {
    selfUpdateCheck.set(false)
  }

  patchPluginXml {
    pluginId.set("${properties("group")}.${properties("pluginName")}")
    version.set(project.version.toString())
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
    pluginDescription.set(
      projectDir.resolve("README.md").readText().lines().run {
        val start = "<!-- Plugin description -->"
        val end = "<!-- Plugin description end -->"

        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end))
      }.joinToString("\n").run { markdownToHTML(this) }
    )

    // Get the latest available change notes from the changelog file
    changeNotes.set(provider {
      changelog.renderItem(
        changelog
          .getUnreleased()
          .withHeader(false)
          .withEmptySections(false),
        Changelog.OutputType.HTML
      )
    })
  }

  test {
    testLogging {
      showStandardStreams = true
      events("PASSED", "SKIPPED", "FAILED")
    }
  }

  buildSearchableOptions {
    enabled = false
  }

  val copyIdeaProperties by registering(Copy::class) {
    dependsOn(build)
    from(layout.projectDirectory.file("idea.properties"))
    into(layout.buildDirectory.dir("idea-sandbox/config"))
  }
}
