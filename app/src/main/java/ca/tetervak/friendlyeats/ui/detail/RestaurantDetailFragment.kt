package ca.tetervak.friendlyeats.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RestaurantDetailFragmentBinding
import ca.tetervak.friendlyeats.util.RestaurantUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private val viewModel: RestaurantDetailViewModel by viewModels()
    private val safeArgs: RestaurantDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RestaurantDetailFragmentBinding.inflate(inflater, container, false)

        val navController = findNavController()

        viewModel.loadData(safeArgs.restaurantId)

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerRatings.addItemDecoration(divider)

        val adapter = RatingListAdapter()
        binding.recyclerRatings.adapter = adapter
        viewModel.ratings.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.restaurant.observe(viewLifecycleOwner){ restaurant ->
            binding.restaurant = restaurant
            binding.restaurantRating.rating = restaurant.avgRating.toFloat()
            binding.restaurantNumRatings.text =
                    getString(R.string.fmt_num_ratings, restaurant.numRatings)
            binding.restaurantPrice.text = RestaurantUtil.getPriceString(restaurant)
        }

        binding.fabShowRatingDialog.setOnClickListener {
            navController.navigate(
                RestaurantDetailFragmentDirections.actionDetailToRatingDialog())
        }

        return binding.root
    }
}