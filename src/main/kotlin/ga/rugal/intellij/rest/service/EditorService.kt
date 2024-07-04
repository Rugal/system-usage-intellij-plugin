package ga.rugal.intellij.rest.service

import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project

object EditorService {
  fun open(project: Project, title: String) {
    HTMLEditorProvider.openEditor(
      project,
      title,
      """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Rugal</title>
</head>
<body>
Rugal Bernstein
</body>
</html>
      """.trimIndent()
    )
  }
}
