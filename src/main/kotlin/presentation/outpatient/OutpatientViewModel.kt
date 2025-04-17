package presentation.outpatient

import androidx.compose.runtime.*
import data.utils.Constant
import presentation.main.State
import java.text.SimpleDateFormat
import java.util.*

@Stable
class OutpatientViewModel: State {

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)
    private var searchBy by mutableStateOf(Constant.SEARCH)
    private var addText by mutableStateOf(Constant.SURNAME)

    private val dateString = "01.01.1999"
    private val formatDate = SimpleDateFormat("dd.MM.yyyy")
    private val formattedDate = formatDate.parse(dateString)
    private var dateResult by mutableStateOf(formattedDate)
    private var openDialog by mutableStateOf(false)

    private var expanded by mutableStateOf(false)
    private var confirmedPrice by mutableStateOf(false)
    private var containPrice by mutableStateOf(false)

    private var manualVaccineAdder by mutableStateOf(false)

    private var vaccine by mutableStateOf(Constant.EMPTY)
    private var vaccineDate by mutableStateOf(Constant.EMPTY)

    fun updateVaccine(value: String) {
        vaccine = value
    }

    fun vaccine(): String = vaccine

    fun updateVaccineDate(value: String) {
        vaccineDate = value
    }

    fun vaccineDate(): String = vaccineDate

    fun updateManualVaccineAdder(value: Boolean) {
        manualVaccineAdder = value
    }

    fun manualVaccineAdder() = manualVaccineAdder

    fun updateContainPrice(value: Boolean) {
        containPrice = value
    }

    fun containPrice(): Boolean = containPrice

    fun updateConfirmedPrice(value: Boolean) {
        confirmedPrice = value
    }

    fun confirmedPrice(): Boolean = confirmedPrice

    fun updateExpanded(value: Boolean) {
        expanded = value
    }

    fun expanded(): Boolean = expanded

    fun updateDateResult(value: Date) {
        dateResult = value
    }

    fun dateResult(): Date = dateResult

    fun updateOpenDialog(value: Boolean) {
        openDialog = value
    }

    fun openDialog(): Boolean = openDialog

    override fun updateIsSearch(value: Boolean) {
        isSearch = value
    }

    override fun getIsSearch(): Boolean = isSearch

    override fun updateSearchText(value: String) {
        searchText = value
    }

    override fun searchText(): String = searchText

    override fun updateSearchBy(value: String) {
        searchBy = value
    }

    override fun searchBy(): String = searchBy

    override fun updateAddText(value: String) {
        addText = value
    }

    override fun addText(): String = addText
}