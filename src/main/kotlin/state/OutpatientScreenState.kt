package state

import androidx.compose.runtime.*

@Stable
class OutpatientScreenState {

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(EMPTY)
    private var searchBy by mutableStateOf(SEARCH)
    private var addText by mutableStateOf(SURNAME)

    fun updateIsSearch(value: Boolean) {
        isSearch = value
    }

    fun getIsSearch(): Boolean = isSearch

    fun updateSearchText(value: String) {
        searchText = value
    }

    fun searchText(): String = searchText

    fun updateSearchBy(value: String) {
        searchBy = value
    }

    fun searchBy(): String = searchBy

    fun updateAddText(value: String) {
        addText = value
    }

    fun addText(): String = addText

    companion object {
        private const val EMPTY = ""
        private const val SEARCH = "secondName"
        private const val SURNAME = " фамилии"
    }
}