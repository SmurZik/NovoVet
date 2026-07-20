package presentation.client

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import presentation.components.search
import data.Repository
import presentation.main.navcontroller.NavController
import presentation.main.navcontroller.Screen
import presentation.outpatient.IllnessHistoryViewModel
import presentation.outpatient.PetInfoViewModel
import presentation.main.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun buildClients(
    navController: NavController,
    clientsViewModel: ClientsViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    tabViewModel: TabViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel
) {
//    LazyRow(
//        modifier = Modifier
////            .padding(start = 60.dp)
//            .background(color = Color.Cyan)
//            .fillMaxWidth(1f)
//    ) {
//        item {
//            Text(
//                text = "Амбулаторные приемы",
//                fontSize = 18.sp,
//                color = Color.Black,
//                fontStyle = FontStyle.Italic,
//                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
//            )
//        }
//    }
    val textButton = listOf("Добавить клиента", "Найти")
    val stateHorizontal = rememberScrollState(0)
//    val scope = CoroutineScope(Dispatchers.Default)
    LazyRow(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxWidth(1f)
            .height(110.dp)
    ) {
        items(2) {
            Button(
                colors = ButtonDefaults.buttonColors(Color(0, 191, 255)),
                onClick = {
                    if (it == 1) {
                        clientsViewModel.updateIsSearch(!clientsViewModel.getIsSearch())
                        clientsViewModel.updateSearchText("")
                        clientsViewModel.updateSearchBy("secondName")
                        clientsViewModel.updateAddText(" фамилии")
                    } else {
                        clientsViewModel.updateAddingNewClient(true)
                        clientsViewModel.updateEmail("")
                        clientsViewModel.updateSecondName("")
                        clientsViewModel.updateFirstName("")
                        clientsViewModel.updateLastName("")
                        clientsViewModel.updateAddress("")
                        clientsViewModel.updatePhoneNumber("")
                        clientsViewModel.updateNickname("")
                        clientsViewModel.updateKind("")
                        clientsViewModel.updateBreed("")
                        clientsViewModel.updateMale("")
                        clientsViewModel.updateAge("")
                    }
                },
                modifier = Modifier
                    .padding(top = 30.dp, end = 8.dp)
            ) {
                val icon = if (it == 0) Icons.Filled.Add else Icons.Filled.Search
                Icon(
                    icon,
                    contentDescription = ""
                )
                Text(textButton[it], modifier = Modifier.padding(8.dp))
            }
        }
    }

    val info = Repository.getClientsInfo(clientsViewModel.searchText(), clientsViewModel.searchBy())
    val currentNote = info.first.second
    val clientIds = info.first.first
    val countLines = info.second

    var expanded by remember { mutableStateOf(false) }
    if (clientsViewModel.getIsSearch()) {
        search(clientsViewModel, expanded, onExpandedChange = { expanded = it })
    }

    if (clientsViewModel.addingNewClient()) {
        newClientAdder(clientsViewModel, tabViewModel, navController, illnessHistoryViewModel, petInfoViewModel, clientInfoViewModel)
    }
    Card(
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxSize()
    ) {
        LazyColumn {
            items(countLines) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .horizontalScroll(stateHorizontal)
                        .background(
                            color = if (row == 0) Color.Blue else if (row % 2 == 0) Color.White else Color(
                                224,
                                224,
                                224
                            )
                        ),
                    horizontalArrangement = Arrangement.Start
                ) {
                    for (it in 0..1) {
                        Text(
                            text = currentNote[it + row * 2],
                            modifier = if (it != 0 || row == 0) Modifier
                                .padding(13.dp)
                                .width(780.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    val clientInfo = Repository.getClientInfo(clientIds[row - 1])
                                    StateWrapper().fillClientInfoState(
                                        clientInfoViewModel,
                                        clientInfo.first
                                    )
                                    clientInfoViewModel.updateAddInfo(clientInfo.first.second)
                                    clientInfoViewModel.updateCountLines(clientInfo.second.first)
                                    clientInfoViewModel.updatePetIds(clientInfo.second.second)
                                    tabViewModel.addTab(Screen.ClientInfoScreen.name)
                                    tabViewModel.updateActiveTab(Screen.ClientInfoScreen.name)
                                    navController.navigate(Screen.ClientInfoScreen.name)
                                }
                                .padding(13.dp)
                                .width(780.dp),
                            textAlign = TextAlign.Center,
                            color = if (row == 0) Color.White else Color.Black,
                            fontSize = if (row == 0) 18.sp else 16.sp
                        )

                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight(),
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun newClientAdder(
    clientsViewModel: ClientsViewModel,
    tabViewModel: TabViewModel,
    navController: NavController,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel,
    clientInfoViewModel: ClientInfoViewModel
) {
    val labels = listOf("Фамилия", "Имя", "Отчество", "Телефон", "Адрес", "Эл. почта")
    val labelsForPet = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")
    Card(
        modifier = Modifier.padding(start = 300.dp, top = 100.dp).zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(176, 224, 230))
                .width(500.dp)
                .height(600.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Row() {
                Text(
                    text = if (!clientsViewModel.addingNewPet()) "Введите данные о клиенте: " else "Введите данные о питомце: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = 100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        clientsViewModel.updateAddingNewClient(false)
                        clientsViewModel.updateAddingNewPet(false)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 80.dp, start = 60.dp, end = 8.dp)
                    .background(color = Color(64, 224, 208), shape = RoundedCornerShape(16.dp))
            ) {
                items(if (!clientsViewModel.addingNewPet()) 6 else 5) { count ->
                    Row(
                        modifier = Modifier
                            .padding(
                                top = if (count % 3 == 1) 3.dp else 0.dp
                            )
                    ) {
                        Text(
                            if (!clientsViewModel.addingNewPet()) labels[count] + ": " else labelsForPet[count] + ": ",
                            modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = when (count) {
                                0 -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.secondName()
                                    else clientsViewModel.nickname()
                                }

                                1 -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.firstName()
                                    else clientsViewModel.kind()
                                }

                                2 -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.lastName()
                                    else clientsViewModel.breed()
                                }

                                3 -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.phoneNumber()
                                    else clientsViewModel.male()
                                }

                                4 -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.address()
                                    else clientsViewModel.age()
                                }

                                else -> {
                                    if (!clientsViewModel.addingNewPet()) clientsViewModel.email()
                                    else ""
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updateSecondName(it)
                                        else clientsViewModel.updateNickname(it)
                                    }

                                    1 -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updateFirstName(it)
                                        else clientsViewModel.updateKind(it)
                                    }

                                    2 -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updateLastName(it)
                                        else clientsViewModel.updateBreed(it)
                                    }

                                    3 -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updatePhoneNumber(it)
                                        else clientsViewModel.updateMale(it)
                                    }

                                    4 -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updateAddress(it)
                                        else clientsViewModel.updateAge(it)
                                    }

                                    else -> {
                                        if (!clientsViewModel.addingNewPet()) clientsViewModel.updateEmail(it)
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(264.dp)
                                .padding(8.dp),
                            placeholder = {
                                if (!clientsViewModel.addingNewPet()) Text(labels[count])
                                else Text(labelsForPet[count])
                            },
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.width(500.dp)
            ) {
                Button(
                    onClick = {
                        if (!clientsViewModel.addingNewPet()) {
                            val clientId = Repository.addClientInfo(
                                clientsViewModel.secondName(),
                                clientsViewModel.firstName(),
                                clientsViewModel.lastName(),
                                clientsViewModel.address(),
                                clientsViewModel.phoneNumber(),
                                clientsViewModel.email()
                            )
                            clientInfoViewModel.updateClientId(clientsViewModel.clientId())
                            clientInfoViewModel.updateFirstName(clientsViewModel.firstName())
                            clientInfoViewModel.updateSecondName(clientsViewModel.secondName())
                            clientInfoViewModel.updateLastName(clientsViewModel.lastName())
                            clientInfoViewModel.updateAddress(clientsViewModel.address())
                            clientInfoViewModel.updatePhoneNumber(clientsViewModel.phoneNumber())
                            clientInfoViewModel.updateEmail(clientsViewModel.email())
                            clientsViewModel.updateClientId(clientId)
                            clientsViewModel.updateAddingNewPet(true)
                            clientsViewModel.updateSecondName("")
                            clientsViewModel.updateFirstName("")
                            clientsViewModel.updateLastName("")
                            clientsViewModel.updateAddress("")
                            clientsViewModel.updatePhoneNumber("")
                            clientsViewModel.updateEmail("")
                        } else {
                            Repository.addPetInfo(
                                clientsViewModel.nickname(),
                                clientsViewModel.kind(),
                                clientsViewModel.breed(),
                                clientsViewModel.male(),
                                clientsViewModel.age(),
                                clientsViewModel.clientId()
                            )
                            clientsViewModel.updateAddingNewPet(false)
                            clientsViewModel.updateAddingNewClient(false)
                            val petId = Repository.getPetId(
                                clientsViewModel.nickname(),
                                clientsViewModel.kind(),
                                clientsViewModel.breed(),
                                clientsViewModel.male(),
                                clientsViewModel.age(),
                                clientsViewModel.clientId()
                            )
                            StateWrapper().fillPetInfoState(
                                petInfoViewModel,
                                listOf(
                                    clientsViewModel.nickname(),
                                    clientsViewModel.kind(),
                                    clientsViewModel.breed(),
                                    clientsViewModel.male(),
                                    clientsViewModel.age()
                                )
                            )
                            illnessHistoryViewModel.updateId(petId)
                            tabViewModel.addTab(Screen.OutpatientCardScreen.name)
                            tabViewModel.updateActiveTab(Screen.OutpatientCardScreen.name)
                            navController.navigate(Screen.OutpatientCardScreen.name)
                            tabViewModel.updateNicknameTab(clientsViewModel.nickname())
                            illnessHistoryViewModel.updateIsPattern(false)
                            illnessHistoryViewModel.updateIsNewVisitInfo(true)
                            val dateNow = Date()
                            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                            illnessHistoryViewModel.updateDate(formatForDateNow.format(dateNow))
                            StateWrapper().clearIllnessHistoryState(illnessHistoryViewModel)
                            illnessHistoryViewModel.updateCompletedIds(listOf())
                            illnessHistoryViewModel.updateCompletedPair(
                                Pair(
                                    listOf("Услуга"),
                                    listOf(
                                        listOf(listOf("Препараты")),
                                        listOf(listOf("Количество")),
                                        listOf(listOf("Своё"))
                                    )
                                )
                            )
                            illnessHistoryViewModel.clearMeasures(listOf(""))
                            illnessHistoryViewModel.clearOwnerDrugs(listOf("false"))
                            illnessHistoryViewModel.updateCountLines(0)
                            StateWrapper().clearIllnessHistoryState(illnessHistoryViewModel)
                            clientsViewModel.updateKind("")
                            clientsViewModel.updateBreed("")
                            clientsViewModel.updateNickname("")
                            clientsViewModel.updateAge("")
                            clientsViewModel.updateMale("")
                            illnessHistoryViewModel.updatePrice(0)
                        }
                    },
                    modifier = Modifier.align(Alignment.Center).padding(top = 530.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text("Добавить")
                }
            }
        }
    }
}