package ca.tetervak.friendlyeats.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import ca.tetervak.friendlyeats.repository.RestaurantDataRepository

class RestaurantDetailViewModel @ViewModelInject constructor(
    private val repository: RestaurantDataRepository
): ViewModel() {

}