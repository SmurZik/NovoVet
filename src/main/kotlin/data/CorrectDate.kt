package data

import java.util.Calendar

interface CorrectDate {

    fun toCorrectDate(calendar: Calendar, newDate: Long): String

    class Base : CorrectDate {

        override fun toCorrectDate(calendar: Calendar, newDate: Long): String {

            calendar.timeInMillis = newDate
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            val dayOfWeek = when (weekDay) {
                2 -> "Понедельник"
                3 -> "Вторник"
                4 -> "Среда"
                5 -> "Четверг"
                6 -> "Пятница"
                7 -> "Суббота"
                else -> "Воскресенье"
            }

            val correctDay = if (day < 10) "0$day" else day.toString()

            val correctMonth = if (month < 10) "0$month" else month.toString()

            return "$dayOfWeek, $correctDay.$correctMonth.$year"
        }
    }
}