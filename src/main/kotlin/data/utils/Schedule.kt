package data.utils

import java.util.Calendar

interface Schedule {

    fun makeSchedule(time: Long, calendar: Calendar): List<String>

    class Base : Schedule {

        override fun makeSchedule(time: Long, calendar: Calendar): List<String> {
            val result = mutableListOf("Время")
            calendar.timeInMillis = time
            calendar.set(Calendar.HOUR_OF_DAY, 10)
            calendar.set(Calendar.MINUTE, 0)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            val count = if (weekDay == 1 || weekDay == 7) 27 else 39
            for (i in 0..count) {
                val minute = calendar.get(Calendar.MINUTE)
                result.add(
                    calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + if (minute == 0) "00" else calendar.get(
                        Calendar.MINUTE
                    )
                )
                calendar.timeInMillis += 900_000L
            }
            return result
        }
    }
}