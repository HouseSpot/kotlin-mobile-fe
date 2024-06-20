package com.entsh118.housespot.ui.layananjasa

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.R
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.model.UserPreferences
import com.entsh118.housespot.data.api.request.OrderRequest
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.databinding.ActivityFormPesananBinding
import com.entsh118.housespot.ui.layananjasa.viewmodel.FormPesananViewModel
import com.entsh118.housespot.ui.pesanan.PesananClientActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
class FormPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPesananBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val orderViewModel = FormPesananViewModel()
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDatePicker()
        dataStoreManager = DataStoreManager(applicationContext)
        loadUserData()

        val detail = intent.getParcelableExtra<VendorResponseItem>(DETAIL_VENDOR)

        val textFields = listOf(
            binding.jenisProperti,
            binding.biaya,
            binding.startLayananText,
            binding.endDateEditText,
            binding.deskripsi
        )

        val radioButtons = listOf(
            binding.renovasi,
            binding.radioPirates,
            binding.radioNinjas
        )

        textFields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkAllFields()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        radioButtons.forEach { radioButton ->
            radioButton.setOnCheckedChangeListener { _, _ ->
                checkAllFields()
            }
        }

        binding.addStory.setOnClickListener {

            val idPemesan = userPreferences.id // Example ID, replace with actual ID
            val idVendor = detail?.id
            val serviceType = if (binding.renovasi.isChecked) "Renovasi" else "Bangun dari awal"
            val propertyType = binding.jenisProperti.text.toString()
            val budget = binding.biaya.text.toString()
            val startDate = binding.startLayananText.text.toString()
            val endDate = binding.endDateEditText.text.toString()
            val projectDescription = binding.deskripsi.text.toString()
            val materialProvider = if (binding.radioPirates.isChecked) "Kontraktor" else "Saya"

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val startDateParsed = dateFormat.parse(startDate)
            val endDateParsed = dateFormat.parse(endDate)

            if (startDateParsed != null && endDateParsed != null && startDateParsed.after(endDateParsed)) {
                Toast.makeText(this, "Tanggal mulai tidak boleh lebih besar dari tanggal selesai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val orderRequest =
                OrderRequest(
                    idPemesan = idPemesan,
                    idVendor = idVendor.toString(),
                    serviceType = serviceType,
                    propertyType = propertyType,
                    budget = budget,
                    startDate = startDate,
                    endDate = endDate,
                    projectDescription = projectDescription,
                    materialProvider = materialProvider
                )

            if (orderRequest != null) {
                orderViewModel.addOrder(
                    orderRequest.idPemesan,
                    orderRequest.idVendor,
                    orderRequest.serviceType,
                    orderRequest.propertyType,
                    orderRequest.budget,
                    orderRequest.startDate,
                    orderRequest.endDate,
                    orderRequest.projectDescription,
                    orderRequest.materialProvider
                )
            }
        }

        orderViewModel.orderResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onFailure = { throwable ->
                    Toast.makeText(this, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
            )
            backToDashboard()
        })

    }

    private fun checkAllFields() {
        val isAllFieldsFilled = binding.jenisProperti.text.isNotEmpty() &&
                binding.biaya.text.isNotEmpty() &&
                binding.startLayananText.text.toString().isNotEmpty() &&
                binding.endDateEditText.text.toString().isNotEmpty() &&
                binding.deskripsi.text.isNotEmpty() &&
                (binding.renovasi.isChecked || binding.radioPirates.isChecked || binding.radioNinjas.isChecked)

        binding.addStory.isEnabled = isAllFieldsFilled
        if (isAllFieldsFilled) {
            binding.formInstruction.visibility = View.GONE
        } else{
            binding.formInstruction.visibility = View.VISIBLE
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            userPreferences = dataStoreManager.userPreferencesFlow.first()
        }
    }

    private fun setupDatePicker() {
        binding.startLayananLayout.setEndIconOnClickListener {
            pickDate("start")
        }
        binding.endDateEditTextLayout.setEndIconOnClickListener {
            pickDate("end")
        }
    }

    private fun pickDate(jenis: String) {
        val today = System.currentTimeMillis()
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val startDate = today
        calendar.add(Calendar.YEAR, 50)
        val endDate = calendar.timeInMillis

        val constraints = CalendarConstraints.Builder()
            .setStart(startDate)
            .setEnd(endDate)
            .setOpenAt(today)
            .build()

        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(today)
            .setCalendarConstraints(constraints)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)

        val datePicker = datePickerBuilder.build()
        datePicker.show(supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { selection ->
            val date = dateFormat.format(Date(selection))
            if (jenis == "end") {
                binding.endDateEditText.setText(date)
            } else {
                binding.startLayananText.setText(date)
            }
            checkAllFields()
        }
    }
    private fun backToDashboard() {
        val intent = Intent(this, PesananClientActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        const val DETAIL_VENDOR = "detail_vendor"
    }
}
