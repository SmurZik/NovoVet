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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import data.DataImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navcontroller.NavController
import navcontroller.Screen
import state.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun buildClientInfo(
    clientInfoState: ClientInfoState,
    tabState: TabState,
    navController: NavController,
    illnessHistoryState: IllnessHistoryState,
    petInfoState: PetInfoState
) {
    val labels = listOf("Фамилия", "Имя", "Отчество", "Телефон", "Адрес", "Эл. почта")
    val textButton = listOf("Добавить питомца", "Найти")

    if (clientInfoState.addingNewPet()) {
        newPetAdder(clientInfoState, tabState, navController, illnessHistoryState, petInfoState)
    }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(176, 224, 230))
                .fillMaxHeight()
        ) {
            Text(
                text = "Данные о клиенте: ",
                modifier = Modifier.padding(top = 50.dp, start = 16.dp),
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 80.dp, start = 8.dp, end = 8.dp)
                    .background(color = Color(64, 224, 208), shape = RoundedCornerShape(16.dp))
            ) {
                items(6) { count ->
                    Row(
                        modifier = Modifier
                            .padding(
                                top = if (count % 3 == 1) 3.dp else 0.dp
                            )
                    ) {
                        Text(
                            labels[count] + ": ",
                            modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = when (count) {
                                0 -> clientInfoState.secondName()
                                1 -> clientInfoState.firstName()
                                2 -> clientInfoState.lastName()
                                3 -> clientInfoState.phoneNumber()
                                4 -> clientInfoState.address()
                                else -> clientInfoState.email()
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        clientInfoState.updateSecondName(it)
                                    }

                                    1 -> {
                                        clientInfoState.updateFirstName(it)
                                    }

                                    2 -> clientInfoState.updateLastName(it)
                                    3 -> clientInfoState.updatePhoneNumber(it)
                                    4 -> clientInfoState.updateAddress(it)
                                    else -> clientInfoState.updateEmail(it)
                                }
                            },
                            modifier = Modifier
                                .width(264.dp)
                                .padding(8.dp),
                            placeholder = {
                                Text(labels[count])
                            },
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            readOnly = !clientInfoState.save()
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight().width(380.dp)
            ) {
                Button(
                    onClick = {
                        if (clientInfoState.save()) {
                            DataImpl().setClientInfo(
                                secondName = clientInfoState.secondName(),
                                firstName = clientInfoState.firstName(),
                                lastName = clientInfoState.lastName(),
                                address = clientInfoState.address(),
                                phone = clientInfoState.phoneNumber(),
                                email = clientInfoState.email(),
                                clientId = clientInfoState.clientId()
                            )
                        }
                        clientInfoState.updateSave(!clientInfoState.save())
                    },
                    modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text(if (!clientInfoState.save()) "Редактировать" else "Сохранить")
                }
            }
        }
        Column {
            LazyRow(
                modifier = Modifier
                    .background(color = Color.Cyan)
                    .fillMaxWidth(1f)
                    .height(110.dp)
            ) {
                items(1) {
                    Button(
                        onClick = {
                            clientInfoState.updateAddingNewPet(true)
                            clientInfoState.updateNickname("")
                            clientInfoState.updateKind("")
                            clientInfoState.updateBreed("")
                            clientInfoState.updateMale("")
                            clientInfoState.updateAge("")
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
            val stateHorizontal = rememberScrollState(0)
            LazyColumn(
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                items(clientInfoState.countLines()) { row ->
                    Row(
                        modifier = Modifier
                            .width(1108.dp)
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
                        for (it in 0..3) {
                            Text(
                                text = clientInfoState.addInfo()[it + row * 4],
                                modifier = if (it != 0 || row == 0) Modifier
                                    .padding(13.dp)
                                    .width(250.dp)
                                else Modifier
                                    .clickable {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            val info = withContext(Dispatchers.IO) {
                                                DataImpl().getInfoByPetId(clientInfoState.petIds()[row - 1] to clientInfoState.addInfo()[row * 4 + 3])
                                            }
                                            illnessHistoryState.updateVisit(info.first.second)
                                            illnessHistoryState.updateNote(info.first.first)
                                            illnessHistoryState.updateId(info.second.first)
                                            illnessHistoryState.updateVisitId(info.second.second)
                                            StateWrapper().fillPetInfoState(petInfoState, info.first.first)
                                            tabState.updateNicknameTab(petInfoState.nickname())
                                        }

                                        tabState.addTab(Screen.OutpatientCardScreen.name)
                                        tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
                                        navController.navigate(Screen.OutpatientCardScreen.name)
                                        illnessHistoryState.updateIsPattern(true)
                                        illnessHistoryState.updateIsNew(false)
                                    }
                                    .fillMaxHeight()
                                    .padding(13.dp)
                                    .width(250.dp),
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
}

@Composable
fun newPetAdder(
    clientInfoState: ClientInfoState,
    tabState: TabState,
    navController: NavController,
    illnessHistoryState: IllnessHistoryState,
    petInfoState: PetInfoState
) {
    val labelsForPet = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")
    clientInfoState.updateNickname("")
    clientInfoState.updateKind("")
    clientInfoState.updateBreed("")
    Card(
        modifier = Modifier.padding(start = 450.dp, top = 100.dp).zIndex(1f)
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
                    text = "Введите данные о питомце: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = 100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        clientInfoState.updateAddingNewPet(false)
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
                items(5) { count ->
                    Row(
                        modifier = Modifier
                            .padding(
                                top = if (count % 3 == 1) 3.dp else 0.dp
                            )
                    ) {
                        Text(
                            labelsForPet[count] + ": ",
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
                                    clientInfoState.nickname()
                                }

                                1 -> {
                                    clientInfoState.kind()
                                }

                                2 -> {
                                    clientInfoState.breed()
                                }

                                3 -> {
                                    clientInfoState.male()
                                }

                                else -> {
                                    clientInfoState.age()
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        clientInfoState.updateNickname(it)
                                    }

                                    1 -> {
                                        clientInfoState.updateKind(it)
                                    }

                                    2 -> {
                                        clientInfoState.updateBreed(it)
                                    }

                                    3 -> {
                                        clientInfoState.updateMale(it)
                                    }

                                    else -> {
                                        clientInfoState.updateAge(it)
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(264.dp)
                                .padding(8.dp),
                            placeholder = {
                                Text(labelsForPet[count])
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
                        DataImpl().addPetInfo(
                            clientInfoState.nickname(),
                            clientInfoState.kind(),
                            clientInfoState.breed(),
                            clientInfoState.male(),
                            clientInfoState.age(),
                            clientInfoState.clientId()
                        )
                        clientInfoState.updateAddingNewPet(false)
                        val petId = DataImpl().getPetId(
                            clientInfoState.nickname(),
                            clientInfoState.kind(),
                            clientInfoState.breed(),
                            clientInfoState.male(),
                            clientInfoState.age(),
                            clientInfoState.clientId()
                        )
                        StateWrapper().fillPetInfoState(
                            petInfoState,
                            listOf(
                                clientInfoState.nickname(),
                                clientInfoState.kind(),
                                clientInfoState.breed(),
                                clientInfoState.male(),
                                clientInfoState.age()
                            )
                        )
                        illnessHistoryState.updateId(petId)
                        tabState.addTab(Screen.OutpatientCardScreen.name)
                        tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
                        navController.navigate(Screen.OutpatientCardScreen.name)
                        tabState.updateNicknameTab(clientInfoState.nickname())
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
                    },
                    modifier = Modifier.align(Alignment.Center).padding(top = 500.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text("Добавить")
                }
            }
        }
    }
}