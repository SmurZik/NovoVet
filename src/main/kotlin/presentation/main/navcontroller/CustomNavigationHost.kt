package presentation.main.navcontroller

import androidx.compose.runtime.Composable
import presentation.appointment.AppointmentViewModel
import presentation.appointment.buildAppointment
import presentation.client.ClientInfoViewModel
import presentation.client.ClientsViewModel
import presentation.client.buildClientInfo
import presentation.client.buildClients
import presentation.drugs.DrugsViewModel
import presentation.drugs.buildDrugs
import presentation.outpatient.*
import presentation.services.ServicesViewModel
import presentation.services.buildServices
import presentation.vaccine.VaccineState
import presentation.vaccine.buildVaccine
import presentation.main.*

@Composable
fun customNavigationHost(
    navController: NavController,
    shareDataState: ShareDataState,
    tabViewModel: TabViewModel,
    outpatientViewModel: OutpatientViewModel,
    petInfoViewModel: PetInfoViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    clientsViewModel: ClientsViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    servicesViewModel: ServicesViewModel,
    drugsViewModel: DrugsViewModel,
    vaccineState: VaccineState,
    appointmentViewModel: AppointmentViewModel
) {
    NavigationHost(navController) {

        composable(Screen.JournalScreen.name) {
            markup(
                navController,
                shareDataState,
                tabViewModel,
                outpatientViewModel,
                illnessHistoryViewModel,
                petInfoViewModel,
                clientsViewModel,
                clientInfoViewModel
            )
        }

        composable(Screen.OutpatientCardScreen.name) {
            buildOutpatientCard(
                navController,
                shareDataState,
                tabViewModel,
                petInfoViewModel,
                illnessHistoryViewModel,
                clientInfoViewModel,
                outpatientViewModel
            )
        }

        composable(Screen.ClientsScreen.name) {
            buildClients(
                navController,
                clientsViewModel,
                clientInfoViewModel,
                tabViewModel,
                illnessHistoryViewModel,
                petInfoViewModel
            )
        }

        composable(Screen.ClientInfoScreen.name) {
            buildClientInfo(
                clientInfoViewModel,
                tabViewModel,
                navController,
                illnessHistoryViewModel,
                petInfoViewModel
            )
        }

        composable(Screen.ServicesScreen.name) {
            buildServices(
                servicesViewModel
            )
        }

        composable(Screen.DrugsScreen.name) {
            buildDrugs(
                drugsViewModel
            )
        }

        composable(Screen.VaccineScreen.name) {
            buildVaccine(
                vaccineState
            )
        }

        composable(Screen.AppointmentScreen.name) {
            buildAppointment(
                appointmentViewModel
            )
        }
    }.build()
}