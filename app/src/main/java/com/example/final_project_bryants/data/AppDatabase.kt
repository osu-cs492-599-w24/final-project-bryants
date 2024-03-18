package com.example.final_project_bryants.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TimeCapsuleItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeCapsuleItemDao(): TimeCapsuleItemDao
}
