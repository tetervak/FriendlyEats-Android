package ca.tetervak.friendlyeats.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ca.tetervak.friendlyeats.util.formatDate
import java.util.*

@BindingAdapter("date")
fun bindDate(textView: TextView, date: Date?) {
    if (date is Date)
        textView.text = formatDate(date)
}