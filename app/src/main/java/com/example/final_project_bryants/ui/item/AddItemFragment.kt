package com.example.final_project_bryants.ui.item


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.final_project_bryants.R
import com.example.final_project_bryants.data.AddItemViewModelFactory
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.databinding.FragmentAddItemBinding
import com.example.final_project_bryants.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddItemViewModel by viewModels()

    private lateinit var selectPhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private var image_uri : String? = null

    companion object {
        private const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100
    }

    // Rest of your code as before...
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        selectPhotoLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.imageView.setImageURI(uri)
                binding.imageView.visibility = View.VISIBLE
                image_uri = uri.toString()
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTypeSpinner()
        setupButtons()
    }
    private fun setupTypeSpinner() {
        ArrayAdapter.createFromResource(requireContext(), R.array.item_types, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerType.adapter = adapter
        }

        // Set up the spinner onItemSelectedListener
        binding.spinnerType.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                // This method will be invoked when an item in the spinner is selected
                val selectedItem = parent.getItemAtPosition(position).toString()
                updateUIBasedOnType(selectedItem)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // This method will be invoked when the selection disappears from this view
            }
        }
    }

    private fun updateUIBasedOnType(type: String) {
        when (type) {
            "Text", "Link" -> {
                binding.editTextContent.visibility = View.VISIBLE
                binding.btnSelectPhoto.visibility = View.GONE
            }
            "Photo" -> {
                binding.editTextContent.visibility = View.GONE
                binding.btnSelectPhoto.visibility = View.VISIBLE
            }
        }
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener {
            Log.d("View Test", "$it")
            val type = binding.spinnerType.selectedItem.toString()
            val content = when (type) {
                "Text", "Link" -> {
                    binding.editTextContent.text.toString()
                }
                //If it is a photo get the uri and conver uri to a string
                "Photo" -> {
                    if (image_uri != null) {
                        image_uri.toString()
                    } else {
                        // If image_uri is null, show a Toast and return an empty string
                        Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }
                else -> ""
            }
            if (content.isNotEmpty()) {
                val timestamp = System.currentTimeMillis()
                val monthYear = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date(timestamp))
                val item = TimeCapsuleItem(0, type, content, timestamp, monthYear)
                viewModel.insertItem(item)
            }
        }

        binding.btnSelectPhoto.setOnClickListener {
            checkPermissionAndPickImage()
        }
    }

    private fun checkPermissionAndPickImage() {
        val test = context?.let { isPhotoPickerAvailable(it) }
        Log.d("Test Act", "$test")
        // Launch the photo picker and let the user choose only images.
        selectPhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
