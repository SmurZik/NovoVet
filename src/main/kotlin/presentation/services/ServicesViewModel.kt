package presentation.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.utils.Constant
import presentation.main.State

class ServicesViewModel: State {

    private var addingNewService by mutableStateOf(false)
    private var editingService by mutableStateOf(false)

    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)

    private var name by mutableStateOf(Constant.EMPTY)
    private var price by mutableStateOf(Constant.EMPTY)
    private var id by mutableStateOf(0)

    override fun updateIsSearch(value: Boolean) {
        isSearch = value
    }

    override fun getIsSearch(): Boolean = isSearch

    override fun updateSearchText(value: String) {
        searchText = value
    }

    override fun searchText(): String = searchText
    override fun updateSearchBy(value: String) {

    }

    override fun searchBy(): String {
        return ""
    }

    override fun updateAddText(value: String) {

    }

    override fun addText(): String {
        return "названию"
    }

    fun updateAddingNewService(value: Boolean) {
        addingNewService = value
    }

    fun addingNewService(): Boolean = addingNewService

    fun updateEditingService(value: Boolean) {
        editingService = value
    }

    fun editingService(): Boolean = editingService

    fun updateName(value: String) {
        name = value
    }

    fun name(): String = name

    fun updatePrice(value: String) {
        price = value
    }

    fun price(): String = price

    fun updateId(value: Int) {
        id = value
    }

    fun id(): Int = id
}