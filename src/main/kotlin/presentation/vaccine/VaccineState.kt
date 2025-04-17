package presentation.vaccine

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.utils.Constant
import presentation.main.State
import java.text.SimpleDateFormat
import java.util.*

@Stable
class VaccineState: State {

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)
    private var searchBy by mutableStateOf(Constant.SEARCH)
    private var addText by mutableStateOf(Constant.SURNAME)

    private val dateString = "01.01.1999"
    private val formatDate = SimpleDateFormat("dd.MM.yyyy")
    private val formattedDate = formatDate.parse(dateString)
    private var dateResult by mutableStateOf(formattedDate)
    private var openDialog by mutableStateOf(false)

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