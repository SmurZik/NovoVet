package navcontroller

import state.OutpatientScreenState
import androidx.compose.runtime.Composable
import screen.buildOutpatientCard
import screen.markup
import state.IllnessHistoryState
import state.PetInfoState

@Composable
fun customNavigationHost(
    navController: NavController,
    shareData: Pair<Int, String>,
    onDataChange: (Pair<Int, String>) -> Unit,
    onNicknameTabChange: (String) -> Unit,
    onTabsAdd: (String) -> Unit,
    onTabsSub: (String) -> Unit,
    onActiveTabChange: (String) -> Unit,
    outpatientScreenState: OutpatientScreenState,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState
) {
    NavigationHost(navController) {

        composable(Screen.JournalScreen.name) {
            markup(
                navController,
                onDataChange,
                onTabsAdd,
                onActiveTabChange,
                onNicknameTabChange,
                outpatientScreenState
            )
        }

        composable(Screen.OutpatientCardScreen.name) {
            buildOutpatientCard(
                navController,
                shareData,
                onActiveTabChange,
                onTabsSub,
                onTabsAdd,
                petInfoState,
                illnessHistoryState
            )
        }
    }.build()
}