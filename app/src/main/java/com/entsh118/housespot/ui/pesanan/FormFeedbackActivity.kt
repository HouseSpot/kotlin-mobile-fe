package com.entsh118.housespot.ui.pesanan

import FormFeedbackViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.ActivityFormFeedbackBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormFeedbackActivity : AppCompatActivity() {
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityFormFeedbackBinding
    private val viewModel: FormFeedbackViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var detail: DataItem
    private lateinit var dataStoreManager: DataStoreManager

    private val REQUEST_STORAGE_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()

        // Initialize DataStoreManager
        dataStoreManager = DataStoreManager(applicationContext)

        // Retrieve detail from intent
        detail = intent.getParcelableExtra<DataItem>(DETAIL_PESANAN)!!

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    val fileName = getFileName(uri)
                    binding.previewImageView.setImageURI(uri)
                }
            }
        }

        // Setup listeners for UI buttons
        setupListeners()
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionResults = permissions.map { permission ->
            ContextCompat.checkSelfPermission(this, permission)
        }
        if (permissionResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted
            } else {
                Toast.makeText(this, "Storage permissions are required to upload profile image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        binding.galeriButton.setOnClickListener { openGallery() }

        binding.uploadButton.setOnClickListener {
            val rating = binding.rating.text.toString()
            val message = binding.feedback.text.toString()
            val idClient = detail.idPemesan.toString()
            val idVendor = detail.idVendor.toString()

            Log.d("FormFeedbackActivity", "idClient: $idClient, idVendor: $idVendor, message: $message, rating: $rating")

            if (rating.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ratingDouble = rating.toDoubleOrNull()
            if (ratingDouble == null) {
                Toast.makeText(this, "Please enter a valid rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ratingRequestBody = rating.toRequestBody("text/plain".toMediaTypeOrNull())
            val messageRequestBody = message.toRequestBody("text/plain".toMediaTypeOrNull())
            val idClientRequestBody = idClient.toRequestBody("text/plain".toMediaTypeOrNull())
            val idVendorRequestBody = idVendor.toRequestBody("text/plain".toMediaTypeOrNull())

            if (selectedImageUri != null) {
                val imagePath = getRealPathFromURI(selectedImageUri!!)
                if (imagePath.isNotEmpty()) {
                    val imageFile = File(imagePath)
                    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                    val profileImagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
                    Log.d("FormFeedbackActivity", "Image path: $imagePath")

                    viewModel.uploadFeedback(
                        idClient = idClientRequestBody,
                        idVendor = idVendorRequestBody,
                        image = profileImagePart,
                        message = messageRequestBody,
                        rating = ratingRequestBody
                    )
                } else {
                    Toast.makeText(this, "Unable to get the selected image path.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("FormFeedbackActivity", "idClient: $idClient, idVendor: $idVendor, message: $message, rating: $rating")

                viewModel.uploadFeedbackWithoutImage(
                    idClient = idClientRequestBody,
                    idVendor = idVendorRequestBody,
                    message = messageRequestBody,
                    rating = ratingRequestBody
                )
            }
        }

        viewModel.feedbackResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    backToDashboard()
                },
                onFailure = { throwable ->
                    Toast.makeText(this, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    path = it.getString(columnIndex)
                } else {
                    Toast.makeText(this, "Failed to get image path", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return path
    }

    private fun backToDashboard() {
        val intent = Intent(this, PesananClientActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    companion object {
        const val DETAIL_PESANAN = "DETAIL_PESANAN"
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = it.getString(columnIndex)
                    } else {
                        Toast.makeText(this, "Failed to get file name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result ?: "image.jpg"
    }
}
