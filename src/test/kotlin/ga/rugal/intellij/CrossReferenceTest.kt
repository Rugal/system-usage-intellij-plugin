package ga.rugal.intellij

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.psi.xml.XmlFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.PsiErrorElementUtil

class CrossReferenceTest : BasePlatformTestCase() {

  fun testGood() {
    val psiFile = myFixture.configureByText(XmlFileType.INSTANCE, "<foo>bar</foo>")
    val xmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

    assertFalse(PsiErrorElementUtil.hasErrors(project, xmlFile.virtualFile))
    println(project.basePath)

    assertNotNull(xmlFile.rootTag)

    assertEquals("bar", xmlFile.rootTag!!.value.text)
  }

  fun testBad() {
    val psiFile = myFixture.configureByText(XmlFileType.INSTANCE, "<foo>bar</foo>")
    val xmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

    assertFalse(PsiErrorElementUtil.hasErrors(project, xmlFile.virtualFile))
    println(project.basePath)

    assertNotNull(xmlFile.rootTag)

    assertEquals("bar", xmlFile.rootTag!!.value.text)
  }
}
