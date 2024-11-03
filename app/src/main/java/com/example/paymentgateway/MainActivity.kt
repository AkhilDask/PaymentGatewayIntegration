package com.example.paymentgateway

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme
import com.example.paymentgateway.apis.CashFreePaymentAPI
import com.example.paymentgateway.apis.CashFreePaymentStatusAPI
import com.example.paymentgateway.models.DataToSend
import com.example.paymentgateway.models.PaymentData
import com.example.paymentgateway.models.PaymentStatus
import com.example.paymentgateway.models.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() , CFCheckoutResponseCallback{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
        }catch (e:CFException){
            print(e)

        }
        setContent {
            CardViewForDetails()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CardViewForDetails() {

         var textCustomerNameValue by remember { mutableStateOf("") }
        var textCurrencyTypeValue by remember { mutableStateOf("") }
         var textAmountValue by remember { mutableStateOf("") }
         var textEmailValue by remember { mutableStateOf("") }

        val context: Context = LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Enter Payment Details", fontSize = 20.sp)

                TextField(
                    value = textCustomerNameValue,
                    onValueChange = { textCustomerNameValue = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = textCurrencyTypeValue,
                    onValueChange = { textCurrencyTypeValue = it },
                    label = { Text("Currency Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = textAmountValue,
                    onValueChange = { textAmountValue = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = textEmailValue,
                    onValueChange = { textEmailValue = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            paymentProcess(context,textCustomerNameValue,textCurrencyTypeValue, textAmountValue,textEmailValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.5f)
                            .padding(
                                10.dp
                            )
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE), // Purple background
                            contentColor = Color.White // White text
                        ),
                        shape = MaterialTheme.shapes.medium,
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = "Pay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }



    override fun onPaymentVerify(orderID: String?) {
        getPaymentStatusOfApi(orderID!!)
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse?, orderID: String?) {
        println(cfErrorResponse?.message)
    }

    private fun getPaymentStatusOfApi(orderKeyValue:String){
        CashFreePaymentStatusAPI.getPaymetStatus()?.create(orderKeyValue)
            ?.enqueue(object : Callback<PaymentStatus> {
                override fun onResponse(
                    p0: Call<PaymentStatus>,
                    p1: Response<PaymentStatus>
                ) {
                    if (p1.body()?.order_status == "PAID") {
                        Toast.makeText(
                            this@MainActivity,
                            "Payment complete.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Payment in pending.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(p0: Call<PaymentStatus>, p1: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Payment Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }





        private fun paymentProcess(context: Context,textCustomerNameValue:String,textCurrencyTypeValue:String,textAmountValue:String,textEmailValue:String) {

            CashFreePaymentAPI.createPayment()?.createPayment(
                DataToSend(
                    order_amount = textAmountValue.toDouble(),
                    order_currency = textCurrencyTypeValue,
                    customer_details =  UserData(
                        customer_id  = "user id",
                        customer_name  = textCustomerNameValue,
                        customer_email = textEmailValue,
                        customer_phone = "valid mobile number"
                    )

                )
            )?.enqueue(object : Callback<PaymentData> {
                override fun onResponse(p0: Call<PaymentData>, p1: Response<PaymentData>) {


                    doPayment(p1.body()!!)
                }

                override fun onFailure(p0: Call<PaymentData>, p1: Throwable) {
                    println(p1.message)
                }

                fun doPayment(response: PaymentData) {

                    try {
                        val cfSession = CFSession.CFSessionBuilder()
                            .setEnvironment(CFSession.Environment.SANDBOX)
                            .setPaymentSessionID(response.paymentSessionId!!)
                            .setOrderId(response.orderId!!)
                            .build()
                        val cfTheme = CFWebCheckoutTheme.CFWebCheckoutThemeBuilder()
                            .setNavigationBarBackgroundColor("#fc2678")
                            .setNavigationBarTextColor("#ffffff")
                            .build()
                        val cfWebCheckoutPayment =
                            CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder()
                                .setSession(cfSession)
                                .setCFWebCheckoutUITheme(cfTheme)
                                .build()
                        CFPaymentGatewayService.getInstance()
                            .doPayment(context, cfWebCheckoutPayment)
                    } catch (exception: CFException) {
                        exception.printStackTrace()
                    }

                }


            })

        }
        }











