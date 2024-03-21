package com.example.final_project_bryants.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_project_bryants.data.AppDatabase
import com.example.final_project_bryants.data.NotificationItem
import com.example.final_project_bryants.data.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NotificationRepository(
        AppDatabase.getInstance(application).notificationDao()
    )

    fun addNotification(notificationItem: NotificationItem) {
        viewModelScope.launch {
            repository.insertNotification(notificationItem)
        }
    }

    fun removeNotification(notificationItem: NotificationItem) {
        viewModelScope.launch {
            repository.deleteNotification(notificationItem)
        }
    }
}