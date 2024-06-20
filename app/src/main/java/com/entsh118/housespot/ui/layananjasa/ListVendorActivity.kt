package com.entsh118.housespot.ui.layananjasa

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
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
import android.view.View
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.CompoundButton
import android.widget.RadioButton
import com.entsh118.housespot.data.api.model.Vendor
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import com.entsh118.housespot.databinding.ButtomSheetsFilterBinding

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
            showFilterDialog()
        }

        viewModel.getVendor()
    }

    private fun setListVendorData(listVendor: List<VendorResponseItem>) {
        adapter = ListVendorAdapter(listVendor)
        binding.rvListVendor.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun showFilterDialog() {
        val dialogView = ButtomSheetsFilterBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView.root)
        bottomSheetDialog.show()

        dialogView.buttonFilter.setOnClickListener {
            val chipGroupTipeLayanan = dialogView.chipGroupTipeLayanan
            val chipGroupJenisJasa = dialogView.chipGroupJenisJasa
            val etHargaMinimum = dialogView.hargaMinimum
            val etHargaMaksimum = dialogView.hargaMaksimum

            val selectedTipeLayanan = getSelectedChips(chipGroupTipeLayanan)
            val selectedJenisJasa = getSelectedChips(chipGroupJenisJasa)
            val hargaMinimum = etHargaMinimum.text.toString().trim()
            val hargaMaksimum = etHargaMaksimum.text.toString().trim()

            // Validate hargaMinimum and hargaMaksimum, and convert them to Long if valid
            val hargaMin: Long? = if (hargaMinimum.isNotEmpty()) hargaMinimum.toLong() else null
            val hargaMax: Long? = if (hargaMaksimum.isNotEmpty()) hargaMaksimum.toLong() else null

            // Prepare filter parameters
            val lokasiKantor = "" // Example, you can change this as needed
            val tipeLayanan = selectedTipeLayanan.joinToString(",")
            val jenisJasa = selectedJenisJasa.joinToString(",")
            val namaVendor = ""

            // Call viewModel to apply filters
            viewModel.filterVendors(lokasiKantor, tipeLayanan, jenisJasa, namaVendor, hargaMin, hargaMax)

            bottomSheetDialog.dismiss()
        }
    }

    private fun getSelectedChips(chipGroup: ChipGroup): List<String> {
        val selectedChips = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChips.add(chip.text.toString())
            }
        }
        return selectedChips
    }
}