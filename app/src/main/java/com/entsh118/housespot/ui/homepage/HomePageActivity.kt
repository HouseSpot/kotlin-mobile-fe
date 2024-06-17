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
        loadUserData()
        loadServiceRecommendations()
        setupBottomNavigation()

        binding.revovasiServiceButton.setOnClickListener {
            startActivity( Intent(this@HomePageActivity, ListVendorActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recommendationsRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        // Set your adapter here, for example:
        // binding.recommendationsRecyclerview.adapter = RecommendationsAdapter()
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
        Glide.with(this).load(userPreferences.profile).into(binding.ivProfile)
    }

    private fun loadServiceRecommendations() {
        // Load your service recommendations here
        // For example, you can set your RecyclerView adapter with data
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home navigation
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
