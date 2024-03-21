package com.example.final_project_bryants.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert
    fun insert(notificationItem: NotificationItem)

    @Delete
    fun delete(notificationItem: NotificationItem)

    @Query("SELECT * FROM notifications")
    suspend fun getNotifications() : List<NotificationItem>
}