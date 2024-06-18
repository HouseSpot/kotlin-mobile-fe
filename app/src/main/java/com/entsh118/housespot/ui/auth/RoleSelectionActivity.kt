package com.entsh118.housespot.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ActivityRoleSelectionBinding
import com.entsh118.housespot.ui.auth.viewmodel.RoleSelectionViewModel

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding
    private val viewModel: RoleSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupObservers()

        binding.roleVendor.setOnClickListener {
            viewModel.selectRole("Vendor")
        }

        binding.roleClient.setOnClickListener {
            viewModel.selectRole("Client")
        }

        binding.btnConfirm.setOnClickListener {
            if (viewModel.selectedRole.value == null) {
                Toast.makeText(this, "Please select a role before proceeding", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CreateAccountActivity::class.java)
                intent.putExtra("ROLE", viewModel.selectedRole.value)
                startActivity(intent)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        val backButton: ImageView = binding.toolbarBackButton
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.selectedRole.observe(this, Observer { role ->
            val yellowColor = ContextCompat.getColor(this, R.color.yellow)
            val whiteColor = ContextCompat.getColor(this, R.color.white)
            binding.roleVendor.setBackgroundColor(if (role == "Vendor") yellowColor else whiteColor)
            binding.roleClient.setBackgroundColor(if (role == "Client") yellowColor else whiteColor)
        })
    }
}
