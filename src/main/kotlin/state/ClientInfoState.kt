package state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import const.Constant

class ClientInfoState {

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
    private var date by mutableStateOf(Constant.EMPTY)

    private var addInfo by mutableStateOf(listOf<String>())

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

    fun updateDate(value: String) {
        date = value
    }

    fun date(): String = date
}