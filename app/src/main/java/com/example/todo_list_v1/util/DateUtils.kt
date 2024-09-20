package com.example.todo_list_v1.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    // Converts milliseconds to formatted date string
    fun convertMillisToDate(millis: Long?): String {
        return if (millis != null) {
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            formatter.format(Date(millis))
        } else {
            "Never"
        }
    }

    // Converts a date string to milliseconds
    fun convertDateToMillis(date: String): Long? {
        return if (date == "Never") {
            null
        } else {
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            formatter.parse(date)?.time ?: System.currentTimeMillis()
        }
    }

    // Adds the appropriate repeat interval to a calendar object
    fun calculateNextOccurrence(
        selectedRepeatOption: String?,
        repeatInterval: Int?,
        repeatEndsAt: Long?,
        repeatOnDays: List<Int>?,
        currentDueDate: Long?,

    ): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentDueDate ?: System.currentTimeMillis()
        val safeRepeatInterval = repeatInterval ?: 1
        return when (selectedRepeatOption?.lowercase()) {
            "daily" -> calculateNextDailyOccurrence(calendar, safeRepeatInterval)
            "weekly" -> calculateNextWeeklyOccurrence(calendar, safeRepeatInterval, repeatOnDays)
            "monthly" -> calculateNextMonthlyOccurrence(calendar, safeRepeatInterval)
            "yearly" -> calculateNextYearlyOccurrence(calendar, safeRepeatInterval)
            else -> calendar.timeInMillis
        }
    }

    // Repeat calculation functions...
    private fun calculateNextDailyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
        calendar.add(Calendar.DAY_OF_YEAR, repeatInterval)
        return calendar.timeInMillis
    }

    private fun calculateNextWeeklyOccurrence(calendar: Calendar, repeatInterval: Int, repeatOnDays: List<Int>?): Long {
        if (repeatOnDays.isNullOrEmpty()) {
            calendar.add(Calendar.WEEK_OF_YEAR, repeatInterval)
        } else {
            val currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val sortedDays = repeatOnDays.sorted()
            val nextDay = sortedDays.firstOrNull { it > currentDay } ?: sortedDays.first()
            val daysToAdd = if (nextDay > currentDay) nextDay - currentDay else (7 - currentDay) + nextDay
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
        }
        return calendar.timeInMillis
    }

    private fun calculateNextMonthlyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
        calendar.add(Calendar.MONTH, repeatInterval)
        return calendar.timeInMillis
    }

    private fun calculateNextYearlyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
        calendar.add(Calendar.YEAR, repeatInterval)
        return calendar.timeInMillis
    }
}