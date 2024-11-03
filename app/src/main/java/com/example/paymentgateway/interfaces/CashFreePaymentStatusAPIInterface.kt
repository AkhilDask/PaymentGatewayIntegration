package com.example.paymentgateway.interfaces

import com.example.paymentgateway.models.PaymentStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface CashFreePaymentStatusAPIInterface {



        @GET("/pg/orders/{order_id}")
        @Headers(
            "accept: application/json",
            "X-Client-Id: ***your client id***",
            "X-Client-Secret: ***your client secret key***",
            "x-api-version: 2022-01-01",

        )
        fun create(
            @Path("order_id") orderId: String
        ): Call<PaymentStatus>

}