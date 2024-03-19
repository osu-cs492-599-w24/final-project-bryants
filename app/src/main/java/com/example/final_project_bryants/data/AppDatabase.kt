package com.example.final_project_bryants.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.final_project_bryants.data.Converters
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.data.TimeCapsuleItemDao

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

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}
