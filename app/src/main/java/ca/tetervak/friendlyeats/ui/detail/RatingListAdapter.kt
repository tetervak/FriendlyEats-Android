package ca.tetervak.friendlyeats.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.friendlyeats.databinding.RatingListItemBinding
import ca.tetervak.friendlyeats.domain.Rating
import java.text.SimpleDateFormat
import java.util.*

class RatingListAdapter(
): ListAdapter<Rating, RatingListAdapter.ViewHolder>(RatingDiffCallback())  {

    companion object{
        private val FORMAT = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RatingListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
            private val binding: RatingListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(rating: Rating) {

            binding.rating = rating
            binding.ratingItemRating.rating = rating.rating.toFloat()

            binding.executePendingBindings()
        }

    }

    class RatingDiffCallback : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem == newItem
        }
    }
}