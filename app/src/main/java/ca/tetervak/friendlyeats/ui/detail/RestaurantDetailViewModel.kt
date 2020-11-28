package ca.tetervak.friendlyeats.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import ca.tetervak.friendlyeats.repository.RestaurantRepository

class RestaurantDetailViewModel @ViewModelInject constructor(
    private val repository: RestaurantRepository
): ViewModel() {

}