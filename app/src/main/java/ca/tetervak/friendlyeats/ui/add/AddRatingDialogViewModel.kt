package ca.tetervak.friendlyeats.ui.add

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.repository.RatingRepository
import ca.tetervak.friendlyeats.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRatingDialogViewModel @ViewModelInject constructor(
        private val restaurantRepository: RestaurantRepository,
        private val ratingRepository: RatingRepository
): ViewModel() {

    private val restaurantId = MutableLiveData<String>()
    val restaurant: LiveData<Restaurant> =
            restaurantId.switchMap { restaurantRepository.get(it) }

    fun loadData(id: String){
        restaurantId.value = id
    }

    fun addRating(ratingValue: Double, comments: String){
        viewModelScope.launch(Dispatchers.IO){
            restaurantId.value?.let{
                ratingRepository.insert(it, ratingValue, comments)
            }
        }
    }

}