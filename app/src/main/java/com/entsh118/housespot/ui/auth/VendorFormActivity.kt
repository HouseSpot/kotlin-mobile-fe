package com.entsh118.housespot.ui.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.entsh118.housespot.databinding.ActivityVendorFormBinding
import com.entsh118.housespot.ui.auth.viewmodel.VendorFormViewModel

class VendorFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorFormBinding
    private val viewModel: VendorFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTermsTextView()

        viewModel.isFormValid.observe(this, { isValid ->
            binding.btnNext.isEnabled = isValid
        })

        binding.btnNext.setOnClickListener {
            if (viewModel.isFormValid.value == true) {
                // Proceed to next step
                // Navigate to next page or other action
            } else {
                Toast.makeText(this, "Please fill all fields and check required options", Toast.LENGTH_SHORT).show()
            }
        }

        binding.checkboxRenovasi.setOnCheckedChangeListener { _, _ -> validateForm() }
        binding.checkboxBangunDariAwal.setOnCheckedChangeListener { _, _ -> validateForm() }
        binding.checkboxKontraktor.setOnCheckedChangeListener { _, _ -> validateForm() }
        binding.checkboxTukang.setOnCheckedChangeListener { _, _ -> validateForm() }
        binding.checkboxTerms.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTermsAccepted(isChecked)
        }

        binding.etPropertyType.addTextChangedListener {
            viewModel.updatePropertyType(it.toString())
        }
        binding.etOfficeLocation.addTextChangedListener {
            viewModel.updateOfficeLocation(it.toString())
        }
        binding.etServiceDescription.addTextChangedListener {
            viewModel.updateServiceDescription(it.toString())
        }
    }

    private fun validateForm() {
        val isTipeLayananChecked = binding.checkboxRenovasi.isChecked || binding.checkboxBangunDariAwal.isChecked
        val isJasaKontraktorChecked = binding.checkboxKontraktor.isChecked || binding.checkboxTukang.isChecked

        viewModel.updateTermsAccepted(
            isTipeLayananChecked &&
                    isJasaKontraktorChecked &&
                    binding.etPropertyType.text.isNotEmpty() &&
                    binding.etOfficeLocation.text.isNotEmpty() &&
                    binding.etServiceDescription.text.isNotEmpty()
        )
    }

    private fun setupTermsTextView() {
        val termsText = "Saya telah memahami Terms & Referencesnya"
        val spannableString = SpannableString(termsText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTermsPopup()
            }
        }
        spannableString.setSpan(clickableSpan, 22, termsText.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTerms.text = spannableString
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showTermsPopup() {
        AlertDialog.Builder(this)
            .setTitle("Terms & References")
            .setMessage("Here are the terms and references of our app...")
            .setPositiveButton("OK", null)
            .show()
    }
}
