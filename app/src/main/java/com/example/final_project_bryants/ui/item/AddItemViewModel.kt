package com.example.final_project_bryants.ui.item;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.data.TimeCapsuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Result type to encapsulate success and failure scenarios.
 */
sealed class OperationResult {
    object Success : OperationResult()
    data class Failure(val error: Throwable) : OperationResult()
}

class AddItemViewModel(private val repository: TimeCapsuleRepository) : ViewModel() {

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _operationResult

    fun onSaveClicked(type: String, content: String) {
        val timestamp = System.currentTimeMillis()
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(timestamp))
        val item = TimeCapsuleItem(0, type, content, timestamp, monthYear)
        insertItem(item)
    }

    private fun insertItem(item: TimeCapsuleItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insert(item)
                _operationResult.postValue(OperationResult.Success)
            } catch (e: Exception) {
                _operationResult.postValue(OperationResult.Failure(e))
            }
        }
    }
}
