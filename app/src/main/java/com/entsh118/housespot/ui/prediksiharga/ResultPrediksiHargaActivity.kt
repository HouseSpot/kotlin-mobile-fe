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
        return when (prediction) {
            "0-250 juta" -> Triple("Rp 0 - 250 juta", "250000000", "0")
            "250-500 juta" -> Triple("Rp 250 - 500 juta", "500000000", "250000000")
            "500-750 juta" -> Triple("Rp 500 - 750 juta", "750000000", "500000000")
            "750 juta-1 miliar" -> Triple("Rp 750 juta - 1 miliar", "1000000000", "750000000")
            "1-1.5 miliar" -> Triple("Rp 1 - 1.5 miliar", "1500000000", "1000000000")
            "1.5-2 miliar" -> Triple("Rp 1.5 - 2 miliar", "2000000000", "1500000000")
            "2-2.5 miliar" -> Triple("Rp 2 - 2.5 miliar", "2500000000", "2000000000")
            "2.5-3 miliar" -> Triple("Rp 2.5 - 3 miliar", "3000000000", "2500000000")
            "3-4 miliar" -> Triple("Rp 3 - 4 miliar", "4000000000", "3000000000")
            "4-5 miliar" -> Triple("Rp 4 - 5 miliar", "5000000000", "4000000000")
            "lebih dari 5 miliar" -> Triple("Lebih dari 5 miliar", "0", "0")
            else -> Triple("Invalid Prediction. Please adjust your house criteria.", "0", "0")
        }
    }
}
