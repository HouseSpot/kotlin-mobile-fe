package com.entsh118.housespot.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.entsh118.housespot.R
import com.entsh118.housespot.ui.auth.viewmodel.OnboardingViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.gms.auth.api.signin.GoogleSignIn

class OnboardingActivity : AppCompatActivity() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        findViewById<Button>(R.id.btnJoinNow).setOnClickListener {
            startActivity(Intent(this, RoleSelectionActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnSignInWithGoogle).setOnClickListener {
            viewModel.signInWithGoogle()
        }

        findViewById<TextView>(R.id.tvSignIn).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.signInResult.observe(this, Observer { account ->
            if (account != null) {
                // Handle Google Sign-In success
                // Redirect to RoleSelectionActivity for further registration or HomePageActivity if already registered
            }
        })

        viewModel.signInError.observe(this, Observer { exception ->
            if (exception != null) {
                // Handle sign-in error
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleSignInResult(task)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
