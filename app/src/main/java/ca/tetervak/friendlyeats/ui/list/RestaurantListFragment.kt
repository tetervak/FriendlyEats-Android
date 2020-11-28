package ca.tetervak.friendlyeats.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RestaurantListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment(){

    lateinit var firestore: FirebaseFirestore

    private lateinit var binding: RestaurantListFragmentBinding
    lateinit var navController: NavController
    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RestaurantListFragmentBinding.inflate(inflater, container, false)

        navController = findNavController()

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerRestaurants.addItemDecoration(divider)

        val adapter = RestaurantListAdapter(
            onClick = {
                val action =
                        RestaurantListFragmentDirections.actionRestaurantListToDetail(it.id!!)
                navController.navigate(action)
            }
        )

        viewModel.getAll().observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }

        binding.recyclerRestaurants.adapter = adapter

        return binding.root
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
                viewModel.loadRandomData()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
