package com.entsh118.housespot.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.databinding.ActivityVendorFormBinding
import com.entsh118.housespot.ui.auth.adapter.SelectedImagesAdapter
import com.entsh118.housespot.ui.auth.viewmodel.VendorFormViewModel

class VendorFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorFormBinding
    private val viewModel: VendorFormViewModel by viewModels()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUris: MutableList<Uri> = mutableListOf()
    private lateinit var userId: String
    private lateinit var selectedImagesAdapter: SelectedImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTermsTextView()

        userId = intent.getStringExtra("ID") ?: ""
        Log.d("VendorFormActivity", "User ID: $userId")

        setupRecyclerView()

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.btnNext.setOnClickListener {
            validateForm()
            if (viewModel.isFormValid.value == true) {
                if (viewModel.termsAccepted.value == true) {
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

                    val portofolioPaths = selectedImageUris.mapNotNull { getRealPathFromURI(it) }

                    viewModel.addVendor(
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
                } else {
                    Toast.makeText(this, "Please accept our terms and conditions.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields and check required options", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        binding.checkboxTerms.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTermsAccepted(isChecked)
        }

        viewModel.addVendorResult.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            if (result.status == "success") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        selectedImagesAdapter = SelectedImagesAdapter(selectedImageUris) { position ->
            selectedImageUris.removeAt(position)
            selectedImagesAdapter.notifyDataSetChanged()
        }
        binding.rvSelectedImages.layoutManager = LinearLayoutManager(this)
        binding.rvSelectedImages.adapter = selectedImagesAdapter
    }

    private fun validateForm() {
        val isTipeLayananChecked = binding.checkboxRenovasi.isChecked || binding.checkboxBangunDariAwal.isChecked
        val isJasaKontraktorChecked = binding.checkboxKontraktor.isChecked || binding.checkboxTukang.isChecked

        viewModel.validateForm(
            isTipeLayananChecked &&
                    isJasaKontraktorChecked &&
                    binding.etPropertyType.text.isNotEmpty() &&
                    binding.etOfficeLocation.text.isNotEmpty() &&
                    binding.etServiceDescription.text.isNotEmpty() &&
                    binding.etFeeMinimum.text.isNotEmpty()
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryLauncher.launch(intent)
    }

    private fun setupTermsTextView() {
        val termsText = "Terms & Referencesnya"
        val spannableString = SpannableString(termsText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTermsPopup()
            }
        }
        spannableString.setSpan(clickableSpan, 0, termsText.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTerms.text = spannableString
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showTermsPopup() {
        AlertDialog.Builder(this)
            .setTitle("Terms & Conditions")
            .setMessage(
                """
            Welcome to HouseSpot! 
            By creating an account, you agree to the following terms and conditions:

            1. Account Responsibility 
            You are responsible for maintaining the confidentiality of your account and password and for restricting access to your computer. You agree to accept responsibility for all activities that occur under your account or password.

            2. User Conduct
            You agree not to use the app for any unlawful purpose or in any way that interrupts, damages, impairs, or renders the app less efficient.

            3. Content Ownership
            All content you upload, share, or provide on the app, including images and text, remains your property. However, you grant us a non-exclusive, royalty-free license to use, distribute, modify, and publicly display such content for the purposes of operating and improving the app.

            4. Privacy Policy
            We are committed to protecting your privacy. Please review our Privacy Policy to understand how we collect, use, and safeguard the information you provide.

            5. Service Changes
            We reserve the right to modify or discontinue, temporarily or permanently, the service (or any part thereof) with or without notice. You agree that we shall not be liable to you or to any third party for any modification, suspension, or discontinuance of the service.

            6. Termination
            We may terminate or suspend your account and bar access to the service immediately, without prior notice or liability, under our sole discretion, for any reason whatsoever and without limitation, including but not limited to a breach of the Terms.

            7. Limitation of Liability
            In no event shall HouseSpot, nor its directors, employees, partners, agents, suppliers, or affiliates, be liable for any indirect, incidental, special, consequential, or punitive damages, including without limitation, loss of profits, data, use, goodwill, or other intangible losses, resulting from (i) your use or inability to use the service; (ii) any unauthorized access to or use of our servers and/or any personal information stored therein; (iii) any interruption or cessation of transmission to or from the service.

            By clicking "OK", you acknowledge that you have read, understood, and agree to be bound by these terms and conditions.
            """.trimIndent()
            )
            .setPositiveButton("OK", null)
            .show()
    }

    private fun getRealPathFromURI(uri: Uri): String? {
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
}
