package ca.tetervak.friendlyeats.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ca.tetervak.friendlyeats.model.Rating
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.repository.RatingRepository
import ca.tetervak.friendlyeats.repository.RestaurantRepository

class RestaurantDetailViewModel @ViewModelInject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val ratingRepository: RatingRepository
): ViewModel() {

    private val restaurantId = MutableLiveData<String>()

    val restaurant: LiveData<Restaurant> =
            restaurantId.switchMap{ restaurantRepository.get(it).asLiveData() }

    val ratings: LiveData<List<Rating>> =
            restaurantId.switchMap{ ratingRepository.getAll(it).asLiveData() }

    fun loadData(id: String){
        restaurantId.value = id
    }
}