package ca.tetervak.friendlyeats.repository

import androidx.lifecycle.LiveData
import ca.tetervak.friendlyeats.model.Rating

interface RatingRepository {
    fun getAll(restaurantId: String): LiveData<List<Rating>>
    suspend fun insert(
        restaurantId: String,
        ratingValue: Double,
        comments: String
    )
}