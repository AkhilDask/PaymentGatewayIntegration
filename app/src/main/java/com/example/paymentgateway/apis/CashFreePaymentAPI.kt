package com.example.paymentgateway.apis

import com.example.paymentgateway.interfaces.CashFreeApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object CashFreePaymentAPI {

    private var retrofit:Retrofit? = null
    fun createPayment():CashFreeApiInterface?{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl("your base url")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return  retrofit!!.create(CashFreeApiInterface::class.java)
    }
}