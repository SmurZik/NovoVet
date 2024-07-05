package navcontroller

import androidx.compose.runtime.Composable
import screen.buildClientInfo
import screen.buildClients
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
    illnessHistoryState: IllnessHistoryState,
    clientsScreenState: ClientsScreenState,
    clientInfoState: ClientInfoState
) {
    NavigationHost(navController) {

        composable(Screen.JournalScreen.name) {
            markup(
                navController,
                shareDataState,
                tabState,
                outpatientScreenState,
                illnessHistoryState,
                petInfoState,
                clientsScreenState,
                clientInfoState
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

        composable(Screen.ClientsScreen.name) {
            buildClients(
                navController,
                clientsScreenState,
                tabState
            )
        }

        composable(Screen.ClientInfoScreen.name) {
            buildClientInfo(clientInfoState)
        }
    }.build()
}