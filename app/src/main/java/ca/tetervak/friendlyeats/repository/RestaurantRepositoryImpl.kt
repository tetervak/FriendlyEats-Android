package ca.tetervak.friendlyeats.repository

import android.app.Application
import android.util.Log
import ca.tetervak.friendlyeats.firestore.FirestoreRepository
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.util.RatingUtil
import ca.tetervak.friendlyeats.util.RestaurantUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
        private val application: Application
) : RestaurantRepository {

    companion object {
        private const val TAG = "RestaurantRepositoryImpl"
        private const val LIMIT = 50
    }

    private val firestoreRepository =
        FirestoreRepository(Restaurant::class.java)

    private val firestore = Firebase.firestore
    private val collection = firestore.collection("restaurants")
//    private val query = collection.orderBy("avgRating", Query.Direction.DESCENDING)
//            .limit(LIMIT.toLong())

    @ExperimentalCoroutinesApi
    override fun getAll(): Flow<List<Restaurant>> {
        return firestoreRepository.getAllFromCollection(collection)
    }

    @ExperimentalCoroutinesApi
    override fun get(id: String): Flow<Restaurant> {
        return firestoreRepository.getDocument(collection.document(id))
    }

    override fun loadRandomData() {
        // Add a bunch of random restaurants
        val batch = firestore.batch()
        for (i in 0..9) {
            val restRef = firestore.collection("restaurants").document()

            // Create random restaurant / ratings
            val randomRestaurant = RestaurantUtil.getRandom(application)
            val randomRatings = RatingUtil.getRandomList(randomRestaurant.numRatings)
            randomRestaurant.avgRating = RatingUtil.getAverageRating(randomRatings)

            // Add restaurant
            batch.set(restRef, randomRestaurant)

            // Add ratings to sub-collection
            for (rating in randomRatings) {
                batch.set(restRef.collection("ratings").document(), rating)
            }
        }

        batch.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Write batch succeeded.")
            } else {
                Log.w(TAG, "write batch failed.", task.exception)
            }
        }
    }
}