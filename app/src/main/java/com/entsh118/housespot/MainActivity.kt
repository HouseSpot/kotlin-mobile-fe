package com.entsh118.housespot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.ui.auth.OnboardingActivity
import com.entsh118.housespot.ui.homepage.HomePageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = checkUserLoggedIn()

            if (isLoggedIn) {
                startActivity(Intent(this, HomePageActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, 3000)
    }

    private fun checkUserLoggedIn(): Boolean {
        // TODO: implement logic to check if user is logged in
        return false
    }
}
