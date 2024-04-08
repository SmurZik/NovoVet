package state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

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
    fun initialIllnessHistoryState(): IllnessHistoryState {
        return remember {
            IllnessHistoryState()
        }
    }
}