package com.example.final_project_bryants.ui.calendar

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.final_project_bryants.data.NotificationItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val notificationViewModel: NotificationViewModel by viewModels()

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        Log.w("ShareDate", "currentDate is ${year}")
        var monthText: String = "March"
        if (month.toInt() == 3) {
            monthText = "March"

        }
        val dateToShare = "${monthText} ${dayOfMonth} ${year}"
        val timeToShare = "${month}/${dayOfMonth}/${year}"
        val notification: NotificationItem = NotificationItem(dateToShare, timeToShare)
        val id = notificationViewModel.addNotification(notification)
    }

}