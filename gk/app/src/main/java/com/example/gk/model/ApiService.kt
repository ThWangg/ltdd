package com.example.gk.model

import com.example.gk.model.ApiResponse
import com.example.gk.model.OrderListResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("get_orders.php")
    suspend fun getAllOrders(): Response<OrderListResponse>

    @FormUrlEncoded
    @POST("add_order.php")
    suspend fun addOrder(
        @Field("customer_name") customerName: String,
        @Field("phone_number") phoneNumber: String,
        @Field("total_price") totalPrice: Double,
        @Field("status") status: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("update_order.php")
    suspend fun updateOrder(
        @Field("id") id: Int,
        @Field("customer_name") customerName: String,
        @Field("phone_number") phoneNumber: String,
        @Field("total_price") totalPrice: Double,
        @Field("status") status: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("delete_order.php")
    suspend fun deleteOrder(
        @Field("id") id: Int
    ): Response<ApiResponse>
}
