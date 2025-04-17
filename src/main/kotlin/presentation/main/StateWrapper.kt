package presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.utils.Constant
import presentation.client.ClientInfoViewModel
import presentation.client.ClientsViewModel
import presentation.drugs.DrugsViewModel
import presentation.outpatient.IllnessHistoryViewModel
import presentation.outpatient.OutpatientViewModel
import presentation.outpatient.PetInfoViewModel
import presentation.services.ServicesViewModel
import presentation.vaccine.VaccineState

class StateWrapper {

    @Composable
    fun initialOutpatientScreenState(): OutpatientViewModel {
        return remember {
            OutpatientViewModel()
        }
    }

    @Composable
    fun initialPetInfoState(): PetInfoViewModel {
        return remember {
            PetInfoViewModel()
        }
    }

    @Composable
    fun initialClientsScreenState(): ClientsViewModel {
        return remember {
            ClientsViewModel()
        }
    }

    @Composable
    fun initialClientInfoState(): ClientInfoViewModel {
        return remember {
            ClientInfoViewModel()
        }
    }

    @Composable
    fun initialIllnessHistoryState(): IllnessHistoryViewModel {
        return remember {
            IllnessHistoryViewModel()
        }
    }

    @Composable
    fun initialShareDataState(): ShareDataState {
        return remember {
            ShareDataState()
        }
    }

    @Composable
    fun initialTabState(): TabViewModel {
        return remember {
            TabViewModel()
        }
    }

    @Composable
    fun initialServiceState(): ServicesViewModel {
        return remember {
            ServicesViewModel()
        }
    }

    @Composable
    fun initialDrugState(): DrugsViewModel {
        return remember {
            DrugsViewModel()
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

    fun fillPetInfoState(petInfoViewModel: PetInfoViewModel, note: List<String>) {
        petInfoViewModel.updateNickname(note[0])
        petInfoViewModel.updateKind(note[1])
        petInfoViewModel.updateBreed(note[2])
        petInfoViewModel.updateMale(note[3])
        petInfoViewModel.updateAge(note[4])
        if (note.size > 5) {
            petInfoViewModel.updateVac(note[5])
            petInfoViewModel.updateVacDate(note[6])
        } else {
            petInfoViewModel.updateVac("")
            petInfoViewModel.updateVacDate("")
        }
    }

    fun clearIllnessHistoryState(state: IllnessHistoryViewModel) {
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
        state.updateChecked1(false)
        state.updateChecked2(false)
        state.updateChecked3(false)
        state.updateChecked4(false)
        state.updateFeelingNorm(false)
        state.updateFeelingHard(false)
        state.updateFeelingVeryHard(false)
        state.updateAppetiteSave(false)
        state.updateAppetiteLack(false)
        state.updateAppetiteRarely(false)
        state.updateVomitNo(false)
        state.updateVomitYesRarely(false)
        state.updateVomitYesOften(false)
        state.updateDeficationNorm(false)
        state.updateDeficationRarely(false)
        state.updateDeficationOften(false)
        state.updateDeficationLack(false)
        state.updateUrinationNorm(false)
        state.updateUrinationLack(false)
        state.updateUrinationOften(false)
    }

    fun fillIllnessHistoryState(state: IllnessHistoryViewModel, visit: List<String>) {
        state.updatePrice(visit[0].toInt())
        state.updateOwnerWords(visit[2])
        when (visit[3]) {
            "Удовлетворительное" -> {
                state.updateFeelingNorm(true)
                state.updateFeelingHard(false)
                state.updateFeelingVeryHard(false)
            }
            "Тяжелое" -> {
                state.updateFeelingHard(true)
                state.updateFeelingNorm(false)
                state.updateFeelingVeryHard(false)
            }
            "Крайне тяжелое" -> {
                state.updateFeelingHard(false)
                state.updateFeelingNorm(false)
                state.updateFeelingVeryHard(true)
            }
        }
        state.updateTemperature(visit[5])
        when (visit[6]) {
            "Сохранен" -> {
                state.updateAppetiteSave(true)
                state.updateAppetiteLack(false)
                state.updateAppetiteRarely(false)
            }
            "Отсутствует" -> {
                state.updateAppetiteLack(true)
                state.updateAppetiteSave(false)
                state.updateAppetiteRarely(false)
            }
            "Снижен" -> {
                state.updateAppetiteLack(false)
                state.updateAppetiteSave(false)
                state.updateAppetiteRarely(true)
            }
        }
        when (visit[7]) {
            "Нет" -> {
                state.updateVomitNo(true)
                state.updateVomitYesRarely(false)
                state.updateVomitYesOften(false)
            }
            "Да (редко)" -> {
                state.updateVomitYesRarely(true)
                state.updateVomitYesOften(false)
                state.updateVomitNo(false)
            }
            "Да (часто)" -> {
                state.updateVomitYesOften(true)
                state.updateVomitNo(false)
                state.updateVomitYesRarely(false)
            }
        }
        when (visit[8]) {
            "Нормальная" -> {
                state.updateDeficationNorm(true)
                state.updateDeficationRarely(false)
                state.updateDeficationOften(false)
                state.updateDeficationLack(false)
            }
            "Неоформленная (редко)" -> {
                state.updateDeficationRarely(true)
                state.updateDeficationNorm(false)
                state.updateDeficationOften(false)
                state.updateDeficationLack(false)
            }
            "Неоформленная (часто)" -> {
                state.updateDeficationOften(true)
                state.updateDeficationRarely(false)
                state.updateDeficationNorm(false)
                state.updateDeficationLack(false)
            }
            "Нет" -> {
                state.updateDeficationLack(true)
                state.updateDeficationOften(false)
                state.updateDeficationRarely(false)
                state.updateDeficationNorm(false)
            }
        }
        when (visit[9]) {
            "Нормальное" -> {
                state.updateUrinationNorm(true)
                state.updateUrinationLack(false)
                state.updateUrinationOften(false)
            }
            "Отсутствует" -> {
                state.updateUrinationLack(true)
                state.updateUrinationOften(false)
                state.updateUrinationNorm(false)
            }
            "Учащенное" -> {
                state.updateUrinationOften(true)
                state.updateUrinationNorm(false)
                state.updateUrinationLack(false)
            }
        }
        state.updateExtra(visit[10])
        state.updateDiagnosis(visit[11])
        state.updateCompleted(visit[12])
        state.updateRecommendations(visit[13])
        if (visit[1].contains("Мурзина")) {
            state.updateChecked1(true)
        } else {
            state.updateChecked1(false)
        }
        if (visit[1].contains("Кленкова")) {
            state.updateChecked2(true)
        } else {
            state.updateChecked2(false)
        }
        if (visit[1].contains("Камышенцева")) {
            state.updateChecked3(true)
        } else {
            state.updateChecked3(false)
        }
        if (visit[1].contains("Францкевич")) {
            state.updateChecked4(true)
        } else {
            state.updateChecked4(false)
        }
        state.updateWeight(visit[4])
        state.updateVisit(visit)

//        state.updateDate(data.DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
    }

    fun fillClientInfoState(state: ClientInfoViewModel, info: Pair<List<String>, List<String>>) {
        val currentInfo = info.first
        val additionalInfo = info.second
        state.updateClientId(currentInfo[0].toInt())
        state.updateFirstName(currentInfo[2])
        state.updateSecondName(currentInfo[1])
        state.updateLastName(currentInfo[3])
        state.updateAddress(currentInfo[4])
        state.updatePhoneNumber(currentInfo[5])
        state.updateEmail(currentInfo[6])

        state.updateNickname(additionalInfo[0])
        state.updateKind(additionalInfo[1])
        state.updateBreed(additionalInfo[2])
        state.updateDate(additionalInfo[3])
    }
}