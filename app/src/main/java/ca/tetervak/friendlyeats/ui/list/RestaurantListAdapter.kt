package ca.tetervak.friendlyeats.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RestaurantListItemBinding
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.util.RestaurantUtil
import com.bumptech.glide.Glide

class RestaurantListAdapter(
        private val onClick: (Restaurant) -> Unit
): ListAdapter<Restaurant, RestaurantListAdapter.ViewHolder>(RestaurantDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RestaurantListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
            private val binding: RestaurantListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(restaurant: Restaurant) {
            val resources = binding.root.resources

            // Load image
            Glide.with(binding.restaurantItemImage.context)
                    .load(restaurant.photo)
                    .into(binding.restaurantItemImage)

            binding.restaurant = restaurant
            binding.restaurantItemRating.rating = restaurant.avgRating.toFloat()
            binding.restaurantItemNumRatings.text =
                    resources.getString(
                            R.string.fmt_num_ratings,
                            restaurant.numRatings)
            binding.restaurantItemPrice.text = RestaurantUtil.getPriceString(restaurant)
            binding.root.setOnClickListener { onClick(restaurant) }
            binding.executePendingBindings()
        }
    }

    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }
}