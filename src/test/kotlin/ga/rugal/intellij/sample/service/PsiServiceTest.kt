package ga.rugal.intellij.sample.service

import ga.rugal.intellij.sample.service.PsiService.httpServletRequest
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.testFramework.EditorTestUtil
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$PROJECT_ROOT/src/test/resources/copy")
class PsiServiceTest : BasePlatformTestCase() {

  fun testHasRequestAnnotation() {
    myFixture.configureByFile("A.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/root/rugal", it.servletPath)
    }
  }

  fun testAction() {
    val psiFile = myFixture.configureByFile("A.java")
    val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)

    val manager = FileEditorManager.getInstance(project)
    val editor: Editor? = manager.openTextEditor(OpenFileDescriptor(project, javaFile.virtualFile), true)

    assertNotNull(editor)

    EditorTestUtil.executeAction(editor!!, "ga.rugal.intellij.sample.ui.action.RugalAction")
  }


  override fun getTestDataPath() = "src/test/resources/copy"
}
