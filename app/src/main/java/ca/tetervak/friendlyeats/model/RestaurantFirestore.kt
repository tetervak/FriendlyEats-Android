package ca.tetervak.friendlyeats.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Restaurant POJO.
 */
@IgnoreExtraProperties
data class RestaurantFirestore(
    var name: String? = null,
    var city: String? = null,
    var category: String? = null,
    var photo: String? = null,
    var price: Int = 0,
    var numRatings: Int = 0,
    var avgRating: Double = 0.toDouble(),
    @DocumentId var id: String? = null
)
