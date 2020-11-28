package ca.tetervak.friendlyeats.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.friendlyeats.databinding.RatingListItemBinding
import ca.tetervak.friendlyeats.firestore.FirestoreRecyclerViewAdapter
import ca.tetervak.friendlyeats.model.Rating
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * RecyclerView adapter for a list of [Rating].
 */
open class RatingListAdapterOld(query: Query) : FirestoreRecyclerViewAdapter<RatingListAdapterOld.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RatingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject<Rating>())
    }

    class ViewHolder(val binding: RatingListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rating: Rating?) {
            if (rating == null) {
                return
            }

            binding.ratingItemName.text = rating.userName
            binding.ratingItemRating.rating = rating.rating.toFloat()
            binding.ratingItemText.text = rating.text

            if (rating.timestamp != null) {
                binding.ratingItemDate.text = FORMAT.format(rating.timestamp)
            }
        }

        companion object {

            private val FORMAT = SimpleDateFormat(
                    "MM/dd/yyyy", Locale.US)
        }
    }
}
