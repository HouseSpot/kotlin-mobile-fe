package com.entsh118.housespot.ui.auth

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.textfield.EmailTextField
import com.entsh118.housespot.custom.textfield.PasswordTextField
import com.entsh118.housespot.ui.auth.viewmodel.LoginViewModel
import com.entsh118.housespot.ui.homepage.HomePageActivity
import com.entsh118.housespot.ui.homepage.VendorHomeActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var etEmail: EmailTextField
    private lateinit var etPassword: PasswordTextField
    private lateinit var btnLogin: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressIndicator = findViewById(R.id.progressIndicator)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressIndicator.visibility = View.VISIBLE
            } else {
                progressIndicator.visibility = View.GONE
            }
        })

        loginViewModel.loginResult.observe(this, Observer { loginResponse ->
            Toast.makeText(this, "Login successful: ${loginResponse.message}", Toast.LENGTH_SHORT).show()
            if (loginResponse.data.peran == "vendor") {
                val intent = Intent(this, VendorHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        loginViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        })

        // Check if user is already logged in
        lifecycleScope.launch {
            loginViewModel.userPreferencesFlow.collect { userPreferences ->
//                if (userPreferences.id.isNotEmpty()) {
//                    // User is logged in, navigate to home
//                    val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        val backButton: ImageView = findViewById(R.id.toolbar_back_button)
        backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
