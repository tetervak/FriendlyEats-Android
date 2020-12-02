package ca.tetervak.friendlyeats.model

import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.DocumentId
import java.util.Date

/**
 * Model POJO for a rating.
 */
data class RatingFirestore(
    var userId: String? = null,
    var userName: String? = null,
    var rating: Double = 0.toDouble(),
    var text: String? = null,
    @ServerTimestamp var timestamp: Date? = null,
    @DocumentId var id: String? = null
)
