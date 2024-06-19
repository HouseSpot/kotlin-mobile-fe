package com.entsh118.housespot.ui.orderdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.entsh118.housespot.custom.StatusSpinnerAdapter
import com.entsh118.housespot.databinding.ActivityVendorOrderDetailsBinding
import com.entsh118.housespot.ui.orderdetails.viewmodel.VendorOrderDetailsViewModel

class VendorOrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorOrderDetailsBinding
    private val viewModel: VendorOrderDetailsViewModel by viewModels()
    private var isInitialSpinnerSetup = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_id")

        if (orderId != null) {
            viewModel.loadOrderDetails(orderId)
        }

        setupObservers()
        setupToolbar()
        setupSpinner()
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

    private fun setupObservers() {
        viewModel.orderDetails.observe(this, Observer { order ->
            if (order != null) {
                binding.tvJenisLayananValue.text = order.serviceType
                binding.tvJenisPropertiValue.text = order.propertyType
                binding.tvBudgetTotalValue.text = order.budget
                binding.tvWaktuMulaiValue.text = order.startDate
                binding.tvWaktuSelesaiValue.text = order.endDate
                binding.tvDeskripsiProyekValue.text = order.projectDescription
                binding.tvPenyediaMaterialValue.text = order.materialProvider

                // Set initial value for spinner
                val statusPosition = (binding.spinnerStatus.adapter as StatusSpinnerAdapter).getPosition(order.status?.toUpperCase())
                binding.spinnerStatus.setSelection(statusPosition)
            }
        })

        viewModel.userDetails.observe(this, Observer { user ->
            if (user != null) {
                binding.tvNamaClientValue.text = user.nama

                binding.llKontakClient.setOnClickListener {
                    val noHp = user.noHp
                    val phoneNumber = if (noHp.startsWith("0")) {
                        "62" + noHp.substring(1)
                    } else {
                        noHp
                    }
                    val url = "https://wa.me/$phoneNumber"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun setupSpinner() {
        val statuses = listOf("WAITING", "ONGOING", "COMPLETED", "REJECTED")
        val adapter = StatusSpinnerAdapter(this, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter

        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isInitialSpinnerSetup) {
                    isInitialSpinnerSetup = false
                    return
                }

                val selectedStatus = statuses[position]
                val orderId = intent.getStringExtra("order_id")
                if (orderId != null) {
                    viewModel.updateOrderStatus(orderId, selectedStatus)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}
