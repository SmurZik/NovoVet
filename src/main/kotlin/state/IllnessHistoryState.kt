package state

import androidx.compose.runtime.*
import const.Constant

@Stable
class IllnessHistoryState {

    private var ownerWords by mutableStateOf(Constant.EMPTY)
    private var commonFeeling by mutableStateOf(Constant.EMPTY)
    private var temperature by mutableStateOf(Constant.EMPTY)
    private var appetite by mutableStateOf(Constant.EMPTY)
    private var vomit by mutableStateOf(Constant.EMPTY)
    private var defication by mutableStateOf(Constant.EMPTY)
    private var urination by mutableStateOf(Constant.EMPTY)
    private var extra by mutableStateOf(Constant.EMPTY)
    private var diagnosis by mutableStateOf(Constant.EMPTY)
    private var completed by mutableStateOf(Constant.EMPTY)
    private var recommendations by mutableStateOf(Constant.EMPTY)
    private var date by mutableStateOf(Constant.EMPTY)
    private var isPattern by mutableStateOf(true)
    private var next by mutableStateOf(false)
    private var visit by mutableStateOf(listOf(Constant.EMPTY))
    private var note by mutableStateOf(listOf(Constant.EMPTY))
    private var id by mutableStateOf(0)
    private var isNew by mutableStateOf(false)
    private var isNewVisitInfo by mutableStateOf(false)
    private var visitId by mutableStateOf(0)

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

    fun updateNext(value: Boolean) {
        next = value
    }

    fun next(): Boolean = next

    fun updateVisit(value: List<String>) {
        visit = value
    }

    fun visit(): List<String> = visit

    fun updateNote(value: List<String>) {
        note = value
    }

    fun note(): List<String> = note

    fun updateId(value: Int) {
        id = value
    }

    fun id(): Int = id

    fun updateIsNew(value: Boolean) {
        isNew = value
    }

    fun getIsNew(): Boolean = isNew

    fun updateVisitId(value: Int) {
        visitId = value
    }

    fun visitId(): Int = visitId

    fun updateIsNewVisitInfo(value: Boolean) {
        isNewVisitInfo = value
    }

    fun getIsNewVisitInfo(): Boolean = isNewVisitInfo
}