package com.entsh118.housespot.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.entsh118.housespot.databinding.ActivityAccountHomepageBinding
import com.entsh118.housespot.databinding.ActivityAccountVendorBinding
import com.entsh118.housespot.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class AccountVendorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountVendorBinding
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()

        binding.rlProfile.setOnClickListener {
            // Handle profile click
        }

        binding.rlLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                startActivity(Intent(this@AccountVendorActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            viewModel.userPreferencesFlow.collect { user ->
                user?.let {
                    binding.userName.text = it.nama
                    binding.userEmail.text = it.email
                    Glide.with(this@AccountVendorActivity)
                        .load(it.profile)
                        .circleCrop()
                        .into(binding.profileImage)
                }
            }
        }
    }

}
