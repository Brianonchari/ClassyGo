package com.classygo.app.model

data class PaymentMethodItem(
    var id: String? = "",
    val name: String? = null,
    val type: String? = "",
    val pan: String? = "",
    val expiry: String? = "",
    val cvv: String? = null
)
