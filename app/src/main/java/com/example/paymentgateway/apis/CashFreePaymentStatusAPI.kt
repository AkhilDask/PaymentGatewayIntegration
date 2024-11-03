package com.example.paymentgateway.apis

import com.example.paymentgateway.interfaces.CashFreeApiInterface
import com.example.paymentgateway.interfaces.CashFreePaymentStatusAPIInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CashFreePaymentStatusAPI {

private  var retrofit: Retrofit? = null
fun getPaymetStatus(): CashFreePaymentStatusAPIInterface?{
    if(retrofit == null){
        retrofit = Retrofit.Builder()
            .baseUrl("your base url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    return  retrofit!!.create(CashFreePaymentStatusAPIInterface::class.java)
}
}