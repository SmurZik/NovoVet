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
        state.updateIsPattern(false)
    }

    fun fillIllnessHistoryState(state: IllnessHistoryState, visit: List<String>) {
        state.updateOwnerWords(visit[0])
        state.updateCommonFeeling(visit[1])
        state.updateTemperature(visit[2])
        state.updateAppetite(visit[3])
        state.updateVomit(visit[4])
        state.updateDefication(visit[5])
        state.updateUrination(visit[6])
        state.updateExtra(visit[7])
        state.updateDiagnosis(visit[8])
        state.updateCompleted(visit[9])
        state.updateRecommendations(visit[10])
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