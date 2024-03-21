package com.example.final_project_bryants.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notifications")
data class NotificationItem(
    val dateToShare: String,
    @PrimaryKey
    val timeToShare: String
)