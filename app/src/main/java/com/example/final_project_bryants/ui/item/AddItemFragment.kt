package com.example.final_project_bryants.ui.item


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.final_project_bryants.R
import com.example.final_project_bryants.data.AddItemViewModelFactory
import com.example.final_project_bryants.data.TimeCapsuleItem
import com.example.final_project_bryants.databinding.FragmentAddItemBinding
import com.example.final_project_bryants.databinding.FragmentHomeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddItemViewModel by viewModels()

    private lateinit var selectPhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private var image_uri : String? = null

    // Define a variable to hold the temporary file path for the captured image
    private var tempPhotoFilePath: String? = null

    companion object {
        private const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
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
                binding.btnOpenCamera.visibility = View.GONE
            }
            "Photo" -> {
                binding.editTextContent.visibility = View.GONE
                binding.btnSelectPhoto.visibility = View.VISIBLE
                binding.btnOpenCamera.visibility = View.GONE
            }
            "Add With Camera" -> {
                binding.editTextContent.visibility = View.GONE
                binding.btnSelectPhoto.visibility = View.GONE
                binding.btnOpenCamera.visibility = View.VISIBLE
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
                "Add With Camera" -> {
                    image_uri.toString()
                }
                else -> ""
            }
            Log.d("Content Test", content)
            if (content.isNotEmpty()) {
                val timestamp = System.currentTimeMillis()
                val monthYear = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date(timestamp))
                if (type != "Add With Camera") {
                    val item = TimeCapsuleItem(0, type, content, timestamp, monthYear)
                    viewModel.insertItem(item)
                }
                else {
                    val item = TimeCapsuleItem(0, "Photo", content, timestamp, monthYear)
                    viewModel.insertItem(item)
                }
                Toast.makeText(requireContext(), "Image Added To Today's Capsule!", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSelectPhoto.setOnClickListener {
            checkPermissionAndPickImage()
        }

        binding.btnOpenCamera.setOnClickListener {
            openCamera()
        }
    }

    private fun checkPermissionAndPickImage() {
        val test = context?.let { isPhotoPickerAvailable(it) }
        Log.d("Test Act", "$test")
        // Launch the photo picker and let the user choose only images.
        selectPhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun openCamera() {
        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Create a temporary file to store the captured image
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.final_project_bryants.fileprovider",
                    it
                )
                tempPhotoFilePath = it.absolutePath
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
        } else {
            // Request camera permission
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file path for use with ACTION_VIEW intents
            tempPhotoFilePath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Image captured and saved, update the image_uri with the temporary file path
            tempPhotoFilePath?.let { path ->
                Log.d("PhotoPicker", "Photo Path: $path")
                binding.imageView.setImageURI(Uri.fromFile(File(path)))
                binding.imageView.visibility = View.VISIBLE
                image_uri = path
                Log.d("Test Photo", "$image_uri")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, open camera
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
