package presentation.appointment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.utils.CorrectDate
import data.Repository
import data.utils.Schedule
import java.util.*

class AppointmentViewModel(
    private val dateFormatter: CorrectDate,
    private val calendar: Calendar,
    private val schedule: Schedule,
    private val repository: Repository
) {

    private var client by mutableStateOf("")

    private var enableAdder by mutableStateOf(false)

    private var phoneNumber by mutableStateOf("")

    private var time by mutableStateOf("")

    private var goal by mutableStateOf("")

    private val oneDayInMillis = 24 * 60 * 60 * 1000

    private var date by mutableStateOf("")

    private var dateInMillis by mutableStateOf(0L)

    private var initDate by mutableStateOf(true)

    private var isSearching by mutableStateOf(false)

    private var searchText by mutableStateOf("")

    private var scheduleList by mutableStateOf(listOf<String>())

    private var fullSchedule by mutableStateOf(listOf<String>())

    private var emptyAppointment by mutableStateOf(true)

    private val colorMap = mutableMapOf<Int, Int>()

    private var showToast by mutableStateOf(false)

    private var row by mutableStateOf(0)

    fun updateRow(value: Int) {
        row = value
    }

    fun row() = row

    fun updateShowToast(value: Boolean) {
        showToast = value
    }

    fun isShowToast() = showToast

    fun updateEmptyAppointment(client: String, phoneNumber: String, goal: String) {
        if (client.isEmpty()) emptyAppointment = true
        this.client = client
        this.phoneNumber = phoneNumber
        this.goal = goal
    }

    fun updateTime(row: Int) {
        val hours = fullSchedule[row * 5]
        val day = date.split(", ")[1]
        time = "$hours $day"
    }

    fun time() = time

    fun updateEnableAdder(value: Boolean) {
        enableAdder = value
    }

    fun isEnabledAdder() = enableAdder

    fun updateClient(value: String) {
        client = value
    }

    fun client() = client

    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }

    fun phoneNumber() = phoneNumber

    fun updateGoal(value: String) {
        goal = value
    }

    fun goal() = goal

    fun fullSchedule() = fullSchedule

    fun updateScheduleList(value: List<String>) {
        scheduleList = value
    }

    fun scheduleList() = scheduleList

    fun isSearch() = isSearching

    fun updateIsSearching(value: Boolean) {
        isSearching = value
    }

    fun searchText() = searchText

    fun updateSearchText(value: String) {
        searchText = value
    }

    fun initDate() = initDate

    fun updateInitDate(value: Boolean) {
        initDate = value
    }

    fun date() = date

    fun updateDate(newDate: Long) {
        date = dateFormatter.toCorrectDate(calendar, newDate)
        dateInMillis = newDate
        initDate = false
        updateScheduleList(makeSchedule(dateInMillis))
    }

    fun dateInMillis() = dateInMillis

    fun nextDay(timeMillis: Long) {
        date = dateFormatter.toCorrectDate(calendar, timeMillis + oneDayInMillis)
        dateInMillis = timeMillis + oneDayInMillis
        updateScheduleList(makeSchedule(dateInMillis))
    }

    fun previousDay(timeMillis: Long) {
        date = dateFormatter.toCorrectDate(calendar, timeMillis - oneDayInMillis)
        dateInMillis = timeMillis - oneDayInMillis
        updateScheduleList(makeSchedule(dateInMillis))
    }

    fun makeSchedule(time: Long): List<String> = schedule.makeSchedule(time, calendar)

    fun init() {
        fullSchedule = repository.getSchedule(dateInMillis, scheduleList)
        initColor()
    }

    fun colorMap() = colorMap

    fun initColor() {
        colorMap.clear()
        colorMap[1] = 1
        for (row in 2..<scheduleList().size) {
            if (fullSchedule()[row * 5 + 1].isEmpty() && fullSchedule()[row * 5 + 4] == "true") {
                if (colorMap[row - 1]!! == 1) colorMap[row] = 1 else colorMap[row] = 2
            } else {
                if (colorMap[row - 1]!! == 1) colorMap[row] = 2 else colorMap[row] = 1
            }
        }
    }
}
