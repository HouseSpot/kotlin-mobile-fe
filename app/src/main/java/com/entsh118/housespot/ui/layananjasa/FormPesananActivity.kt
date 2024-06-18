package com.entsh118.housespot.ui.layananjasa

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.request.OrderRequest
import com.entsh118.housespot.databinding.ActivityFormPesananBinding
import com.entsh118.housespot.databinding.ActivityListVendorBinding
import com.entsh118.housespot.ui.layananjasa.viewmodel.FormPesananViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.Observer
import android.widget.Toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.TimeZone

class FormPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPesananBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val orderViewModel = FormPesananViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDatePicker()

        binding.addStory.setOnClickListener {
            val idPemesan = "590aa050-6b4b-409c-8611-a68967c4f2fa" // Example ID, replace with actual ID
            val idVendor = "vendor456" // Example ID, replace with actual ID
            val serviceType = if (binding.renovasi.isChecked) "Renovasi" else "Bangun dari awal"
            val propertyType = binding.jenisProperti.text.toString()
            val budget = binding.biaya.text.toString()
            val startDate = "2022-01-01"
            val endDate = "2023-01-01"
            val projectDescription = binding.deskripsi.text.toString()
            val materialProvider = if (binding.radioPirates.isChecked) "Kontraktor" else "Saya"

            val orderRequest = OrderRequest(
                idPemesan = idPemesan,
                idVendor = idVendor,
                serviceType = serviceType,
                propertyType = propertyType,
                budget = budget,
                startDate = startDate,
                endDate = endDate,
                projectDescription = projectDescription,
                materialProvider = materialProvider
            )

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

        orderViewModel.orderResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onFailure = { throwable ->
                    Toast.makeText(this, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })

    }

    private fun setupDatePicker() {
        binding?.startLayananLayout?.setEndIconOnClickListener {
            // Get the current date in milliseconds
            val today = System.currentTimeMillis()

            // Set up the calendar instance
            val calendar = Calendar.getInstance(TimeZone.getDefault())

            // Set up the start date (January 1, 1950)
            calendar.set(1950, Calendar.JANUARY, 1)
            val startDate = calendar.timeInMillis

            // Retrieve the birthDate argument
            val birthDate = "01-01-2000"
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val openAtDate: Long = if (birthDate.isNotEmpty()) {
                dateFormat.parse(birthDate)?.time ?: today
            } else {
                today
            }

            // Configure constraints to restrict the dates
            val constraints = CalendarConstraints.Builder()
                .setStart(startDate)
                .setEnd(today)
                .setOpenAt(openAtDate)
                .setValidator(DateValidatorPointBackward.now())
                .build()
            // Build the date picker
            val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_birth_date))
                .setSelection(openAtDate)
                .setCalendarConstraints(constraints)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTextInputFormat(dateFormat)
            val datePicker = datePickerBuilder.build()
         //   datePicker.show(childFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val date = dateFormat.format(Date(selection))
                binding?.startLayananText?.setText(date)
            }
        }
    }
}