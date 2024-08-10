package state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import const.Constant

class StateWrapper {

    @Composable
    fun initialOutpatientScreenState(): OutpatientScreenState {
        return remember {
            OutpatientScreenState()
        }
    }

    @Composable
    fun initialPetInfoState(): PetInfoState {
        return remember {
            PetInfoState()
        }
    }

    @Composable
    fun initialClientsScreenState(): ClientsScreenState {
        return remember {
            ClientsScreenState()
        }
    }

    @Composable
    fun initialClientInfoState(): ClientInfoState {
        return remember {
            ClientInfoState()
        }
    }

    @Composable
    fun initialIllnessHistoryState(): IllnessHistoryState {
        return remember {
            IllnessHistoryState()
        }
    }

    @Composable
    fun initialShareDataState(): ShareDataState {
        return remember {
            ShareDataState()
        }
    }

    @Composable
    fun initialTabState(): TabState {
        return remember {
            TabState()
        }
    }

    @Composable
    fun initialServiceState(): ServicesState {
        return remember {
            ServicesState()
        }
    }

    @Composable
    fun initialDrugState(): DrugsState {
        return remember {
            DrugsState()
        }
    }

    @Composable
    fun initialVaccineState(): VaccineState {
        return remember {
            VaccineState()
        }
    }

//    fun clearPetInfoState(state: PetInfoState) {
//        state.updateAge(Constant.EMPTY)
//        state.updateNickname(Constant.EMPTY)
//        state.updateKind(Constant.EMPTY)
//        state.updateMale(Constant.EMPTY)
//        state.updateBreed(Constant.EMPTY)
//    }

    fun fillPetInfoState(petInfoState: PetInfoState, note: List<String>) {
        petInfoState.updateNickname(note[0])
        petInfoState.updateKind(note[1])
        petInfoState.updateBreed(note[2])
        petInfoState.updateMale(note[3])
        petInfoState.updateAge(note[4])
        if (note.size > 5) {
            petInfoState.updateVac(note[5])
            petInfoState.updateVacDate(note[6])
        } else {
            petInfoState.updateVac("")
            petInfoState.updateVacDate("")
        }
    }

    fun clearIllnessHistoryState(state: IllnessHistoryState) {
        state.updateOwnerWords(Constant.EMPTY)
        state.updateCommonFeeling(Constant.EMPTY)
        state.updateTemperature(Constant.EMPTY)
        state.updateAppetite(Constant.EMPTY)
        state.updateVomit(Constant.EMPTY)
        state.updateDefication(Constant.EMPTY)
        state.updateUrination(Constant.EMPTY)
        state.updateExtra(Constant.EMPTY)
        state.updateDiagnosis(Constant.EMPTY)
        state.updateCompleted(Constant.EMPTY)
        state.updateRecommendations(Constant.EMPTY)
        state.updateWeight(Constant.EMPTY)
        state.updateIsPattern(false)
    }

    fun fillIllnessHistoryState(state: IllnessHistoryState, visit: List<String>) {
        state.updatePrice(visit[0].toInt())
        state.updateOwnerWords(visit[2])
        when (visit[3]) {
            "Удовлетворительное" -> {
                state.updateFeelingNorm(true)
            }
            "Тяжелое" -> {
                state.updateFeelingHard(true)
            }
            "Крайне тяжелое" -> {
                state.updateFeelingVeryHard(true)
            }
        }
        state.updateTemperature(visit[5])
        when (visit[6]) {
            "Сохранен" -> {
                state.updateAppetiteSave(true)
            }
            "Отсутствует" -> {
                state.updateAppetiteLack(true)
            }
        }
        when (visit[7]) {
            "Нет" -> {
                state.updateVomitNo(true)
            }
            "Да (редко)" -> {
                state.updateVomitYesRarely(true)
            }
            "Да (часто)" -> {
                state.updateVomitYesOften(true)
            }
        }
        when (visit[8]) {
            "Нормальная" -> {
                state.updateDeficationNorm(true)
            }
            "Неоформленная (редко)" -> {
                state.updateDeficationRarely(true)
            }
            "Неоформленная (часто)" -> {
                state.updateDeficationOften(true)
            }
        }
        when (visit[9]) {
            "Нормальное" -> {
                state.updateUrinationNorm(true)
            }
            "Отсутствует" -> {
                state.updateUrinationLack(true)
            }
            "Учащенное" -> {
                state.updateUrinationOften(true)
            }
        }
        state.updateExtra(visit[10])
        state.updateDiagnosis(visit[11])
        state.updateCompleted(visit[12])
        state.updateRecommendations(visit[13])
        if (visit[1].contains("Мурзина")) state.updateChecked1(true)
        if (visit[1].contains("Кленкова")) state.updateChecked2(true)
        if (visit[1].contains("Камышенцева")) state.updateChecked3(true)
        if (visit[1].contains("Францкевич")) state.updateChecked4(true)
        state.updateWeight(visit[4])
        state.updateVisit(visit)

//        state.updateDate(DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
    }

    fun fillClientInfoState(state: ClientInfoState, info: Pair<List<String>, List<String>>) {
        val currentInfo = info.first
        val additionalInfo = info.second
        state.updateClientId(currentInfo[0].toInt())
        state.updateFirstName(currentInfo[2])
        state.updateSecondName(currentInfo[1])
        state.updateLastName(currentInfo[3])
        state.updateAddress(currentInfo[4])
        state.updatePhoneNumber(currentInfo[5])

        state.updateNickname(additionalInfo[0])
        state.updateKind(additionalInfo[1])
        state.updateBreed(additionalInfo[2])
        state.updateDate(additionalInfo[3])
    }
}