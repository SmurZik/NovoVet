package state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import const.Constant

class ClientInfoState: State {


    private var isSearch by mutableStateOf(false)
    private var searchText by mutableStateOf(Constant.EMPTY)
    private var searchBy by mutableStateOf(Constant.SEARCH)
    private var addText by mutableStateOf(Constant.SURNAME)

    private var secondName by mutableStateOf(Constant.EMPTY)
    private var firstName by mutableStateOf(Constant.EMPTY)
    private var lastName by mutableStateOf(Constant.EMPTY)
    private var phoneNumber by mutableStateOf(Constant.EMPTY)
    private var address by mutableStateOf(Constant.EMPTY)
    private var save by mutableStateOf(false)
    private var clientId by mutableStateOf(0)
    private var countLines by mutableStateOf(0)

    private var nickname by mutableStateOf(Constant.EMPTY)
    private var kind by mutableStateOf(Constant.EMPTY)
    private var breed by mutableStateOf(Constant.EMPTY)
    private var male by mutableStateOf(Constant.EMPTY)
    private var age by mutableStateOf(Constant.EMPTY)
    private var date by mutableStateOf(Constant.EMPTY)

    private var addInfo by mutableStateOf(listOf<String>())

    private var addingNewPet by mutableStateOf(false)

    private var petIds by mutableStateOf(listOf<Int>())

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

    fun updateSecondName(value: String) {
        secondName = value
    }

    fun secondName(): String = secondName

    fun updateFirstName(value: String) {
        firstName = value
    }

    fun firstName(): String = firstName

    fun updateLastName(value: String) {
        lastName = value
    }

    fun lastName(): String = lastName

    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }

    fun phoneNumber(): String = phoneNumber

    fun updateAddress(value: String) {
        address = value
    }

    fun address(): String = address

    fun updateSave(value: Boolean) {
        save = value
    }

    fun save(): Boolean = save

    fun updateClientId(value: Int) {
        clientId = value
    }

    fun clientId(): Int = clientId

    fun updateCountLines(value: Int) {
        countLines = value
    }

    fun countLines(): Int = countLines

    fun updateNickname(value: String) {
        nickname = value
    }

    fun nickname(): String = nickname

    fun updateAddInfo(value: List<String>) {
        addInfo = value
    }

    fun addInfo(): List<String> = addInfo

    fun updateKind(value: String) {
        kind = value
    }

    fun kind(): String = kind

    fun updateBreed(value: String) {
        breed = value
    }

    fun breed(): String = breed

    fun updateMale(value: String) {
        male = value
    }

    fun male(): String = male

    fun updateAge(value: String) {
        age = value
    }

    fun age(): String = age

    fun updateDate(value: String) {
        date = value
    }

    fun date(): String = date

    fun updateAddingNewPet(value: Boolean) {
        addingNewPet = value
    }

    fun addingNewPet(): Boolean = addingNewPet

    fun updatePetIds(value: List<Int>) {
        petIds = value
    }

    fun petIds(): List<Int> = petIds
}