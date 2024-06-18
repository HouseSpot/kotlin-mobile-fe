package com.entsh118.housespot.ui.pesanan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.entsh118.housespot.databinding.ActivityPesananClientBinding
import com.entsh118.housespot.ui.ViewModelFactory
import com.entsh118.housespot.ui.pesanan.viewmodel.PesananViewModel
import  com.entsh118.housespot.ui.pesanan.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.R
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.ui.account.AccountHomepageActivity
import com.entsh118.housespot.ui.homepage.HomePageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PesananClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananClientBinding
    private lateinit var viewModel: PesananViewModel
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelFactory())[
            PesananViewModel::class.java]
        dataStoreManager = DataStoreManager(applicationContext)

        loadUserData()
        // untuk fragment
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        //sectionsPagerAdapter.username = user.login

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val userPreferences = dataStoreManager.userPreferencesFlow.first()
            viewModel.getPesananAktif(userPreferences.id)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf( R.string.tab_text_1, R.string.tab_text_2)
        const val EXTRA_USER = "extra_user"
    }

}