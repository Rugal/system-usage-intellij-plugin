package ga.rugal.intellij.sample.service

import ga.rugal.intellij.sample.entity.DefaultHttpServletRequest
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

  private val annotations: Set<String> = setOf(
    GetMapping::class.java.name,
    DeleteMapping::class.java.name,
    PutMapping::class.java.name,
    PostMapping::class.java.name,
    PatchMapping::class.java.name,
    RequestMapping::class.java.name,
  )

  @Throws(NoSuchElementException::class)
  fun hasRequestAnnotation(method: PsiMethod): Boolean = runCatching {
    this.getRequestAnnotation(method)
    true
  }.getOrDefault(false)

  @Throws(NoSuchElementException::class)
  fun getRequestAnnotation(method: PsiMethod): PsiAnnotation =
    method.annotations.first { this.annotations.contains(it.qualifiedName!!) }

  @Throws(NoSuchElementException::class)
  fun test(element: PsiMethod): HttpServletRequest {
    val annotation: PsiAnnotation = this.getRequestAnnotation(element)
    // determine if its RequestMapping
    if (annotation.qualifiedName == RequestMapping::class.java.name) {
      // method, path, content type
      val method = annotation.findAttributeValue("method") ?: annotation.findAttributeValue("method")
      val path = annotation.findAttributeValue("value") ?: annotation.findAttributeValue("path")

      val request = DefaultHttpServletRequest(method?.text ?: HttpMethod.GET.name(), path?.text ?: "")
    } else {
      val method = when (annotation.qualifiedName) {
        GetMapping::class.java.name -> HttpMethod.GET.name()
        DeleteMapping::class.java.name -> HttpMethod.DELETE.name()
        PutMapping::class.java.name -> HttpMethod.PUT.name()
        PostMapping::class.java.name -> HttpMethod.POST.name()
        PatchMapping::class.java.name -> HttpMethod.PATCH.name()
        else -> HttpMethod.GET.name()
      }
      val path = annotation.findAttributeValue("value") ?: annotation.findAttributeValue("path")
      val request = DefaultHttpServletRequest(method, path?.text ?: "")
    }
    // get class annotation
    // concatenate path
  }
}
