import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  java
  // kotlin running on JVM, this defines kotlin version as well
  kotlin("jvm") version "1.8.0"
  // to build into IntelliJ plugin
  id("org.jetbrains.intellij") version "1.13.0"
  // Gradle Changelog Plugin
  id("org.jetbrains.changelog") version "2.0.0"
  // release plugin
  id("net.researchgate.release") version "3.0.2"

}

group = properties("group")

// Configure project's dependencies
repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("com.github.oshi:oshi-core:6.4.0")

  implementation("org.slf4j:slf4j-api:2.0.5")
  implementation("org.slf4j:slf4j-simple:2.0.6")

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
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
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
}

if (hasProperty("buildScan")) {
  extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
  }
}

tasks {
  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  patchPluginXml {
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

  buildSearchableOptions {
    enabled = false
  }

  // Configure UI tests plugin
  // Read more: https://github.com/JetBrains/intellij-ui-test-robot
  runIdeForUiTests {
    systemProperty("robot-server.port", "8082")
    systemProperty("ide.mac.message.dialogs.as.sheets", "false")
    systemProperty("jb.privacy.policy.text", "<!--999.999-->")
    systemProperty("jb.consents.confirmation.enabled", "false")
  }

  release {
    failOnCommitNeeded.set(true)
    failOnSnapshotDependencies.set(true)
    tagTemplate.set("v${version}")
    versionPropertyFile.set("gradle.properties")
    snapshotSuffix.set("-SNAPSHOT")

    git {
      requireBranch.set("master")
      pushToRemote.set("origin")
      signTag.set(false)
    }
  }
}
