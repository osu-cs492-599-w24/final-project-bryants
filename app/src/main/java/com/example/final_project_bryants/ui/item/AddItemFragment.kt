package com.example.final_project_bryants.ui.additem


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.final_project_bryants.R
import com.example.final_project_bryants.databinding.FragmentAddItemBinding
import com.example.final_project_bryants.data.*

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddItemViewModel

    private lateinit var selectPhotoLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100
    }

    // Rest of your code as before...

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
            val type = binding.spinnerType.selectedItem.toString()
            val content = when (type) {
                "Text", "Link" -> binding.editTextContent.text.toString()
                else -> ""
            }
            if (content.isNotEmpty()) viewModel.onSaveClicked(type, content)
        }

        binding.btnSelectPhoto.setOnClickListener {
            checkPermissionAndPickImage()
        }
    }

    private fun checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            Toast.makeText(requireContext(), "Permission to access storage was denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        selectPhotoLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
