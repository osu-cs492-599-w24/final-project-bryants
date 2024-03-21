package com.example.final_project_bryants.data

class TimeCapsuleRepository(private val itemDao: TimeCapsuleItemDao) {

    suspend fun insert(item: TimeCapsuleItem) {
        itemDao.insert(item)
    }

    // Add more repository methods as needed, e.g., for fetching items, deleting, etc.
    fun getItemsByType(type: String) =
        itemDao.getItemsByType(type)

    fun getItemsByDate(date: String) =
        itemDao.getItemsByDate(date)
}