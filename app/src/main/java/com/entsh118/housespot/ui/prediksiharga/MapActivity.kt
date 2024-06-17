package com.entsh118.housespot.ui.prediksiharga

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.entsh118.housespot.R

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val jakarta = LatLng(-6.2088, 106.8456)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 12f))

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            val intent = Intent()
            intent.putExtra("latitude", latLng.latitude)
            intent.putExtra("longitude", latLng.longitude)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
