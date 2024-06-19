package com.entsh118.housespot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.ui.auth.OnboardingActivity
import com.entsh118.housespot.ui.homepage.HomePageActivity
import com.entsh118.housespot.ui.homepage.VendorHomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataStoreManager = DataStoreManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val isLoggedIn = checkUserLoggedIn()
                if (isLoggedIn) {
                    val role = dataStoreManager.userPreferencesFlow.first().peran
                    if (role == "vendor") {
                        startActivity(Intent(this@MainActivity, VendorHomeActivity::class.java))
                    } else{
                        startActivity(Intent(this@MainActivity, HomePageActivity::class.java))
                    }
                } else {
                    startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                }
                finish()
            }
        }, 3000)
    }

    private suspend fun checkUserLoggedIn(): Boolean {
        val userPreferences = dataStoreManager.userPreferencesFlow.first()
        return userPreferences.id.isNotEmpty()
    }
}
