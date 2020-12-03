package ca.tetervak.friendlyeats.domain

import java.util.*

data class Rating(
    var userId: String? = null,
    var userName: String? = null,
    var score: Double = 0.toDouble(),
    var comments: String? = null,
    var date: Date? = null,
    var id: String? = null
)