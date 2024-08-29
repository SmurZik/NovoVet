package screen

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
import components.search
import data.DataImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import navcontroller.NavController
import navcontroller.Screen
import state.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun buildClients(
    navController: NavController,
    clientsScreenState: ClientsScreenState,
    clientInfoState: ClientInfoState,
    tabState: TabState,
    illnessHistoryState: IllnessHistoryState,
    petInfoState: PetInfoState
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
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
            .height(110.dp)
    ) {
        items(2) {
            Button(
                onClick = {
                    if (it == 1) {
                        clientsScreenState.updateIsSearch(!clientsScreenState.getIsSearch())
                        clientsScreenState.updateSearchText("")
                        clientsScreenState.updateSearchBy("secondName")
                        clientsScreenState.updateAddText(" фамилии")
                    } else {
                        clientsScreenState.updateAddingNewClient(true)
                        clientsScreenState.updateEmail("")
                        clientsScreenState.updateSecondName("")
                        clientsScreenState.updateFirstName("")
                        clientsScreenState.updateLastName("")
                        clientsScreenState.updateAddress("")
                        clientsScreenState.updatePhoneNumber("")
                        clientsScreenState.updateNickname("")
                        clientsScreenState.updateKind("")
                        clientsScreenState.updateBreed("")
                        clientsScreenState.updateMale("")
                        clientsScreenState.updateAge("")
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

    val info = DataImpl().getClientsInfo(clientsScreenState.searchText(), clientsScreenState.searchBy())
    val currentNote = info.first.second
    val clientIds = info.first.first
    val countLines = info.second

    var expanded by remember { mutableStateOf(false) }
    if (clientsScreenState.getIsSearch()) {
        search(clientsScreenState, expanded, onExpandedChange = { expanded = it })
    }

    if (clientsScreenState.addingNewClient()) {
        newClientAdder(clientsScreenState, tabState, navController, illnessHistoryState, petInfoState)
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
                        .width(1100.dp)
                        .height(50.dp)
                        .horizontalScroll(stateHorizontal)
                        .background(
                            color = if (row == 0) Color.Blue else if (row % 2 == 0) Color.Cyan else Color(
                                127,
                                199,
                                255
                            )
                        ),
                    horizontalArrangement = Arrangement.Start
                ) {
                    for (it in 0..1) {
                        Text(
                            text = currentNote[it + row * 2],
                            modifier = if (it != 0 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 0) 500.dp else 600.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    CoroutineScope(Dispatchers.Default).launch {
                                        DataImpl().getClientInfo(clientIds[row - 1], clientInfoState)
                                        StateWrapper().fillClientInfoState(
                                            clientInfoState,
                                            clientInfoState.clientInfo().first
                                        )
                                        clientInfoState.updateAddInfo(clientInfoState.clientInfo().first.second)
                                        clientInfoState.updateCountLines(clientInfoState.clientInfo().second.first)
                                        clientInfoState.updatePetIds(clientInfoState.clientInfo().second.second)
                                    }
                                    tabState.addTab(Screen.ClientInfoScreen.name)
                                    tabState.updateActiveTab(Screen.ClientInfoScreen.name)
                                    navController.navigate(Screen.ClientInfoScreen.name)
                                }
                                .padding(13.dp)
                                .width(500.dp),
                            textAlign = TextAlign.Center,
                            color = if (row == 0) Color.White else Color.Black,
                            fontSize = if (row == 0) 18.sp else 16.sp
                        )

                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight(),
                            color = Color(0, 191, 255)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun newClientAdder(
    clientsScreenState: ClientsScreenState,
    tabState: TabState,
    navController: NavController,
    illnessHistoryState: IllnessHistoryState,
    petInfoState: PetInfoState
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
            Row {
                Text(
                    text = if (!clientsScreenState.addingNewPet()) "Введите данные о клиенте: " else "Введите данные о питомце: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = 100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        clientsScreenState.updateAddingNewClient(false)
                        clientsScreenState.updateAddingNewPet(false)
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
                items(if (!clientsScreenState.addingNewPet()) 6 else 5) { count ->
                    Row(
                        modifier = Modifier
                            .padding(
                                top = if (count % 3 == 1) 3.dp else 0.dp
                            )
                    ) {
                        Text(
                            if (!clientsScreenState.addingNewPet()) labels[count] + ": " else labelsForPet[count] + ": ",
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
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.secondName()
                                    else clientsScreenState.nickname()
                                }

                                1 -> {
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.firstName()
                                    else clientsScreenState.kind()
                                }

                                2 -> {
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.lastName()
                                    else clientsScreenState.breed()
                                }

                                3 -> {
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.phoneNumber()
                                    else clientsScreenState.male()
                                }

                                4 -> {
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.address()
                                    else clientsScreenState.age()
                                }

                                else -> {
                                    if (!clientsScreenState.addingNewPet()) clientsScreenState.email()
                                    else ""
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updateSecondName(it)
                                        else clientsScreenState.updateNickname(it)
                                    }

                                    1 -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updateFirstName(it)
                                        else clientsScreenState.updateKind(it)
                                    }

                                    2 -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updateLastName(it)
                                        else clientsScreenState.updateBreed(it)
                                    }

                                    3 -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updatePhoneNumber(it)
                                        else clientsScreenState.updateMale(it)
                                    }

                                    4 -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updateAddress(it)
                                        else clientsScreenState.updateAge(it)
                                    }

                                    else -> {
                                        if (!clientsScreenState.addingNewPet()) clientsScreenState.updateEmail(it)
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(264.dp)
                                .padding(8.dp),
                            placeholder = {
                                if (!clientsScreenState.addingNewPet()) Text(labels[count])
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
                        if (!clientsScreenState.addingNewPet()) {
                            val clientId = DataImpl().addClientInfo(
                                clientsScreenState.secondName(),
                                clientsScreenState.firstName(),
                                clientsScreenState.lastName(),
                                clientsScreenState.address(),
                                clientsScreenState.phoneNumber(),
                                clientsScreenState.email()
                            )
                            clientsScreenState.updateClientId(clientId)
                            clientsScreenState.updateAddingNewPet(true)
                        } else {
                            DataImpl().addPetInfo(
                                clientsScreenState.nickname(),
                                clientsScreenState.kind(),
                                clientsScreenState.breed(),
                                clientsScreenState.male(),
                                clientsScreenState.age(),
                                clientsScreenState.clientId()
                            )
                            clientsScreenState.updateAddingNewPet(false)
                            clientsScreenState.updateAddingNewClient(false)
                            val petId = DataImpl().getPetId(
                                clientsScreenState.nickname(),
                                clientsScreenState.kind(),
                                clientsScreenState.breed(),
                                clientsScreenState.male(),
                                clientsScreenState.age(),
                                clientsScreenState.clientId()
                            )
                            StateWrapper().fillPetInfoState(
                                petInfoState,
                                listOf(
                                    clientsScreenState.nickname(),
                                    clientsScreenState.kind(),
                                    clientsScreenState.breed(),
                                    clientsScreenState.male(),
                                    clientsScreenState.age()
                                )
                            )
                            illnessHistoryState.updateId(petId)
                            tabState.addTab(Screen.OutpatientCardScreen.name)
                            tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
                            navController.navigate(Screen.OutpatientCardScreen.name)
                            tabState.updateNicknameTab(clientsScreenState.nickname())
                            illnessHistoryState.updateIsPattern(false)
                            illnessHistoryState.updateIsNewVisitInfo(true)
                            val dateNow = Date()
                            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                            illnessHistoryState.updateDate(formatForDateNow.format(dateNow))
                            StateWrapper().clearIllnessHistoryState(illnessHistoryState)
                            illnessHistoryState.updateCompletedIds(listOf())
                            illnessHistoryState.updateCompletedPair(
                                Pair(
                                    listOf("Услуга"),
                                    Pair(listOf(listOf("Препараты")), listOf(listOf("Количество")))
                                )
                            )
                            illnessHistoryState.clearMeasures(listOf(""))
                            illnessHistoryState.updateCountLines(0)
                            StateWrapper().clearIllnessHistoryState(illnessHistoryState)
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