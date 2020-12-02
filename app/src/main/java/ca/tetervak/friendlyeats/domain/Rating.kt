package ca.tetervak.friendlyeats.domain

import java.util.*

data class Rating(
    var userId: String? = null,
    var userName: String? = null,
    var rating: Double = 0.toDouble(),
    var text: String? = null,
    var timestamp: Date? = null,
    var id: String? = null
)