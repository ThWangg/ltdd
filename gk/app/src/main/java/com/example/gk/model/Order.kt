package com.example.gk.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: Int,

    @SerializedName("customer_name")
    val customerName: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("total_price")
    val totalPrice: Double,

    @SerializedName("status")
    val status: String
)
