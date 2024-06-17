package com.entsh118.housespot.ui.layananjasa

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entsh118.housespot.R
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.entsh118.housespot.databinding.ActivityFormPesananBinding
import com.entsh118.housespot.databinding.ActivityListVendorBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormPesananActivity : AppCompatActivity() {
    private lateinit var btnDatePicker: Button
    private lateinit var tvSelectedDate: TextView
    private val calendar = Calendar.getInstance()
    private lateinit var binding: ActivityFormPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pesanan)

    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
              //  tvSelectedDate.text = "Selected Date: $formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}