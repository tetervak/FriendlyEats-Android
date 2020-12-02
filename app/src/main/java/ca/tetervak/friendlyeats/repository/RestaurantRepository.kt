package ca.tetervak.friendlyeats.repository

import ca.tetervak.friendlyeats.domain.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getAll(): Flow<List<Restaurant>>
    fun get(id: String): Flow<Restaurant>
    fun loadRandomData()
}