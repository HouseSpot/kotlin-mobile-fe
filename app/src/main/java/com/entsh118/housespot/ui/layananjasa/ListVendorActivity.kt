package com.entsh118.housespot.ui.layananjasa

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ActivityListVendorBinding
import com.entsh118.housespot.ui.layananjasa.viewmodel.ListVendorViewModel
import androidx.lifecycle.ViewModelProvider
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.ui.ViewModelFactory
import com.entsh118.housespot.ui.layananjasa.adapter.ListVendorAdapter
import android.util.Log
class ListVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListVendorBinding
    private lateinit var viewModel: ListVendorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("FITUR LIST VENDOR","START LIST VENDOR")
        super.onCreate(savedInstanceState)
        binding = ActivityListVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvListVendor.layoutManager = LinearLayoutManager(this) // tambahkan ini


        viewModel = ViewModelProvider(this, ViewModelFactory())[
            ListVendorViewModel::class.java]

        viewModel.listVendor.observe(this) { listVendor -> setListVendorData(listVendor) }
    }

    private fun setListVendorData(listVendor: List<VendorResponseItem>) {
        Log.e("CEK LIST",listVendor[0].toString())
        val adapter = ListVendorAdapter(listVendor)
        Log.e("CEK LIST count di adapter", adapter.itemCount.toString())
        binding.rvListVendor.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}