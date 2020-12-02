package ca.tetervak.friendlyeats.repository

import android.util.Log
import ca.tetervak.friendlyeats.domain.Rating
import ca.tetervak.friendlyeats.firestore.FirestoreRepository
import ca.tetervak.friendlyeats.model.RatingFirestore
import ca.tetervak.friendlyeats.model.RestaurantFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor() : RatingRepository {

    companion object {
        private const val TAG = "RatingRepositoryImpl"
        private const val LIMIT = 50
    }

    private val firestoreRatingRepository =
        FirestoreRepository(RatingFirestore::class.java)

    private val firestore = Firebase.firestore
    private val collection = firestore.collection("restaurants")

    @ExperimentalCoroutinesApi
    override fun getAll(restaurantId: String): Flow<List<Rating>> {
        val query = collection.document(restaurantId)
            .collection("ratings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(LIMIT.toLong())

         return firestoreRatingRepository.getAllFromQuery(query).map { list ->
             list.map { it.asRating() }
         }
    }

    // insert own rating for the restaurant
    override suspend fun insert(
            restaurantId: String,
            ratingValue: Double,
            comments: String
    ) {

        val user = Firebase.auth.currentUser
        if(user == null){
            Log.d(TAG, "insert: the user is not found")
            return
        }

        val restaurantRef = collection.document(restaurantId)
        val ratingRef = restaurantRef.collection("ratings").document()

        try {
            // get the restaurant object
            val restaurant: RestaurantFirestore? =
                    restaurantRef.get().await()?.toObject<RestaurantFirestore>()
            if (restaurant == null) {
                Log.d(TAG, "insert: the restaurant is not found")
                return
            }

            // compute new number of ratings
            val newNumRatings = restaurant.numRatings + 1

            // Compute new average rating
            val oldRatingTotal = restaurant.avgRating * restaurant.numRatings
            val newAvgRating = (oldRatingTotal + ratingValue) / newNumRatings

            // Set new restaurant info
            restaurant.numRatings = newNumRatings
            restaurant.avgRating = newAvgRating

            // Set new restaurant info

            val rating = RatingFirestore(
                    userId = user.uid,
                    userName =
                    if (user.displayName?.isEmpty() == true) {
                        user.email
                    } else {
                        user.displayName
                    },
                    rating = ratingValue,
                    text = comments
            )

            // the commented out 2 lines of code below are alright for
            // unrelated updates, but in this case the updates are related.
            // Therefore, the updates must be joined into
            // one transaction to maintain data consistency

            // restaurantRef.set(restaurant).await()
            // ratingRef.set(rating).await()

            firestore.runTransaction { transaction ->
                transaction.set(restaurantRef, restaurant)
                transaction.set(ratingRef, rating)
            }.await()

        } catch (e: Exception){
            Log.d(TAG, "insert: ${e.message}")
        }
    }
}

fun RatingFirestore.asRating(): Rating {
    return Rating(userId, userName, rating, text, timestamp, id)
}