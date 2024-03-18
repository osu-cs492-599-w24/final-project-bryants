package com.example.final_project_bryants.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.final_project_bryants.databinding.FragmentDateDisplayBinding

class DateDisplayFragment : Fragment() {

    private var _binding: FragmentDateDisplayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateDisplayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retrieve the selected date arguments passed from CalendarFragment
        val selectedDate = arguments?.getString("selectedDate")
        Log.d("Date Display", "$selectedDate")
        binding.dateTextView.text = selectedDate

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SELECTED_DATE = "selected_date"

        // Factory method to create a new instance of DateDisplayFragment
        fun newInstance(selectedDate: String): DateDisplayFragment {
            val fragment = DateDisplayFragment()
            val args = Bundle()
            args.putString(ARG_SELECTED_DATE, selectedDate)
            fragment.arguments = args
            return fragment
        }
    }
}