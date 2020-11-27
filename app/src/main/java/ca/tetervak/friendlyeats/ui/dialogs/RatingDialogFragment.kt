package ca.tetervak.friendlyeats.ui.dialogs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ca.tetervak.friendlyeats.R
import ca.tetervak.friendlyeats.databinding.RatingDialogFragmentBinding
import ca.tetervak.friendlyeats.model.Rating
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RatingDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "RatingDialog"
    }

    private lateinit var binding: RatingDialogFragmentBinding

    private var ratingListener: RatingListener? = null


    internal interface RatingListener {
        fun onRating(rating: Rating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RatingDialogFragmentBinding.inflate(inflater, container, false)

        binding.restaurantFormButton.setOnClickListener { onSubmitClicked() }
        binding.restaurantFormCancel.setOnClickListener { onCancelClicked() }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is RatingListener) {
            ratingListener = context
        }
    }

    override fun onResume() {
        super.onResume()
//        dialog?.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun onSubmitClicked() {
        val user = Firebase.auth.currentUser
        user?.let {
            val rating = Rating(
                it,
                binding.restaurantFormRating.rating.toDouble(),
                binding.restaurantFormText.text.toString())

            ratingListener?.onRating(rating)
        }

        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }
}
