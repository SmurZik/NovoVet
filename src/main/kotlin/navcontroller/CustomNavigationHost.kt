package navcontroller

import androidx.compose.runtime.Composable
import screen.buildOutpatientCard
import screen.markup
import state.*

@Composable
fun customNavigationHost(
    navController: NavController,
    shareDataState: ShareDataState,
    tabState: TabState,
    outpatientScreenState: OutpatientScreenState,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState
) {
    NavigationHost(navController) {

        composable(Screen.JournalScreen.name) {
            markup(
                navController,
                shareDataState,
                tabState,
                outpatientScreenState,
                illnessHistoryState,
                petInfoState
            )
        }

        composable(Screen.OutpatientCardScreen.name) {
            buildOutpatientCard(
                navController,
                shareDataState,
                tabState,
                petInfoState,
                illnessHistoryState
            )
        }
    }.build()
}