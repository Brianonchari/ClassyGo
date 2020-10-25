package com.classygo.app.model

import java.util.*

//MARK: items for all notifications
data class NotificationItem(
    var id: String? = "",
    val title: String? = null,
    val message: String? = "",
    val date: Date? = null
)
