package com.example.final_project_bryants.ui.calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarDateViewModel : ViewModel() {

    // LiveData to hold the selected date
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    // Function to set the selected date
    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }
}