package state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import const.Constant

class ServicesState {

    private var addingNewService by mutableStateOf(false)
    private var editingService by mutableStateOf(false)

    private var name by mutableStateOf(Constant.EMPTY)
    private var price by mutableStateOf(Constant.EMPTY)
    private var id by mutableStateOf(0)

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