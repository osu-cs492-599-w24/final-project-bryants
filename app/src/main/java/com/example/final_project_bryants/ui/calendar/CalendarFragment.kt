package com.example.final_project_bryants.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.final_project_bryants.MainActivity
import com.example.final_project_bryants.R
import com.example.final_project_bryants.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Call a function in MainActivity to handle fragment change
            (activity as? MainActivity)?.changeFragment(year, month, dayOfMonth)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager: FragmentManager? = getFragmentManager()
        val manager: FragmentManager = fragmentManager as FragmentManager
        val shareBtn: Button? = view?.findViewById(R.id.share_button)
        shareBtn?.setOnClickListener {
            Log.w("ShareDate", "Share button was clicked")
            DatePickerFragment().show(parentFragmentManager, "datePicker")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}