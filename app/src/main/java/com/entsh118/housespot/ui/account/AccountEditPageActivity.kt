package com.entsh118.housespot.ui.account

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.textfield.ConfirmationPasswordTextField
import com.entsh118.housespot.custom.textfield.EmailTextField
import com.entsh118.housespot.custom.textfield.NameTextField
import com.entsh118.housespot.custom.textfield.NoPhoneTextField
import com.entsh118.housespot.custom.textfield.PasswordTextField
import com.entsh118.housespot.databinding.ActivityAccountEditPageBinding
import com.entsh118.housespot.databinding.ActivityCreateAccountBinding
import com.entsh118.housespot.ui.account.viewmodel.AccountEditPageViewModel
import com.entsh118.housespot.ui.account.viewmodel.AccountProfilePageViewModel
import com.entsh118.housespot.ui.auth.LoginActivity
import com.entsh118.housespot.ui.auth.adapter.SelectedImagesAdapter
import com.entsh118.housespot.ui.auth.viewmodel.CreateAccountViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AccountEditPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountEditPageBinding
    private val viewModel: AccountEditPageViewModel by viewModels()
    private lateinit var userId: String
    private lateinit var peran: String

    private var isVendor: Boolean = false

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    private val REQUEST_STORAGE_PERMISSION = 1001


    private var selectedImageUris: MutableList<Uri> = mutableListOf()
    private lateinit var selectedImagesAdapter: SelectedImagesAdapter

    private lateinit var galleryLauncherMultiple: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: ""
        peran = intent.getStringExtra("peran") ?: ""

        if (peran.lowercase() == "vendor") {
            isVendor = true
            populateVendorFieldsFromIntent()
        } else {
            isVendor = false
            populateClientFieldsFromIntent()
        }

        checkAndRequestPermissions()

        binding.btnChangePhoto.setOnClickListener {
            openGallery()
        }

        binding.btnGallery.setOnClickListener {
            openGalleryMultiple()
        }

        setupRecyclerView()

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    val fileName = getFileName(uri)
                    binding.civProfilePicture.setImageURI(uri)
                }
            }
        }

        galleryLauncherMultiple = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.clipData?.let { clipData ->
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        selectedImageUris.add(uri)
                    }
                } ?: result.data?.data?.let { uri ->
                    selectedImageUris.add(uri)
                }
                selectedImagesAdapter.notifyDataSetChanged()
            }
        }

        setupToolbar()
        setupObservers()

        binding.btnSaveProfile.setOnClickListener {
            if (validateFields()) {
                saveProfile()
            } else {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {

        if(!isVendor) {
            viewModel.updateSuccess.observe(this) { isSuccess ->
                if (isSuccess) {
                    Log.d("AccountEditPageActivity", "Profile updated successfully")

                }
            }
        }

        if(isVendor) {
            viewModel.updateVendorResult.observe(this) { result ->
                if (result.status == "success") {
                    Toast.makeText(this, "Vendor profile updated successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Failed to update vendor profile", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openGalleryMultiple() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryLauncherMultiple.launch(intent)
    }

    private fun setupRecyclerView() {
        selectedImagesAdapter = SelectedImagesAdapter(selectedImageUris) { position ->
            selectedImageUris.removeAt(position)
            selectedImagesAdapter.notifyDataSetChanged()
        }
        binding.rvSelectedImages.layoutManager = LinearLayoutManager(this)
        binding.rvSelectedImages.adapter = selectedImagesAdapter
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
                    Log.d("AccountEditPageActivity", "Failed to get image path")
                }
            }
        }
        return path
    }

    private fun getRealPortfolioPathFromURI(uri: Uri): String? {
        var result: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                result = it.getString(columnIndex)
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun populateClientFieldsFromIntent() {
        binding.extraInfoContainer.visibility = View.GONE

        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val profile = intent.getStringExtra("profile") ?: ""

        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etContact.setText(phone)

        Glide.with(this)
            .load(profile)
            .into(binding.civProfilePicture)
    }

    private fun populateVendorFieldsFromIntent() {
        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val profile = intent.getStringExtra("profile") ?: ""
        val jenisProperti = intent.getStringExtra("jenisProperti") ?: ""
        val feeMinimum = intent.getStringExtra("feeMinimum") ?: ""
        val lokasiKantor = intent.getStringExtra("lokasiKantor") ?: ""
        val deskripsiLayanan = intent.getStringExtra("deskripsiLayanan") ?: ""
        val tipeLayanan = intent.getStringArrayListExtra("tipeLayanan") ?: arrayListOf()
        val jasaKontraktor = intent.getStringArrayListExtra("jasaKontraktor") ?: arrayListOf()

        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etContact.setText(phone)

        Glide.with(this)
            .load(profile)
            .into(binding.civProfilePicture)

        binding.etPropertyType.setText(jenisProperti)
        binding.etFeeMinimum.setText(feeMinimum)
        binding.etOfficeLocation.setText(lokasiKantor)
        binding.etServiceDescription.setText(deskripsiLayanan)

        binding.checkboxRenovasi.isChecked = tipeLayanan.contains("Renovasi")
        binding.checkboxBangunDariAwal.isChecked = tipeLayanan.contains("Bangun dari awal")
        binding.checkboxKontraktor.isChecked = jasaKontraktor.contains("Kontraktor")
        binding.checkboxTukang.isChecked = jasaKontraktor.contains("Tukang")

        binding.extraInfoContainer.visibility = View.VISIBLE
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.etName.error = "Name is required"
            isValid = false
        }

        if (binding.etContact.text.toString().trim().isEmpty()) {
            binding.etContact.error = "Contact is required"
            isValid = false
        }

        if (isVendor){
            val isTipeLayananChecked = binding.checkboxRenovasi.isChecked || binding.checkboxBangunDariAwal.isChecked
            val isJasaKontraktorChecked = binding.checkboxKontraktor.isChecked || binding.checkboxTukang.isChecked

            isValid = isTipeLayananChecked &&
                    isJasaKontraktorChecked &&
                    binding.etPropertyType.text.isNotEmpty() &&
                    binding.etOfficeLocation.text.isNotEmpty() &&
                    binding.etServiceDescription.text.isNotEmpty() &&
                    binding.etFeeMinimum.text.isNotEmpty()
        }

        return isValid
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString()
        val phone = binding.etContact.text.toString()
        selectedImageUri?.let { uri ->
            val imagePath = getRealPathFromURI(uri)
            if (imagePath.isNotEmpty()) {
                val imageFile = File(imagePath)
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val profileImagePart = MultipartBody.Part.createFormData("profile", imageFile.name, requestBody)
                viewModel.updateUser(
                    id = userId,
                    name = name,
                    phone = phone,
                    profileImage = profileImagePart
                )
            } else {
                Toast.makeText(this, "Unable to get the selected image path.", Toast.LENGTH_SHORT).show()
                return
            }
        } ?: run {
            viewModel.updateUser(
                id = userId,
                name = name,
                phone = phone
            )
        }

        if(isVendor){
            val id = userId
            val tipeLayanan = mutableListOf<String>()
            if (binding.checkboxRenovasi.isChecked) {
                tipeLayanan.add(binding.checkboxRenovasi.text.toString())
            }
            if (binding.checkboxBangunDariAwal.isChecked) {
                tipeLayanan.add(binding.checkboxBangunDariAwal.text.toString())
            }
            val jenisProperti = binding.etPropertyType.text.toString()
            val jasaKontraktor = mutableListOf<String>()
            if (binding.checkboxKontraktor.isChecked) {
                jasaKontraktor.add(binding.checkboxKontraktor.text.toString())
            }
            if (binding.checkboxTukang.isChecked) {
                jasaKontraktor.add(binding.checkboxTukang.text.toString())
            }
            val lokasiKantor = binding.etOfficeLocation.text.toString()
            val deskripsiLayanan = binding.etServiceDescription.text.toString()
            val feeMinimum = binding.etFeeMinimum.text.toString()
            val iklanPersetujuan = true

            val portofolioPaths = selectedImageUris.mapNotNull { getRealPortfolioPathFromURI(it) }

            viewModel.updateVendor(
                id,
                tipeLayanan,
                jenisProperti,
                jasaKontraktor,
                lokasiKantor,
                deskripsiLayanan,
                iklanPersetujuan,
                feeMinimum,
                portofolioPaths
            )
        }

        if(!isVendor){
            Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()
        }
    }
}

