package ca.tetervak.friendlyeats.repository

import androidx.lifecycle.LiveData
import ca.tetervak.friendlyeats.model.Restaurant

interface RestaurantRepository {
    fun getAll(): LiveData<List<Restaurant>>
    fun get(id: String): LiveData<Restaurant>
    fun loadRandomData()
}