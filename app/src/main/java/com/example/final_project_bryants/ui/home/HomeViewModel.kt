package com.example.final_project_bryants.ui.item;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_project_bryants.data.AppDatabase
import com.example.final_project_bryants.data.TimeCapsuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

/**
 * Result type to encapsulate success and failure scenarios.
 */

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TimeCapsuleRepository(AppDatabase.getInstance(application).timeCapsuleItemDao())

    fun getItems(type: String) =
        repository.getItemsByType(type).asLiveData()
}
