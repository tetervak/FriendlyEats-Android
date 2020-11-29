package ca.tetervak.friendlyeats.util

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private val formatterDate =
    DateTimeFormatter.ofPattern("MMM dd, yyyy")

fun formatDate(date: Date?): String? {
    return date?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()
        ?.format(formatterDate)
}