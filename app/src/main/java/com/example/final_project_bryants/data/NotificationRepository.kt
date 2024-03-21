package com.example.final_project_bryants.data

class NotificationRepository (private val itemDao: NotificationDao) {

    suspend fun insertNotification(item: NotificationItem) = itemDao.insert(item)

    suspend fun deleteNotification(item: NotificationItem) = itemDao.delete(item)

    suspend fun getAllNotifications() = itemDao.getNotifications()
}