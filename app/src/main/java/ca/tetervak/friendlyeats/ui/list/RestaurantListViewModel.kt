package ca.tetervak.friendlyeats.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import ca.tetervak.friendlyeats.repository.RestaurantDataRepository

class RestaurantListViewModel @ViewModelInject constructor(
    private val repository: RestaurantDataRepository
): ViewModel() {

}