package navcontroller

import androidx.compose.runtime.Composable
import screen.*
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
    clientInfoState: ClientInfoState,
    servicesState: ServicesState,
    drugsState: DrugsState,
    vaccineState: VaccineState
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
                illnessHistoryState,
                clientInfoState,
                outpatientScreenState
            )
        }

        composable(Screen.ClientsScreen.name) {
            buildClients(
                navController,
                clientsScreenState,
                clientInfoState,
                tabState,
                illnessHistoryState,
                petInfoState
            )
        }

        composable(Screen.ClientInfoScreen.name) {
            buildClientInfo(
                clientInfoState,
                tabState,
                navController,
                illnessHistoryState,
                petInfoState
            )
        }

        composable(Screen.ServicesScreen.name) {
            buildServices(
                servicesState
            )
        }

        composable(Screen.DrugsScreen.name) {
            buildDrugs(
                drugsState
            )
        }

        composable(Screen.VaccineScreen.name) {
            buildVaccine(
                vaccineState
            )
        }
    }.build()
}