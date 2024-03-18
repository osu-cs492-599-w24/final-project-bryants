package com.example.final_project_bryants.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeCapsuleItemDao {
    @Insert
    fun insert(item: TimeCapsuleItem): Long // This operation is synchronous and should be called from a background thread

    @Query("SELECT * FROM time_capsule_items WHERE monthYear = :monthYear")
    fun getItemsByMonthYear(monthYear: String): LiveData<List<TimeCapsuleItem>> // LiveData for observing data changes
}
