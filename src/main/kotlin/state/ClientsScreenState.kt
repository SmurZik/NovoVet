package state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import const.Constant

class ClientsScreenState: State {

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)
    private var searchBy by mutableStateOf(Constant.SEARCH)
    private var addText by mutableStateOf(Constant.SURNAME)

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