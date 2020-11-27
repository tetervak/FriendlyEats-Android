package ca.tetervak.friendlyeats.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RestaurantDetailFragmentBinding
import ca.tetervak.friendlyeats.model.Restaurant
import ca.tetervak.friendlyeats.ui.list.RestaurantListAdapter
import ca.tetervak.friendlyeats.util.RestaurantUtil
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment(),
    EventListener<DocumentSnapshot> {

    companion object{
        private const val LIMIT = 50
        private const val TAG = "RestaurantDetailFragment"
    }

    private lateinit var binding: RestaurantDetailFragmentBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var restaurantRef: DocumentReference
    private lateinit var ratingAdapter: RatingListAdapter

    private var restaurantRegistration: ListenerRegistration? = null

    private val viewModel: RestaurantDetailViewModel by viewModels()
    private val safeArgs: RestaurantDetailFragmentArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RestaurantDetailFragmentBinding.inflate(inflater, container, false)

        navController = findNavController()

        val restaurantId: String = safeArgs.restaurantId

        // Initialize Firestore
        firestore = Firebase.firestore

        // Get reference to the restaurant
        restaurantRef = firestore.collection("restaurants").document(restaurantId)

        // Get ratings
        val ratingsQuery = restaurantRef
            .collection("ratings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(LIMIT.toLong())

        // RecyclerView
        ratingAdapter = object: RatingListAdapter(ratingsQuery) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.recyclerRatings.visibility = View.GONE
                    binding.viewEmptyRatings.visibility = View.VISIBLE
                } else {
                    binding.recyclerRatings.visibility = View.VISIBLE
                    binding.viewEmptyRatings.visibility = View.GONE
                }
            }
        }

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerRatings.addItemDecoration(divider)
        binding.recyclerRatings.adapter = ratingAdapter

        binding.fabShowRatingDialog.setOnClickListener {
            navController.navigate(
                RestaurantDetailFragmentDirections.actionDetailToRatingDialog())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        ratingAdapter.startListening()
        restaurantRegistration = restaurantRef.addSnapshotListener(this)
    }

    override fun onStop() {
        super.onStop()

        ratingAdapter.stopListening()

        restaurantRegistration?.remove()
        restaurantRegistration = null
    }

    /**
     * Listener for the Restaurant document ([.restaurantRef]).
     */
    override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e)
            return
        }

        snapshot?.let {
            val restaurant = snapshot.toObject<Restaurant>()
            if (restaurant != null) {
                onRestaurantLoaded(restaurant)
            }
        }
    }

    private fun onRestaurantLoaded(restaurant: Restaurant) {
        binding.restaurantName.text = restaurant.name
        binding.restaurantRating.rating = restaurant.avgRating.toFloat()
        binding.restaurantNumRatings.text = getString(R.string.fmt_num_ratings, restaurant.numRatings)
        binding.restaurantCity.text = restaurant.city
        binding.restaurantCategory.text = restaurant.category
        binding.restaurantPrice.text = RestaurantUtil.getPriceString(restaurant)

        // Background image
        Glide.with(binding.restaurantImage.context)
            .load(restaurant.photo)
            .into(binding.restaurantImage)
    }

    private fun notImplemented() {
        Snackbar.make(
            binding.root,
            getString(R.string.not_implemented),
            Snackbar.LENGTH_LONG
        ).show()
    }

}