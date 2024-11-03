package com.example.paymentgateway.interfaces

import com.example.paymentgateway.models.DataToSend
import com.example.paymentgateway.models.PaymentData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CashFreeApiInterface {

    @POST("pg/orders")
    @Headers(
        "Content-Type: application/json",
        "X-Client-Id: ***your client id***",
        "X-Client-Secret: ***your client secret key***",
        "x-api-version: 2023-08-01",
        "Accept: application/json"
    )
    fun createPayment(
        @Body data:DataToSend
    ): Call<PaymentData>
}