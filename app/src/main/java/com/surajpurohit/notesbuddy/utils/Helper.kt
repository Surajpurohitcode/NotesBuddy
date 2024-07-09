package com.surajpurohit.notesbuddy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Helper {
    companion object {
        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("EEEE, d'${getDayOfMonthSuffix(date.day)}' 'of' MMMM", Locale.US)
            return sdf.format(date)
        }

        fun getDayOfMonthSuffix(n: Int): String {
            if (n in 11..13) {
                return "th"
            }
            return when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }
    }
}