package com.entsh118.housespot.ui.layananjasa

import com.entsh118.housespot.ui.layananjasa.FormPesananActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.databinding.ActivityDetailJasaBinding
import com.entsh118.housespot.ui.layananjasa.adapter.PortfolioAdapter

class DetailJasaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJasaBinding
    private lateinit var vendor: VendorResponseItem

    companion object {
        const val DETAIL_VENDOR = "detail_vendor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJasaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detail = intent.getParcelableExtra<VendorResponseItem>(DETAIL_VENDOR)
        detail?.let { setupAction(it) }
        binding.buttonPesanLayanan.setOnClickListener {
            val intent = Intent(this@DetailJasaActivity, FormPesananActivity::class.java)
            intent.putExtra(FormPesananActivity.DETAIL_VENDOR, detail)
            startActivity(intent)
        }

    }

    private fun setupAction(detail: VendorResponseItem) {
        binding.description.text = detail.deskripsiLayanan
        binding.rating.text = detail.rating.toString()
        binding.namaVendor.text = detail.pemilikInfo?.nama
        binding.lokasi.text = detail.lokasiKantor
        // Set the type of property
        binding.jenisProperti.text = detail.jenisProperti
        // Set the vendor's services
        binding.layanan.text = detail.tipeLayanan?.joinToString(", ")
        // Set the type of vendor
        binding.jenisVendor.text = detail.jasaKontraktor?.joinToString(", ")
        // Set the vendor's contact email
        binding.kontak.text = detail.pemilikInfo?.email
        setupRecyclerView(detail.portofolio)
    }

    private fun setupRecyclerView(portofolio: List<String?>?) {
        portofolio?.let {
            val adapter = PortfolioAdapter(it.filterNotNull())
            binding.portfolioRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.portfolioRecyclerView.adapter = adapter
        }
    }
}