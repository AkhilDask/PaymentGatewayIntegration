package com.example.paymentgateway.models

data class PaymentStatus(
    val cf_order_id: String? = null,
    val entity: String? = null,
    val order_amount: Double? = null,
    val order_status: String? = null
)