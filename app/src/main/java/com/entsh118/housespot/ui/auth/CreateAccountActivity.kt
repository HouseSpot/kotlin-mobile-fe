package com.entsh118.housespot.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.entsh118.housespot.databinding.ActivityCreateAccountBinding
import com.entsh118.housespot.ui.auth.viewmodel.CreateAccountViewModel

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private val viewModel: CreateAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.name.observe(this, Observer { binding.etName.setText(it) })
        viewModel.email.observe(this, Observer { binding.etEmail.setText(it) })
        viewModel.password.observe(this, Observer { binding.etPassword.setText(it) })
        viewModel.confirmPassword.observe(this, Observer { binding.etConfirmPassword.setText(it) })
        viewModel.phone.observe(this, Observer { binding.etPhone.setText(it) })

        viewModel.isFormValid.observe(this, Observer { isValid ->
            binding.btnNext.isEnabled = isValid
        })
    }

    private fun setupListeners() {
        binding.etName.addTextChangedListener { viewModel.updateName(it.toString()) }
        binding.etEmail.addTextChangedListener { viewModel.updateEmail(it.toString()) }
        binding.etPassword.addTextChangedListener { viewModel.updatePassword(it.toString()) }
        binding.etConfirmPassword.addTextChangedListener { viewModel.updateConfirmPassword(it.toString()) }
        binding.etPhone.addTextChangedListener { viewModel.updatePhone(it.toString()) }

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val phone = binding.etPhone.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed to next step
                // Navigate to login page or other action
            }
        }
    }
}
