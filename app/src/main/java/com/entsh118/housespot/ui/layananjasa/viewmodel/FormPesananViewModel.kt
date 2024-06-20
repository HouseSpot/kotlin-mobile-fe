package com.entsh118.housespot.ui.layananjasa.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import androidx.lifecycle.lifecycleScope
import com.entsh118.housespot.data.api.model.Order
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.request.OrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.HttpException

class FormPesananViewModel: ViewModel() {

    private val _orderResult = MutableLiveData<Result<String>>()
    val orderResult: LiveData<Result<String>> = _orderResult

    fun addOrder(
        idPemesan: String,
        idVendor: String,
        serviceType: String,
        propertyType: String,
        budget: String,
        startDate: String,
        endDate: String,
        projectDescription: String,
        materialProvider: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            try {
                val response: Response<Void> = ApiConfig.getClientService().addOrder(
                    idPemesan,
                    idVendor,
                    serviceType,
                    propertyType,
                    budget,
                    startDate,
                    endDate,
                    projectDescription,
                    materialProvider
                ).execute()
                if (response.isSuccessful) {
                    _orderResult.postValue(Result.success("Order placed successfully"))
                    Log.d(TAG, "Order placed successfully")
                } else {
                    _orderResult.postValue(Result.failure(Exception("Response not successful: ${response.code()} ${response.message()}")))
                    Log.e(TAG, "Response not successful: ${response.code()} ${response.message()}")
                }
            } catch (e: HttpException) {
                _orderResult.postValue(Result.failure(e))
                Log.e(TAG, "HttpException: ${e.message}")
            } catch (e: Exception) {
                _orderResult.postValue(Result.failure(e))
                Log.e(TAG, "Exception: ${e.message}")
            } finally {
                val endTime = System.currentTimeMillis()
                Log.d(TAG, "addOrder execution time: ${endTime - startTime} ms")
            }
        }
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}