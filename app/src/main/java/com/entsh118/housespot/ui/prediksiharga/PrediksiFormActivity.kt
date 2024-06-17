package com.entsh118.housespot.ui.prediksiharga

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.request.PredictionRequest
import com.entsh118.housespot.databinding.ActivityPrediksiFormBinding
import com.entsh118.housespot.ui.prediksiharga.viewmodel.PrediksiFormViewModel
import org.json.JSONObject

class PrediksiFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrediksiFormBinding
    private val viewModel: PrediksiFormViewModel by viewModels()
    private val mapRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrediksiFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCitySpinner()
        setupDistrictSpinner()

        binding.tvPilih.setOnClickListener {
            openMapActivity()
        }

        binding.btnPredictPrice.setOnClickListener {
            validateAndSubmitForm()
        }

        setupObservers()

        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        })
    }

    private fun openMapActivity() {
        val intent = Intent(this, MapActivity::class.java)
        startActivityForResult(intent, mapRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mapRequestCode && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = data?.getDoubleExtra("longitude", 0.0) ?: 0.0
            binding.tvPilihLokasi.text = "Lat: $latitude \nLon: $longitude"
            viewModel.updateCoordinates(latitude, longitude)
        }
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

    private fun setupCitySpinner() {
        val cityJson = assets.open("encoding_city_dict.json").bufferedReader().use { it.readText() }
        val cityOptions = getOptionsFromJson(cityJson)
        val cityAdapter = ArrayAdapter(this, R.layout.spinner_item, cityOptions)
        cityAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerCity.adapter = cityAdapter
    }

    private fun setupDistrictSpinner() {
        val districtJson = assets.open("encoding_district_dict.json").bufferedReader().use { it.readText() }
        val districtOptions = getOptionsFromJson(districtJson)
        val districtAdapter = ArrayAdapter(this, R.layout.spinner_item, districtOptions)
        districtAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerDistrict.adapter = districtAdapter
    }

    private fun getOptionsFromJson(jsonString: String): List<String> {
        val jsonObject = JSONObject(jsonString)
        val options = mutableListOf<String>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            options.add(key)
        }
        return options
    }

    private fun validateAndSubmitForm() {
        val city = binding.spinnerCity.selectedItem?.toString() ?: ""
        val district = binding.spinnerDistrict.selectedItem?.toString() ?: ""
        val latitude = viewModel.latitude.value ?: 0.0
        val longitude = viewModel.longitude.value ?: 0.0
        val landSize = binding.etLuasTanah.text.toString().toDoubleOrNull() ?: 0.0
        val buildingSize = binding.etLuasBangunan.text.toString().toDoubleOrNull() ?: 0.0
        val floors = binding.etJumlahLantai.text.toString().toIntOrNull() ?: 0
        val bedrooms = binding.etJumlahKamarTidur.text.toString().toIntOrNull() ?: 0
        val bathrooms = binding.etJumlahKamarMandi.text.toString().toIntOrNull() ?: 0
        val carportGarage = binding.etGarage.text.toString().toIntOrNull() ?: 0

        if (city.isEmpty() || district.isEmpty() || latitude == 0.0 || longitude == 0.0 ||
            landSize == 0.0 || buildingSize == 0.0 || floors == 0 || bedrooms == 0 || bathrooms == 0 || carportGarage == 0) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        } else {
            val predictionRequest = PredictionRequest(
                city, district, latitude, longitude, landSize, buildingSize, floors, bedrooms, bathrooms, carportGarage
            )
            viewModel.submitPrediction(predictionRequest)
        }
    }

    private fun setupObservers() {
        viewModel.predictionResult.observe(this, Observer { result ->
            if (result.startsWith("Prediction failed")) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Prediction successful: $result", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ResultPrediksiHargaActivity::class.java).apply {
                    putExtra("prediction", result)
                }
                startActivity(intent)
            }
        })
    }
}
