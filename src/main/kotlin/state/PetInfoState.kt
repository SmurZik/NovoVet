package state

import androidx.compose.runtime.*
import const.Constant

@Stable
class PetInfoState {

    private var nickname by mutableStateOf(Constant.EMPTY)
    private var kind by mutableStateOf(Constant.EMPTY)
    private var breed by mutableStateOf(Constant.EMPTY)
    private var male by mutableStateOf(Constant.EMPTY)
    private var age by mutableStateOf(Constant.EMPTY)
    private var save by mutableStateOf(false)

    fun updateNickname(value: String) {
        nickname = value
    }

    fun nickname(): String = nickname

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

    fun updateSave(value: Boolean) {
        save = value
    }

    fun save(): Boolean = save
}