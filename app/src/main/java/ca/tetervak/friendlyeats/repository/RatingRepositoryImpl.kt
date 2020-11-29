package ca.tetervak.friendlyeats.repository

import androidx.lifecycle.LiveData
import ca.tetervak.friendlyeats.firestore.FirestoreCollectionLiveData
import ca.tetervak.friendlyeats.model.Rating
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(): RatingRepository {

    companion object {
        private const val LIMIT = 50
    }

    private val firestore = Firebase.firestore
    private val collection = firestore.collection("restaurants")

    override fun getAll(restaurantId: String): LiveData<List<Rating>> {

        val query = collection.document(restaurantId)
                .collection("ratings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LIMIT.toLong())

        return FirestoreCollectionLiveData(query, Rating::class.java)
    }
}