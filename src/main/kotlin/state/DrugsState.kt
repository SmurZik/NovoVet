package state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import const.Constant

class DrugsState {

    private var addingNewDrug by mutableStateOf(false)
    private var editingDrug by mutableStateOf(false)

    private var name by mutableStateOf(Constant.EMPTY)
    private var price by mutableStateOf(Constant.EMPTY)
    private var measure by mutableStateOf(Constant.EMPTY)
    private var id by mutableStateOf(0)

    fun updateAddingNewDrug(value: Boolean) {
        addingNewDrug = value
    }

    fun addingNewDrug(): Boolean = addingNewDrug

    fun updateEditingDrug(value: Boolean) {
        editingDrug = value
    }

    fun editingDrug(): Boolean = editingDrug

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

    fun updateMeasure(value: String) {
        measure = value
    }

    fun measure(): String = measure
}