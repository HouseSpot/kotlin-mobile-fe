package com.entsh118.housespot.ui.layananjasa

import android.app.SearchManager
import android.content.Context
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
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.CompoundButton
import android.widget.RadioButton
import com.entsh118.housespot.data.api.model.Vendor
import com.entsh118.housespot.databinding.ButtomSheetsFilterBinding
import java.util.*

class ListVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListVendorBinding
    private lateinit var viewModel: ListVendorViewModel
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var cbTerjelek: RadioButton
    lateinit var cbTerbaik: RadioButton
    lateinit var adapter : ListVendorAdapter
    var modelMainList: MutableList<Vendor> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvListVendor.layoutManager = LinearLayoutManager(this) // tambahkan ini


        viewModel = ViewModelProvider(this, ViewModelFactory())[
            ListVendorViewModel::class.java]

        viewModel.listVendor.observe(this) { listVendor -> setListVendorData(listVendor) }

        binding.fabFilter.setOnClickListener{
            setFilterData()
        }
    }

    private fun setListVendorData(listVendor: List<VendorResponseItem>) {
        adapter = ListVendorAdapter(listVendor)
        binding.rvListVendor.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    fun setFilterData(){
        val dialogView = layoutInflater.inflate(R.layout.buttom_sheets_filter, null)
        cbTerjelek = dialogView.findViewById(R.id.cbTerdekat)
        cbTerbaik = dialogView.findViewById(R.id.cbTerjauh)

        bottomSheetDialog = BottomSheetDialog(this@ListVendorActivity)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()

        cbTerjelek.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                Collections.sort(modelMainList, Vendor.sortByAscRating)
                adapter.notifyDataSetChanged()
            }
        }

        cbTerbaik.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                Collections.sort(modelMainList, Vendor.sortByDescRating)
                adapter.notifyDataSetChanged()
            }

    }
    }
}