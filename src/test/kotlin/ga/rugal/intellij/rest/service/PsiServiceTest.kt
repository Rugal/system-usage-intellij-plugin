package ga.rugal.intellij.rest.service

import java.awt.datatransfer.DataFlavor
import ga.rugal.intellij.rest.service.PsiService.httpServletRequest
import ga.rugal.intellij.rest.ui.action.CopyAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.testFramework.EditorTestUtil
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$PROJECT_ROOT/src/test/resources/copy")
class PsiServiceTest : BasePlatformTestCase() {

  fun testOverrideGetWithRequest() {
    myFixture.configureByFile("OverrideGetWithRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/root/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testOverrideGetWithoutRequest() {
    myFixture.configureByFile("OverrideGetWithoutRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testRequestWithoutRequest() {
    myFixture.configureByFile("RequestWithoutRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testRequestWithRequest() {
    myFixture.configureByFile("RequestWithRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/root/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testGetWithRequest() {
    myFixture.configureByFile("GetWithRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/root/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testGetWithoutRequest() {
    myFixture.configureByFile("GetWithoutRequest.java")
    val element = myFixture.elementAtCaret as PsiMethod
    element.httpServletRequest.also {
      assertEquals("/rugal", it.servletPath)
      assertEquals("GET", it.method)
    }
  }

  fun testAction() {
    val psiFile = myFixture.configureByFile("A.java")
    val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)

    val manager = FileEditorManager.getInstance(project)
    val editor: Editor? = manager.openTextEditor(OpenFileDescriptor(project, javaFile.virtualFile), true)

    assertNotNull(editor)

    EditorTestUtil.executeAction(editor!!, CopyAction::class.java.name)

    CopyPasteManager.getInstance().getContents<String>(DataFlavor.stringFlavor).also {
      assertEquals("GET /root/rugal", it)
    }
  }

  override fun getTestDataPath() = "src/test/resources/copy"
}
