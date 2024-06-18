package com.entsh118.housespot.ui.prediksiharga

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ActivityResultPrediksiHargaBinding

class ResultPrediksiHargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPrediksiHargaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPrediksiHargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prediction = intent.getStringExtra("prediction")

        val (priceRange, maxPrice, minPrice) = parsePrediction(prediction)

        binding.priceEstimationValue.text = priceRange

        binding.viewPropertyButton.setOnClickListener {
            val url = "https://www.rumah123.com/jual/cari/?maxPrice=$maxPrice&minPrice=$minPrice"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.toolbarBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun parsePrediction(prediction: String?): Triple<String, String, String> {
        return if (prediction.isNullOrEmpty()) {
            Triple("Invalid Prediction. Please adjust your house criteria.", "0", "0")
        } else {
            val parts = prediction.split("-")
            val minPrice = parts[0].trim().toDoubleOrNull() ?: 0.0
            val maxPrice = parts[1].trim().toDoubleOrNull() ?: 0.0

            val minPriceInRupiah = if (prediction.contains("miliar")) (minPrice * 1_000_000_000).toInt() else (minPrice * 1_000_000).toInt()
            val maxPriceInRupiah = if (prediction.contains("miliar")) (maxPrice * 1_000_000_000).toInt() else (maxPrice * 1_000_000).toInt()

            Triple(prediction, maxPriceInRupiah.toString(), minPriceInRupiah.toString())
        }
    }
}
