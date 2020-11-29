package ca.tetervak.friendlyeats.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ca.tetervak.friendlyeats.databinding.AddRatingDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRatingDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddRatingDialog"
    }

    private val viewModel: AddRatingDialogViewModel by viewModels()
    private val safeArgs: AddRatingDialogFragmentArgs by navArgs()

    private lateinit var binding: AddRatingDialogFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = AddRatingDialogFragmentBinding.inflate(inflater, container, false)

        viewModel.loadData(safeArgs.restaurantId)
        viewModel.restaurant.observe(viewLifecycleOwner) {
            binding.name.text = it.name
        }

        binding.restaurantFormButton.setOnClickListener {
            viewModel.addRating(
                    ratingValue = binding.restaurantFormRating.rating.toDouble(),
                    comments = binding.restaurantFormText.text.toString()
            )
            dismiss()
        }
        binding.restaurantFormCancel.setOnClickListener { dismiss() }

        return binding.root
    }
}
