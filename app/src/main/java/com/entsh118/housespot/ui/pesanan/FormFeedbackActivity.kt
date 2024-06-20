package com.entsh118.housespot.ui.pesanan

import FormFeedbackViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.ActivityFormFeedbackBinding
import androidx.lifecycle.Observer
import com.entsh118.housespot.ui.reduceFileImage
import com.entsh118.housespot.ui.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormFeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormFeedbackBinding
    private val viewModel: FormFeedbackViewModel by viewModels()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private lateinit var detail: DataItem
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize DataStoreManager
        dataStoreManager = DataStoreManager(applicationContext)

        // Retrieve detail from intent
        detail = intent.getParcelableExtra<DataItem>(DETAIL_PESANAN)!!

        // Setup listeners for UI buttons
        setupListeners()
    }

    private fun setupListeners() {
        binding.galeriButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            val rating = binding.rating.text.toString()
            val message = binding.feedback.text.toString()
            val idClient = detail.idPemesan.toString()
            val idVendor = detail.idVendor.toString()

            if (rating.isEmpty() || message.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            } else {
                selectedImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, this).reduceFileImage()
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                         val multipartBody = MultipartBody.Part.createFormData(
                            "photo",
                            imageFile.name,
                            requestImageFile
                        )
                        viewModel.uploadFeedback(
                            idClient = idClient,
                            idVendor = idVendor,
                            image = multipartBody,
                            message = message,
                            rating = rating
                        )
                }
            }

            viewModel.feedbackResult.observe(this, Observer { result ->
                result.fold(
                    onSuccess = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { throwable ->
                        Toast.makeText(this, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                    }
                )
                backToDashboard()
            })

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
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

    companion object {
        const val DETAIL_PESANAN = "DETAIL_PESANAN"
    }

    private fun backToDashboard() {
        val intent = Intent(this, PesananClientActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            showImage()
        } else {
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        selectedImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

}
