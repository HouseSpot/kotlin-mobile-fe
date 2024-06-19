package com.entsh118.housespot.ui.homepage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.databinding.ActivityVendorHomeBinding
import com.entsh118.housespot.ui.account.AccountVendorActivity
import com.entsh118.housespot.ui.homepage.adapter.VendorPagerAdapter
import com.entsh118.housespot.ui.premium.PremiumVendorActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VendorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorHomeBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        dataStoreManager = DataStoreManager(this)
        updateUI()

        binding.ivSettings.setOnClickListener(){
            startActivity(Intent(this, AccountVendorActivity::class.java))
        }

        binding.rlPromo.setOnClickListener(){
            startActivity(Intent(this, PremiumVendorActivity::class.java))
        }

        viewPager.adapter = VendorPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pesanan Aktif"
                else -> "Riwayat Pesanan"
            }
        }.attach()
    }

    fun updateUI(){
        lifecycleScope.launch {
            val nama = dataStoreManager.userPreferencesFlow.first().nama
            val profile = dataStoreManager.userPreferencesFlow.first().profile
            binding.tvGreeting.text = "Hallo, $nama!"
            binding.tvProjectInfo.text = "Berikut informasi proyekmu!"

            if (profile != null) {
                if (profile.isNotEmpty()){
                    binding.ivProfile.setImageUrl(profile)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
    
}
