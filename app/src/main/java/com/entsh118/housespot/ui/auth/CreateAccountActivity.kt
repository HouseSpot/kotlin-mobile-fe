package com.entsh118.housespot.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.textfield.ConfirmationPasswordTextField
import com.entsh118.housespot.custom.textfield.EmailTextField
import com.entsh118.housespot.custom.textfield.NameTextField
import com.entsh118.housespot.custom.textfield.NoPhoneTextField
import com.entsh118.housespot.custom.textfield.PasswordTextField
import com.entsh118.housespot.databinding.ActivityCreateAccountBinding
import com.entsh118.housespot.ui.auth.viewmodel.CreateAccountViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private val viewModel: CreateAccountViewModel by viewModels()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    private lateinit var etEmail: EmailTextField
    private lateinit var etPassword: PasswordTextField
    private lateinit var etName: NameTextField
    private lateinit var etPhone: NoPhoneTextField
    private lateinit var etConfirmPassword: ConfirmationPasswordTextField
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize gallery launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    val fileName = getFileName(uri)
                    binding.tvFileName.text = fileName
                    binding.fileRow.visibility = View.VISIBLE
                    binding.btnGallery.visibility = View.GONE
                }
            }
        }

        setupObservers()
        setupListeners()

        role = intent.getStringExtra("ROLE") ?: ""

        etEmail = binding.etEmail
        etPassword = binding.etPassword
        etConfirmPassword = binding.etConfirmPassword
        etName = binding.etName
        etPhone = binding.etPhone
    }

    private fun setupObservers() {

        viewModel.registrationResult.observe(this, Observer { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        })

        viewModel.isRegistrationSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                when (role.lowercase()) {
                    "vendor" -> navigateToVendorForm()
                    "client" -> navigateToLogin()
                }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun navigateToVendorForm() {
        val intent = Intent(this, VendorFormActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupListeners() {
        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        binding.btnRemoveFile.setOnClickListener {
            selectedImageUri = null
            binding.fileRow.visibility = View.GONE
            binding.btnGallery.visibility = View.VISIBLE
        }

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val phone = binding.etPhone.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (role.isEmpty()) {
                Toast.makeText(this, "Role is required. Please, go back to the previous section.", Toast.LENGTH_SHORT).show()
            }
            else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                selectedImageUri?.let { uri ->
                    val imageFile = File(getRealPathFromURI(uri))
                    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                    val profileImagePart = MultipartBody.Part.createFormData("profile", imageFile.name, requestBody)
                    viewModel.register(
                        name = name,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        phone = phone,
                        role = role,
                        profileImage = profileImagePart
                    )
                } ?: run {
                    Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show()
                }
            }
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
                        // Handle the case where the column is not found
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

}
