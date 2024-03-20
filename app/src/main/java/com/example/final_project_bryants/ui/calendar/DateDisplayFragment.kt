package com.example.final_project_bryants.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_bryants.R
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.databinding.FragmentDateDisplayBinding
import com.example.final_project_bryants.ui.home.SliderAdapter
import com.example.final_project_bryants.ui.item.DateDisplayViewModel
import com.example.final_project_bryants.ui.item.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateDisplayFragment : Fragment() {

    private var _binding: FragmentDateDisplayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DateDisplayViewModel by viewModels()

    private lateinit var dateDisplayRV: RecyclerView

    lateinit var dateDisplayAdapter : DateDisplayAdapter

    private var timeCapsules: ArrayList<TimeCapsuleItem> = ArrayList()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateDisplayAdapter = DateDisplayAdapter(timeCapsules)
        dateDisplayRV = view.findViewById(R.id.rv_selected_date_capsules)
        dateDisplayRV.adapter = dateDisplayAdapter
        dateDisplayRV.layoutManager = LinearLayoutManager(requireContext())
        dateDisplayRV.setHasFixedSize(true)

        lifecycleScope.launch(Dispatchers.Main) { // Change Dispatchers.IO to Dispatchers.Main
            val selectedDate = arguments?.getString("selectedDate")

            if (selectedDate != null) {
                viewModel.getDateItems(selectedDate).observe(viewLifecycleOwner) { items ->
                    // The items here are emitted as they change.
                    // You can collect them into a list.
                    val itemList = items.map { it } // Assuming 'content' is the property you want to collect
                    timeCapsules.clear() // Clear existing items if needed
                    timeCapsules.addAll(itemList) // Add the collected items to timeCapsules list
                    //Log.d("Date Test", "$timeCapsules")
                    dateDisplayAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}