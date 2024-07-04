package ga.rugal.intellij.rest.service

import ga.rugal.intellij.rest.entity.DefaultHttpServletRequest
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
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
    getRequestAnnotation(method)
    true
  }.getOrDefault(false)

  @get:Throws(NoSuchElementException::class)
  private val PsiMethod.targetAnnotation: PsiAnnotation
    get() = this.annotations.first { PsiService.annotations.contains(it.qualifiedName!!.substringAfterLast(".")) }

  /**
   * Get {@code RequestMapping} or {@code XxxMapping} annotation from the given method.
   */
  @Throws(NoSuchElementException::class)
  fun getRequestAnnotation(method: PsiMethod): PsiAnnotation {
    try {
      // find target annotation from current method
      LOG.trace("Try look for request annotation in current method")
      return method.targetAnnotation
    } catch (e: NoSuchElementException) {
      LOG.trace("Current method does not have request annotation")
      // rethrow if no @Override annotation
      if (method
          .annotations
          .none { it.qualifiedName?.substringAfterLast(".") == Override::class.java.simpleName }
      ) {
        LOG.info("Current method does not have @Override annotation")
        throw e
      }
    }
    // keep searching if method has @Override annotation
    LOG.trace("Try look for request annotation in super methods")
    return method.findSuperMethods().firstNotNullOf { runCatching { it.targetAnnotation }.getOrNull() }
  }

  private fun getRequestMethod(annotation: PsiAnnotation): String {
    // for RequestMapping
    fun PsiAnnotation.method(): String = this.findAttributeValue("method")?.text
      ?.let { if (it.contains(".")) it.substringAfterLast(".") else it } // could be RequestMethod.GET etc.,
      ?: HttpMethod.GET.name()

    // for XXXMapping
    fun PsiAnnotation.simpleName(): String = this.qualifiedName?.substringAfterLast(".")
      ?: GetMapping::class.java.simpleName

    fun String.method(): String = this.substringBefore("Mapping").uppercase()
    // determine if its RequestMapping
    return if (annotation.qualifiedName == RequestMapping::class.java.simpleName) {
      LOG.trace("Get HTTP method from [RequestMapping::class]")
      annotation.method()
    } else {
      LOG.trace("Get HTTP method from [XxxMapping::class]")
      annotation.simpleName().method()
    }
  }

  private val PsiAnnotation.path: String
    get() = (this.findAttributeValue("value")?.text ?: this.findAttributeValue("path")?.text ?: "/").replace("\"", "")

  private val PsiClass.requestMappingAnnotation: PsiAnnotation?
    get() = RequestMapping::class.java.simpleName.let { name ->
      // check current class
      LOG.trace("Try to get RequestMapping annotation from class")
      if (this.hasAnnotation(name)) {
        LOG.debug("Try to get RequestMapping from ${this.name} class")
        this.getAnnotation(name)
        // check base class and interfaces only one level
      } else {
        LOG.debug("Try to get RequestMapping from supers")
        this.supers.firstOrNull { it.hasAnnotation(name) }?.getAnnotation(name)
      }
    }

  @get:Throws(NoSuchElementException::class)
  val PsiMethod.httpServletRequest: HttpServletRequest
    get() {
      // get request information from method itself, it can't try to find it from the super method, otherwise throw exception
      val annotation: PsiAnnotation = getRequestAnnotation(this)
      val request = DefaultHttpServletRequest(getRequestMethod(annotation), annotation.path)
      // if this class has no base path, get base path from class, get class annotation
      val classAnnotation = this.containingClass!!.requestMappingAnnotation ?: return request
      // concatenate path
      LOG.trace("Concatenate class and method REST mapping")
      return request.copy(
        path = "${classAnnotation.path}${request.servletPath}",
      )
    }
}
