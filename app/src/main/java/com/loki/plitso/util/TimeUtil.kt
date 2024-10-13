package com.loki.plitso.util

import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

object TimeUtil {

    fun formatDateTime(input: String): String {
        var formattedTime: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm,dd-MM-yyyy")

            try {
                val dateTime = LocalDateTime.parse(input, formatter)
                val today = LocalDate.now()
                val inputDate = dateTime.toLocalDate()

                formattedTime = when {
                    inputDate.isEqual(today) -> "Today, ${dateTime.toLocalTime()}"
                    inputDate.isEqual(today.minusDays(1)) -> "Yesterday, ${dateTime.toLocalTime()}"
                    else -> dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
                }
            } catch (e: DateTimeParseException) {
                Timber.tag("Format time err").d("Invalid date format")
            }
        }
        return formattedTime ?: input
    }

    fun currentTime(): String {
        val currentDate = System.currentTimeMillis()
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(currentDate))
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun currentDate(): String {
        val currentDate = System.currentTimeMillis()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date(currentDate))
    }
}