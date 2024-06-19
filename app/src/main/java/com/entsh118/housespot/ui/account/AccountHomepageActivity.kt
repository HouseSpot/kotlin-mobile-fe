package com.entsh118.housespot.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ActivityAccountHomepageBinding
import com.entsh118.housespot.ui.account.viewmodel.AccountViewModel
import com.entsh118.housespot.ui.auth.LoginActivity
import com.entsh118.housespot.ui.homepage.HomePageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AccountHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountHomepageBinding
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()

        setupBottomNavigation()

        binding.rlProfile.setOnClickListener {
            lifecycleScope.launch {
                viewModel.userPreferencesFlow.collect { user ->
                    user?.let {
                        val userId = it.id
                        val peran = it.peran
                        val intent = Intent(this@AccountHomepageActivity, AccountProfilePageActivity::class.java)
                        intent.putExtra("userId", userId)
                        intent.putExtra("peran", peran)
                        startActivity(intent)
                    }
                }
            }
        }

        binding.rlLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                startActivity(Intent(this@AccountHomepageActivity, LoginActivity::class.java))
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
                    Glide.with(this@AccountHomepageActivity)
                        .load(it.profile)
                        .circleCrop()
                        .into(binding.profileImage)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()

    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomePageActivity::class.java))
                    true
                }
                R.id.nav_orders -> {
                    true
                }
                R.id.nav_account -> {
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.nav_account
    }

}
