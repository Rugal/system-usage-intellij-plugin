package ga.rugal.intellij.sample.service

import kotlin.concurrent.thread
import ga.rugal.intellij.common.service.PluginPropertyService
import ga.rugal.intellij.sample.configuration.Icon
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationService {

  /**
   * Create notification object without sending notification.
   */
  fun createNotificationObject(text: String, type: NotificationType = NotificationType.INFORMATION): Notification =
    NotificationGroupManager
      .getInstance()
      .getNotificationGroup(PluginPropertyService.get("name"))
      .createNotification(text, type)
      .setIcon(Icon.LIGHTNING_ICON)
      .setTitle(PluginPropertyService.get("name"))

  /**
   * Create notification and send it right away.
   */
  fun notify(
    text: String,
    project: Project,
    type: NotificationType = NotificationType.INFORMATION,
    millis: Long = 5000,
  ) = createNotificationObject(text, type, millis).run { this.notify(project) }

  /**
   * Create notification object but do not notify.
   */
  fun createNotificationObject(
    text: String,
    type: NotificationType = NotificationType.INFORMATION,
    millis: Long = 5000,
  ): Notification = this.createNotificationObject(text, type).also {
    if (millis > 0) {
      thread {
        Thread.sleep(millis)
        it.expire()
      }
    }
  }
}
