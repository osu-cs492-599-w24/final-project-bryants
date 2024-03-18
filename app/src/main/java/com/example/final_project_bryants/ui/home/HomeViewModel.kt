package com.example.final_project_bryants.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // LiveData to handle navigation to the AddItemFragment
    private val _navigateToAddItem = MutableLiveData<Boolean>()
    val navigateToAddItem: LiveData<Boolean> = _navigateToAddItem

    /**
     * Call this method when the Add Item button is clicked.
     * It updates the LiveData to trigger navigation.
     */
    fun onAddItemClicked() {
        _navigateToAddItem.value = true
    }

    /**
     * Call this method once navigation is done or when the Fragment needs to reset the navigation state.
     * It resets the LiveData used for triggering navigation, preventing unintended navigation events.
     */
    fun onAddItemNavigated() {
        _navigateToAddItem.value = false
    }
}
