package com.example.final_project_bryants.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.final_project_bryants.R
import com.smarteist.autoimageslider.SliderView
import com.example.final_project_bryants.data.AppDatabase
import com.example.final_project_bryants.databinding.FragmentHomeBinding
import com.example.final_project_bryants.ui.item.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var imageUrl: ArrayList<String> = ArrayList()
    lateinit var sliderView: SliderView
    lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        sliderView = view.findViewById(R.id.slider)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*imageUrl = ArrayList()
        imageUrl.add("https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75")
        imageUrl.add("https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75")*/

        sliderAdapter = SliderAdapter(imageUrl)

        lifecycleScope.launch(Dispatchers.Main) { // Change Dispatchers.IO to Dispatchers.Main
            viewModel.getItems("Photo").observe(viewLifecycleOwner) { items ->
                // The items here are emitted as they change.
                // You can collect them into a list.
                val itemList = items.map { it.content } // Assuming 'content' is the property you want to collect
                imageUrl.clear() // Clear existing items if needed
                imageUrl.addAll(itemList) // Add the collected items to imageUrl list

                // Notify the adapter that the data has changed
                sliderAdapter.notifyDataSetChanged()
            }
        }
        // on below line we are setting auto cycle direction
        // for our slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        // on below line we are setting adapter for our slider.
        sliderView.setSliderAdapter(sliderAdapter)

        // on below line we are setting scroll time
        // in seconds for our slider view.
        sliderView.scrollTimeInSec = 5

        // on below line we are setting auto cycle
        // to true to auto slide our items.
        sliderView.isAutoCycle = true

        // on below line we are calling start
        // auto cycle to start our cycle.
        sliderView.startAutoCycle()
    }
}
