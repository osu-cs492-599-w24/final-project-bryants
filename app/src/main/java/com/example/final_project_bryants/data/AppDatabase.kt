package com.example.final_project_bryants.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimeCapsuleItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeCapsuleItemDao(): TimeCapsuleItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "time-capsules-db"
            ).build()

        fun getInstance(context: Application) : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}
