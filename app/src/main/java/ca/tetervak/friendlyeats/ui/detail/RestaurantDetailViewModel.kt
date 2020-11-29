package ca.tetervak.friendlyeats.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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
            Transformations.switchMap(restaurantId){
                restaurantRepository.get(it)
            }

    val ratings: LiveData<List<Rating>> =
            Transformations.switchMap(restaurantId){
                ratingRepository.getAll(it)
            }

    fun loadData(id: String){
        restaurantId.value = id
    }
}