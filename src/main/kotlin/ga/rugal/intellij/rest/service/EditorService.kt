package ga.rugal.intellij.rest.service

import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project

object EditorService {
  fun open(project: Project, title: String) {
    val classLoader = this::class.java.classLoader
    val content: String = classLoader.getResourceAsStream("index.html")
      ?.use { BufferedReader(InputStreamReader(it)).use(BufferedReader::readText) }!!

    HTMLEditorProvider.openEditor(
      project,
      title,
      content.trimIndent()
    )
  }
}
