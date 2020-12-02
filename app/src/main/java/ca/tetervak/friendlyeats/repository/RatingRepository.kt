package ca.tetervak.friendlyeats.repository

import ca.tetervak.friendlyeats.domain.Rating
import kotlinx.coroutines.flow.Flow

interface RatingRepository {
    fun getAll(restaurantId: String): Flow<List<Rating>>
    suspend fun insert(
        restaurantId: String,
        ratingValue: Double,
        comments: String
    )
}