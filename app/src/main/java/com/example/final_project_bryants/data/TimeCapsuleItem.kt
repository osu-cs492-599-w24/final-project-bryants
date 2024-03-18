package com.example.final_project_bryants.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_capsule_items")
data class TimeCapsuleItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val content: String,
    val timestamp: Long,
    val monthYear: String
)
