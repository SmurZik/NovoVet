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

    private var isCompleted by mutableStateOf(false)

    private var addingNewService by mutableStateOf(false)
    private var editingService by mutableStateOf(false)

    private var service by mutableStateOf(Constant.EMPTY)
    private var drugs by mutableStateOf(listOf(Constant.EMPTY))
    private var drug by mutableStateOf(Constant.EMPTY)
    private var amount by mutableStateOf(Constant.EMPTY)

    private var searchingService by mutableStateOf(false)
    private var searchingDrug by mutableStateOf(false)
    private var servicesList by mutableStateOf(listOf<String>())
    private var drugsList by mutableStateOf(listOf<String>())
    private var drugCount by mutableStateOf(1)
    private var completedId by mutableStateOf(0)
    private var completedPair by mutableStateOf(
        Pair(
            listOf<String>(), Pair(
                listOf(listOf<String>()), listOf(
                    listOf<String>()
                )
            )
        )
    )

    private var price by mutableStateOf(0)
    private var countLines by mutableStateOf(0)
    private var completedIds by mutableStateOf(listOf<Int>())

    private var amounts by mutableStateOf(listOf(Constant.EMPTY))

    private var currentIndex by mutableStateOf(0)

    fun updatePrice(value: Int) {
        price = value
    }

    fun price(): Int = price

    fun updateCountLines(value: Int) {
        countLines = value
    }

    fun countLines(): Int = countLines

    fun updateCompletedIds(value: List<Int>) {
        completedIds = value
    }

    fun completedIds(): List<Int> = completedIds

    fun updateCompletedPair(value: Pair<List<String>, Pair<List<List<String>>, List<List<String>>>>) {
        completedPair = value
    }

    fun completedPair(): Pair<List<String>, Pair<List<List<String>>, List<List<String>>>> = completedPair

    fun updateCompletedId(value: Int) {
        completedId = value
    }

    fun completedId(): Int = completedId

    fun updateEditingService(value: Boolean) {
        editingService = value
    }

    fun editingService(): Boolean = editingService

    fun updateAmounts(value: String) {
        amounts += value
    }

    fun clearAmounts(value: List<String>) {
        amounts = value
    }

    fun updateSelectedAmount(value: String, index: Int) {
        val temp = amounts.toMutableList()
        temp[index] = value
        amounts = temp
    }

    fun amounts(): List<String> = amounts

    fun updateCurrentIndex(value: Int) {
        currentIndex = value
    }

    fun currentIndex(): Int = currentIndex

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

    fun updateIsCompleted(value: Boolean) {
        isCompleted = value
    }

    fun getIsCompleted(): Boolean = isCompleted

    fun updateAddingNewService(value: Boolean) {
        addingNewService = value
    }

    fun addingNewService(): Boolean = addingNewService

    fun updateService(value: String) {
        service = value
    }

    fun service(): String = service

    fun updateDrugs(value: String) {
        drugs += value
    }

    fun clearDrugs(value: List<String>) {
        drugs = value
    }

    fun updateSelectedDrug(value: String, index: Int) {
        val temp = drugs.toMutableList()
        temp[index] = value
        drugs = temp
    }

    fun updateDrug(value: String) {
        drug = value
    }

    fun drug(): String = drug

    fun drugs(): List<String> = drugs

    fun updateAmount(value: String) {
        amount = value
    }

    fun amount(): String = amount

    fun updateSearchingService(value: Boolean) {
        searchingService = value
    }

    fun searchingService(): Boolean = searchingService

    fun updateSearchingDrug(value: Boolean) {
        searchingDrug = value
    }

    fun searchingDrug(): Boolean = searchingDrug

    fun updateServicesList(value: List<String>) {
        servicesList = value
    }

    fun servicesList(): List<String> = servicesList

    fun updateDrugCount(value: Int) {
        drugCount = value
    }

    fun drugCount(): Int = drugCount

    fun updateDrugsList(value: List<String>) {
        drugsList = value
    }

    fun drugsList(): List<String> = drugsList
}