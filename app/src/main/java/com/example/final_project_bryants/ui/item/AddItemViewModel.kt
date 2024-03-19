package com.example.final_project_bryants.ui.item;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_bryants.data.AppDatabase
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.data.TimeCapsuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Result type to encapsulate success and failure scenarios.
 */

class AddItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TimeCapsuleRepository(AppDatabase.getInstance(application).timeCapsuleItemDao())

    fun insertItem(item: TimeCapsuleItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(item)
        }
    }
}
