package com.example.paymentgateway.models

data class DataToSend (
    val order_amount:Double = 0.0,
    val order_currency:String = "INR",
    val customer_details:UserData? = null
)