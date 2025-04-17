package presentation.outpatient

import androidx.compose.runtime.*
import data.utils.Constant

@Stable
class IllnessHistoryViewModel {

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
            listOf<String>(), listOf(
                listOf(listOf<String>())
            )
        )
    )

    private var price by mutableStateOf(0)
    private var countLines by mutableStateOf(0)
    private var completedIds by mutableStateOf(listOf<Int>())

    private var amounts by mutableStateOf(listOf(Constant.EMPTY))

    private var measures by mutableStateOf(listOf(Constant.EMPTY))

    private var currentIndex by mutableStateOf(0)

    private var checked1 by mutableStateOf(false)
    private var checked2 by mutableStateOf(false)
    private var checked3 by mutableStateOf(false)
    private var checked4 by mutableStateOf(false)

    private var feelingNorm by mutableStateOf(false)
    private var feelingHard by mutableStateOf(false)
    private var feelingVeryHard by mutableStateOf(false)

    private var appetiteLack by mutableStateOf(false)
    private var appetiteSave by mutableStateOf(false)
    private var appetiteRarely by mutableStateOf(false)

    private var vomitNo by mutableStateOf(false)
    private var vomitYesRarely by mutableStateOf(false)
    private var vomitYesOften by mutableStateOf(false)

    private var deficationNorm by mutableStateOf(false)
    private var deficationRarely by mutableStateOf(false)
    private var deficationOften by mutableStateOf(false)
    private var deficationLack by mutableStateOf(false)

    private var urinationNorm by mutableStateOf(false)
    private var urinationLack by mutableStateOf(false)
    private var urinationOften by mutableStateOf(false)

    private var servicePrice by mutableStateOf(Constant.EMPTY)

    private var weight by mutableStateOf(Constant.EMPTY)
    private var writingPrice by mutableStateOf(false)
    private var rangeServicePrice by mutableStateOf(Constant.EMPTY)
    private var priceTemplate by mutableStateOf(Constant.EMPTY)

    private var priceList by mutableStateOf(mutableListOf<String>())

    private var measureComplex by mutableStateOf(mutableListOf<MutableList<String>>())

    private var info by mutableStateOf(Pair(Pair(listOf<String>(), listOf<String>()), Pair(0, 0)))

    private var loading by mutableStateOf(false)

    private var ownerDrugs by mutableStateOf(listOf<String>())

    fun updateOwnerDrugs(value: String) {
        ownerDrugs += value
    }

    fun clearOwnerDrugs(value: List<String>) {
        ownerDrugs = value
    }

    fun updateCurrentOwnerDrugs(value: String, index: Int) {
        val temp = ownerDrugs.toMutableList()
        temp[index] = value
        ownerDrugs = temp
    }

    fun ownerDrugs() = ownerDrugs

    fun updateLoading(value: Boolean) {
        loading = value
    }

    fun loading(): Boolean = loading

    fun updateInfo(value: Pair<Pair<List<String>, List<String>>, Pair<Int, Int>>) {
        info = value
    }

    fun info(): Pair<Pair<List<String>, List<String>>, Pair<Int, Int>> = info

    fun fillMeasureComplex(value: MutableList<MutableList<String>>) {
        measureComplex = value
    }

    fun measureComplex(): MutableList<MutableList<String>> = measureComplex

    fun addPrice(value: String) {
        priceList.add(value)
    }

    fun updatePriceList(value: MutableList<String>) {
        priceList = value
    }

    fun priceList(): List<String> = priceList

    fun updatePriceTemplate(value: String) {
        priceTemplate = value
    }

    fun priceTemplate(): String = priceTemplate

    fun updateRangeServicePrice(value: String) {
        rangeServicePrice = value
    }

    fun rangeServicePrice(): String = rangeServicePrice

    fun updateWritingPrice(value: Boolean) {
        writingPrice = value
    }

    fun writingPrice(): Boolean = writingPrice

    fun updateServicePrice(value: String) {
        servicePrice = value
    }

    fun servicePrice(): String = servicePrice

    fun updateWeight(value: String) {
        weight = value
    }

    fun weight(): String = weight

    fun updateUrinationNorm(value: Boolean) {
        urinationNorm = value
    }

    fun urinationNorm(): Boolean = urinationNorm

    fun updateUrinationLack(value: Boolean) {
        urinationLack = value
    }

    fun urinationLack(): Boolean = urinationLack

    fun updateUrinationOften(value: Boolean) {
        urinationOften = value
    }

    fun urinationOften(): Boolean = urinationOften

    fun updateDeficationNorm(value: Boolean) {
        deficationNorm = value
    }

    fun deficationNorm(): Boolean = deficationNorm

    fun updateDeficationRarely(value: Boolean) {
        deficationRarely = value
    }

    fun deficationRarely(): Boolean = deficationRarely

    fun updateDeficationOften(value: Boolean) {
        deficationOften = value
    }

    fun deficationOften(): Boolean = deficationOften

    fun updateDeficationLack(value: Boolean) {
        deficationLack = value
    }

    fun deficationLack(): Boolean = deficationLack

    fun updateVomitNo(value: Boolean) {
        vomitNo = value
    }

    fun vomitNo(): Boolean = vomitNo

    fun updateVomitYesRarely(value: Boolean) {
        vomitYesRarely = value
    }

    fun vomitYesRarely(): Boolean = vomitYesRarely

    fun updateVomitYesOften(value: Boolean) {
        vomitYesOften = value
    }

    fun vomitYesOften(): Boolean = vomitYesOften

    fun updateAppetiteLack(value: Boolean) {
        appetiteLack = value
    }

    fun appetiteLack(): Boolean = appetiteLack

    fun updateAppetiteSave(value: Boolean) {
        appetiteSave = value
    }

    fun appetiteSave(): Boolean = appetiteSave

    fun updateAppetiteRarely(value: Boolean) {
        appetiteRarely = value
    }

    fun appetiteRarely(): Boolean = appetiteRarely

    fun updateFeelingNorm(value: Boolean) {
        feelingNorm = value
    }

    fun feelingNorm(): Boolean = feelingNorm

    fun updateFeelingHard(value: Boolean) {
        feelingHard = value
    }

    fun feelingHard(): Boolean = feelingHard

    fun updateFeelingVeryHard(value: Boolean) {
        feelingVeryHard = value
    }

    fun feelingVeryHard(): Boolean = feelingVeryHard

    fun updateChecked1(value: Boolean) {
        checked1 = value
    }

    fun checked1(): Boolean = checked1

    fun updateChecked2(value: Boolean) {
        checked2 = value
    }

    fun checked2(): Boolean = checked2

    fun updateChecked3(value: Boolean) {
        checked3 = value
    }

    fun checked3(): Boolean = checked3

    fun updateChecked4(value: Boolean) {
        checked4 = value
    }

    fun checked4(): Boolean = checked4

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

    fun updateCompletedPair(value: Pair<List<String>, List<List<List<String>>>>) {
        completedPair = value
    }

    fun completedPair(): Pair<List<String>, List<List<List<String>>>> = completedPair

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

    fun updateMeasures(value: String) {
        measures += value
    }

    fun clearMeasures(value: List<String>) {
        measures = value
    }

    fun updateSelectedMeasure(value: String, index: Int) {
        val temp = measures.toMutableList()
        temp[index] = value
        measures = temp
    }

    fun measures(): List<String> = measures

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