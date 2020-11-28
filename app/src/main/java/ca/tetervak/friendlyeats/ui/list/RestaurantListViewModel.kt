package ca.tetervak.friendlyeats.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.repository.RestaurantRepository

class RestaurantListViewModel @ViewModelInject constructor(
    private val repository: RestaurantRepository
): ViewModel() {

    fun getAll(): LiveData<List<Restaurant>>{
        return repository.getAll()
    }

    fun loadRandomData() {
        repository.loadRandomData()
    }
}