package com.entsh118.housespot.ui.pesanan
import android.content.Intent
import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.R
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.model.UserPreferences
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.data.api.response.FileUploadResponse
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import com.entsh118.housespot.databinding.ActivityFormFeedbackBinding
import com.entsh118.housespot.ui.ViewModelFactory
import com.entsh118.housespot.ui.layananjasa.DetailJasaActivity
import com.entsh118.housespot.utils.getImageUri
import com.entsh118.housespot.utils.reduceFileImage
import com.entsh118.housespot.utils.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import com.google.gson.Gson

class FormFeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormFeedbackBinding
    private var currentImageUri: Uri? = null
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var userPreferences: UserPreferences

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        dataStoreManager = DataStoreManager(applicationContext)
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.galeriButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            currentImageUri?.let {
                uploadImage()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }

    }



    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            userPreferences = dataStoreManager.userPreferencesFlow.first()
        }
    }


    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            // Prepare image file and request body
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            // Prepare other data
            val deskripsi = binding.feedback.text.toString()
            val rating = binding.rating.text.toString()
            val detailPesanan = intent.getParcelableExtra<DataItem>(FormFeedbackActivity.DETAIL_PESANAN)

            val idClient = detailPesanan?.idPemesan ?: return@let showToast("Client ID is missing")
            val idVendor = detailPesanan?.idVendor ?: return@let showToast("Vendor ID is missing")

            val requestBodyDescription = deskripsi.toRequestBody("text/plain".toMediaType())
            val requestBodyRating = rating.toRequestBody("text/plain".toMediaType())

            // Create multipart body for image
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            // Perform API call using Retrofit
            showLoading(true)
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getClientService()
                    val response = apiService.uploadFeedback(
                        idClient,
                        idVendor,
                        multipartBody,
                        requestBodyDescription,
                        requestBodyRating
                    )
                    showLoading(false)
                    backToMain()
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                    showToast(errorResponse.message.toString())
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }



    private fun backToMain() {
        val intent = Intent(this, PesananClientActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        //  binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DETAIL_PESANAN = "DETAIL_PESANAN"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}