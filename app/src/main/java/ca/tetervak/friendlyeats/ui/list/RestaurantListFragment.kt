package ca.tetervak.friendlyeats.ui.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RestaurantListFragmentBinding
import ca.tetervak.friendlyeats.util.RatingUtil
import ca.tetervak.friendlyeats.util.RestaurantUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment(), RestaurantListAdapter.OnRestaurantSelectedListener {

    companion object{
        private const val LIMIT = 50
        private const val TAG = "RestaurantListFragment"
    }

    lateinit var firestore: FirebaseFirestore
    lateinit var query: Query

    private lateinit var binding: RestaurantListFragmentBinding
    lateinit var adapter: RestaurantListAdapter
    lateinit var navController: NavController

    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Firestore
        firestore = Firebase.firestore

        // Get ${LIMIT} restaurants
        query = firestore.collection("restaurants")
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT.toLong())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RestaurantListFragmentBinding.inflate(inflater, container, false)

        navController = findNavController()

        // RecyclerView
        adapter = object : RestaurantListAdapter(query, this@RestaurantListFragment) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerRestaurants.visibility = View.GONE
                    binding.viewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerRestaurants.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerRestaurants.addItemDecoration(divider)
        binding.recyclerRestaurants.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Start listening for Firestore updates
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_find_restaurant -> {
                notImplemented()
                true
            }
            R.id.action_load_random_data -> {
                loadRandomData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRestaurantSelected(restaurant: DocumentSnapshot) {
        val action =
            RestaurantListFragmentDirections.actionRestaurantListToDetail(restaurant.id)
        navController.navigate(action)
    }

    private fun loadRandomData() {
        // Add a bunch of random restaurants
        val batch = firestore.batch()
        for (i in 0..9) {
            val restRef = firestore.collection("restaurants").document()

            // Create random restaurant / ratings
            val randomRestaurant = RestaurantUtil.getRandom(requireContext())
            val randomRatings = RatingUtil.getRandomList(randomRestaurant.numRatings)
            randomRestaurant.avgRating = RatingUtil.getAverageRating(randomRatings)

            // Add restaurant
            batch.set(restRef, randomRestaurant)

            // Add ratings to subcollection
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

    private fun notImplemented() {
        Snackbar.make(
            binding.root,
            getString(R.string.not_implemented),
            Snackbar.LENGTH_LONG
        ).show()
    }
}
