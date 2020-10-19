package com.classygo.app.model

import java.util.*

//MARK: items for all trips
data class Trip(
    var id: String? = "",
    val route: TripLocation? = null,
    val author: String? = "",
    val image: String? = "",
    val eta: String? = "",
    val startDateAndTime: Date? = null,
    val endDateAndTime: Date? = null
)

//MARK: holding the location information on a trip
data class TripLocation(
    var startLocationName: String? = "",
    val startLocationAddress: String? = "",
    val startLocationLat: Double? = 0.00,
    val startLocationLon: Double? = 0.00,
    var endLocationName: String? = "",
    val endLocationAddress: String? = "",
    val endLocationLat: Double? = 0.00,
    val endLocationLon: Double? = 0.00
)