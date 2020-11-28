package ca.tetervak.friendlyeats.repository

import androidx.lifecycle.LiveData
import ca.tetervak.friendlyeats.model.Restaurant

interface RestaurantRepository {
    fun getAll(): LiveData<List<Restaurant>>
    fun loadRandomData()
}