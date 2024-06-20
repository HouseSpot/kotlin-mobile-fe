package com.entsh118.housespot.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ActivityAccountProfilePageBinding
import com.entsh118.housespot.ui.account.adapter.PortofolioVendorAccountAdapter
import com.entsh118.housespot.ui.account.viewmodel.AccountProfilePageViewModel
import kotlinx.coroutines.launch

class AccountProfilePageActivity : AppCompatActivity() {

    private lateinit var vpPortfolio: ViewPager2
    private lateinit var tvNoPortfolios: TextView
    private lateinit var userId: String
    private lateinit var peran: String

    private lateinit var binding: ActivityAccountProfilePageBinding
    private val viewModel: AccountProfilePageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        vpPortfolio = findViewById(R.id.vpPortfolio)
        tvNoPortfolios = findViewById(R.id.tvNoPortfolios)

        userId = intent.getStringExtra("userId") ?: ""
        peran = intent.getStringExtra("peran") ?: ""

        if (peran == "vendor") {
            if (userId != "") {
                viewModel.loadVendorDetails(userId)
            }

            setupVendorObserver()
        } else {
            updateClientUI()
        }

        viewModel.isLoading.observe(this) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (peran == "vendor") {
            if (userId != "") {
                viewModel.loadVendorDetails(userId)
            }

            setupVendorObserver()
        } else {
            updateClientUI()
        }
    }

    private fun setupVendorObserver() {
        viewModel.vendorDetails.observe(this, Observer { vendor ->
            if (vendor != null) {
                binding.tvName.text = vendor.pemilikInfo?.nama
                binding.tvEmail.text = vendor.pemilikInfo?.email

                val noHp = vendor.pemilikInfo?.noHp
                val phoneNumber = if (noHp?.startsWith("0") == true) {
                    "+62" + noHp.substring(1)
                } else {
                    noHp
                }
                binding.tvContact.text = phoneNumber

                Glide.with(this)
                    .load(vendor.profile)
                    .into(binding.civProfilePicture)

                if (vendor.portofolio.isNullOrEmpty() || vendor.portofolio.size == 0) {
                    vpPortfolio.visibility = View.GONE
                    tvNoPortfolios.visibility = View.VISIBLE
                } else {
                    vpPortfolio.adapter = PortofolioVendorAccountAdapter(vendor.portofolio)
                    vpPortfolio.visibility = View.VISIBLE
                    tvNoPortfolios.visibility = View.GONE
                }

                binding.tvTipeLayananValue.text = vendor.tipeLayanan?.joinToString(", ")
                binding.tvJenisPropertiValue.text = vendor.jenisProperti
                binding.tvJenisJasaValue.text = vendor.jasaKontraktor?.joinToString(", ")
                binding.tvFeeMinimumValue.text = vendor.feeMinimum
                binding.tvLokasiValue.text = vendor.lokasiKantor
                binding.tvDeskripsiValue.text = vendor.deskripsiLayanan

                binding.extraInfoContainer.visibility = View.VISIBLE

                binding.btnEditProfile.setOnClickListener {
                    Log.d("AccountProfilePage", "Phone: ${vendor.pemilikInfo?.noHp}")
                    val intent = Intent(this, AccountEditPageActivity::class.java).apply {
                        putExtra("userId", userId)
                        putExtra("peran", peran)
                        putExtra("name", vendor.pemilikInfo?.nama)
                        putExtra("email", vendor.pemilikInfo?.email)
                        putExtra("phone", vendor.pemilikInfo?.noHp)
                        putExtra("profile", vendor.profile)
                        putExtra("jenisProperti", vendor.jenisProperti)
                        putExtra("feeMinimum", vendor.feeMinimum)
                        putExtra("lokasiKantor", vendor.lokasiKantor)
                        putExtra("deskripsiLayanan", vendor.deskripsiLayanan)
                        putStringArrayListExtra("tipeLayanan", ArrayList(vendor.tipeLayanan))
                        putStringArrayListExtra("jasaKontraktor", ArrayList(vendor.jasaKontraktor))
                    }
                    startActivity(intent)
                }
            }
        })
    }

    private fun updateClientUI() {
        binding.extraInfoContainer.visibility = View.GONE

        setupGeneralUserData()

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, AccountEditPageActivity::class.java)
            lifecycleScope.launch {
                viewModel.userPreferencesFlow.collect { user ->
                    user?.let {
                        intent.putExtra("userId", it.id)
                        intent.putExtra("peran", it.peran)
                        intent.putExtra("name", it.nama)
                        intent.putExtra("email", it.email)
                        intent.putExtra("phone", it.noHp)
                        intent.putExtra("profile", it.profile)

                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val backButton: ImageView = binding.toolbarBackButton
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupGeneralUserData() {
        lifecycleScope.launch {
            viewModel.userPreferencesFlow.collect { user ->
                user?.let {
                    val name = it.nama
                    val email = it.email
                    val profile = it.profile
                    val phone = it.noHp

                    binding.tvName.text = name
                    binding.tvEmail.text = email

                    val noHp = phone
                    val phoneNumber = if (noHp.startsWith("0")) {
                        "+62" + noHp.substring(1)
                    } else {
                        noHp
                    }
                    binding.tvContact.text = phoneNumber

                    Glide.with(this@AccountProfilePageActivity)
                        .load(profile)
                        .into(binding.civProfilePicture)
                }
            }
        }
    }
}
