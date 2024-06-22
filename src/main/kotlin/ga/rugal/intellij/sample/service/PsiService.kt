package ga.rugal.intellij.sample.service

import ga.rugal.intellij.sample.entity.DefaultHttpServletRequest
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiMethod
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

object PsiService {
  private val LOG = Logger.getInstance(this::class.java)

  private val annotations: Set<String> = setOf(
    GetMapping::class.java.simpleName,
    DeleteMapping::class.java.simpleName,
    PutMapping::class.java.simpleName,
    PostMapping::class.java.simpleName,
    PatchMapping::class.java.simpleName,
    RequestMapping::class.java.simpleName,
  )

  @Throws(NoSuchElementException::class)
  fun hasRequestAnnotation(method: PsiMethod): Boolean = runCatching {
    this.getRequestAnnotation(method)
    true
  }.getOrDefault(false)

  @get:Throws(NoSuchElementException::class)
  private val PsiMethod.targetAnnotation: PsiAnnotation
    get() = this.annotations.first { PsiService.annotations.contains(it.qualifiedName!!) }

  /**
   * Get {@code RequestMapping} or {@code XxxMapping} annotation from the given method.
   */
  @Throws(NoSuchElementException::class)
  fun getRequestAnnotation(method: PsiMethod): PsiAnnotation {
    try {
      // find target annotation from current method
      return method.targetAnnotation
    } catch (e: NoSuchElementException) {
      // rethrow if no @Override annotation
      if (method.getAnnotation(Override::class.java.name) == null) throw e
    }
    // keep searching if method has @Override annotation
    return method.findSuperMethods().firstNotNullOf { runCatching { it.targetAnnotation }.getOrNull() }
  }

  private fun getRequestMethod(annotation: PsiAnnotation): String {
    // for RequestMapping
    fun PsiAnnotation.method(): String = this.findAttributeValue("method")?.text ?: HttpMethod.GET.name()

    // for XXXMapping
    fun PsiAnnotation.simpleName(): String = this.qualifiedName?.let { it.substring(it.lastIndexOf(".") + 1) }
      ?: GetMapping::class.java.simpleName

    fun String.method(): String = this.substring(0, this.indexOf("Mapping")).uppercase()
    // determine if its RequestMapping
    return if (annotation.qualifiedName == RequestMapping::class.java.name)
      annotation.method()
    else
      annotation.simpleName().method()
  }

  private val PsiAnnotation.path: String
    get() = (this.findAttributeValue("value")?.text ?: this.findAttributeValue("path")?.text ?: "/").replace("\"", "")

  @get:Throws(NoSuchElementException::class)
  val PsiMethod.httpServletRequest: HttpServletRequest
    get() {
      // get request information from method itself, it can't try to find it from the super method, otherwise throw exception
      val annotation: PsiAnnotation = getRequestAnnotation(this)
      val request = DefaultHttpServletRequest(getRequestMethod(annotation), annotation.path)
      // if this class has no base path, get base path from class, get class annotation
      val classAnnotation =
        this.containingClass!!.annotations.first { it.qualifiedName == RequestMapping::class.java.simpleName }
          ?: return request
      // concatenate path
      return request.copy(
        path = "${classAnnotation.path}${request.servletPath}",
      )
    }
}
