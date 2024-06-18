package com.entsh118.housespot.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.entsh118.housespot.ui.layananjasa.viewmodel.ListVendorViewModel
import com.entsh118.housespot.ui.pesanan.viewmodel.PesananViewModel

class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListVendorViewModel::class.java) -> {
                ListVendorViewModel() as T
            }modelClass.isAssignableFrom(PesananViewModel::class.java) -> {
                PesananViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}