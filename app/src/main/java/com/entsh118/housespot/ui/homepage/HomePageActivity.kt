package com.entsh118.housespot.ui.homepage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.model.UserPreferences
import com.entsh118.housespot.databinding.ActivityHomePageBinding
import com.entsh118.housespot.ui.account.AccountHomepageActivity
import com.entsh118.housespot.ui.layananjasa.ListVendorActivity
import com.entsh118.housespot.ui.pesanan.PesananClientActivity
import com.entsh118.housespot.ui.prediksiharga.PrediksiFormActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(applicationContext)

        setupRecyclerView()
        setupServiceNavigations()
        loadUserData()
        loadServiceRecommendations()
        setupBottomNavigation()

    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun setupRecyclerView() {
        binding.recommendationsRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val userPreferences = dataStoreManager.userPreferencesFlow.first()
            updateUIWithUserData(userPreferences)
        }
    }

    private fun updateUIWithUserData(userPreferences: UserPreferences) {
        binding.tvGreeting.text = "Hallo, ${userPreferences.nama}!"
        Log.d("HomePageActivity", "User name: ${userPreferences.nama}")
        Log.d("HomePageActivity", "User id: ${userPreferences.id}")
        Log.d("HomePageActivity", "User profile: ${userPreferences.profile}")

        val profile = userPreferences.profile

        if (profile != null) {
            if (profile.isNotEmpty()){
                binding.ivProfile.setImageUrl(profile)
            }
        }
    }

    private fun setupServiceNavigations() {
        binding.llEstimasiHarga.setOnClickListener {
            val intent = Intent(this, PrediksiFormActivity::class.java)
            startActivity(intent)
        }
        binding.revovasiServiceButton.setOnClickListener {
            val intent = Intent(this, ListVendorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadServiceRecommendations() {
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this, PesananClientActivity::class.java))
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this, AccountHomepageActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.nav_home
    }
}
