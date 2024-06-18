package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.request.UpdateOrderRequest
import com.entsh118.housespot.data.api.response.OrderByVendorResponse
import com.entsh118.housespot.data.api.response.OrderDetailsResponse
import com.entsh118.housespot.data.api.response.UpdateOrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {
    @GET("view_by_vendor/{id_vendor}")
    fun getOrdersByVendor(
        @Path("id_vendor") idVendor: String,
        @Query("status") status: String? = null
    ): Call<OrderByVendorResponse>

    @GET("view/{id_order}")
    fun getOrderDetailsById(
        @Path("id_order") idOrder: String
    ): Call<OrderDetailsResponse>

    @PUT("update/{id_order}")
    fun updateOrderStatus(
        @Path("id_order") idOrder: String,
        @Body updateOrderRequest: UpdateOrderRequest
    ): Call<UpdateOrderResponse>

}
