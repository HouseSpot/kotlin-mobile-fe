package com.entsh118.housespot.ui.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.databinding.ActivityVendorHomeBinding
import com.entsh118.housespot.ui.homepage.adapter.VendorPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class VendorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load profile image dynamically if needed
        // Example: binding.ivProfile.setImageUrl("http://example.com/path/to/image.jpg")

        // Load other data and set UI elements accordingly
        binding.tvGreeting.text = "Hallo, Fitira!"
        binding.tvProjectInfo.text = "Berikut informasi proyekmu!"

        // Set up tab layout with view pager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = VendorPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pesanan Aktif"
                else -> "Riwayat Pesanan"
            }
        }.attach()
    }
}
