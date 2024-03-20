package com.example.final_project_bryants.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeCapsuleItemDao {
    @Insert
    fun insert(item: TimeCapsuleItem)// This operation is synchronous and should be called from a background thread

    @Query("SELECT * FROM time_capsule_items WHERE monthYear = :monthYear")
    fun getItemsByMonthYear(monthYear: String): Flow<List<TimeCapsuleItem>> // LiveData for observing data changes

    @Query("SELECT * FROM time_capsule_items WHERE type = :type")
    fun getItemsByType(type: String) : Flow<List<TimeCapsuleItem>>

    @Query("SELECT * FROM time_capsule_items WHERE monthYear = :date")
    fun getItemsByDate(date: String) : Flow<List<TimeCapsuleItem>>
}
