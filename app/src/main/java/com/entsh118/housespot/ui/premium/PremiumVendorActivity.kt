package com.entsh118.housespot.ui.premium

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.entsh118.housespot.R

class PremiumVendorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_premium)

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        val backButton: ImageView = findViewById(R.id.toolbar_back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
