package state

import androidx.compose.runtime.*
import const.Constant

@Stable
class OutpatientScreenState {

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)
    private var searchBy by mutableStateOf(Constant.SEARCH)
    private var addText by mutableStateOf(Constant.SURNAME)

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
}