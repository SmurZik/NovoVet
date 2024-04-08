package state

import androidx.compose.runtime.*

@Stable
class IllnessHistoryState {

    private var ownerWords by mutableStateOf("")
    private var commonFeeling by mutableStateOf("")
    private var temperature by mutableStateOf("")
    private var appetite by mutableStateOf("")
    private var vomit by mutableStateOf("")
    private var defication by mutableStateOf("")
    private var urination by mutableStateOf("")
    private var extra by mutableStateOf("")
    private var diagnosis by mutableStateOf("")
    private var completed by mutableStateOf("")
    private var recommendations by mutableStateOf("")
    private var date by mutableStateOf("")
    private var isPattern by mutableStateOf(false)
    private var isEdit by mutableStateOf(false)

    fun updateOwnerWords(value: String) {
        ownerWords = value
    }

    fun ownerWords(): String = ownerWords

    fun updateCommonFeeling(value: String) {
        commonFeeling = value
    }

    fun commonFeeling(): String = commonFeeling

    fun updateTemperature(value: String) {
        temperature = value
    }

    fun temperature(): String = temperature

    fun updateAppetite(value: String) {
        appetite = value
    }

    fun appetite(): String = appetite

    fun updateVomit(value: String) {
        vomit = value
    }

    fun vomit(): String = vomit

    fun updateDefication(value: String) {
        defication = value
    }

    fun defication(): String = defication

    fun updateUrination(value: String) {
        urination = value
    }

    fun urination(): String = urination

    fun updateExtra(value: String) {
        extra = value
    }

    fun extra(): String = extra

    fun updateDiagnosis(value: String) {
        diagnosis = value
    }

    fun diagnosis(): String = diagnosis

    fun updateCompleted(value: String) {
        completed = value
    }

    fun completed(): String = completed

    fun updateRecommendations(value: String) {
        recommendations = value
    }

    fun recommendations(): String = recommendations

    fun updateDate(value: String) {
        date = value
    }

    fun date(): String = date

    fun updateIsPattern(value: Boolean) {
        isPattern = value
    }

    fun getIsPattern(): Boolean = isPattern

    fun updateIsEdit(value: Boolean) {
        isEdit = value
    }

    fun getIsEdit(): Boolean = isEdit
}