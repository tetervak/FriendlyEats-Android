package ca.tetervak.friendlyeats.repository

import androidx.lifecycle.LiveData
import ca.tetervak.friendlyeats.model.Rating
import kotlinx.coroutines.flow.Flow

interface RatingRepository {
    fun getAll(restaurantId: String): Flow<List<Rating>>
    suspend fun insert(
        restaurantId: String,
        ratingValue: Double,
        comments: String
    )
}