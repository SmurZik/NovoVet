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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.main.navcontroller.NavController
import presentation.main.navcontroller.Screen
import presentation.outpatient.IllnessHistoryViewModel
import presentation.outpatient.PetInfoViewModel
import presentation.main.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun buildClientInfo(
    clientInfoViewModel: ClientInfoViewModel,
    tabViewModel: TabViewModel,
    navController: NavController,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel
) {
    val labels = listOf("Фамилия", "Имя", "Отчество", "Телефон", "Адрес", "Эл. почта")
    val textButton = listOf("Добавить питомца", "Найти")

    if (clientInfoViewModel.addingNewPet()) {
        newPetAdder(clientInfoViewModel, tabViewModel, navController, illnessHistoryViewModel, petInfoViewModel)
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
                                0 -> clientInfoViewModel.secondName()
                                1 -> clientInfoViewModel.firstName()
                                2 -> clientInfoViewModel.lastName()
                                3 -> clientInfoViewModel.phoneNumber()
                                4 -> clientInfoViewModel.address()
                                else -> clientInfoViewModel.email()
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        clientInfoViewModel.updateSecondName(it)
                                    }

                                    1 -> {
                                        clientInfoViewModel.updateFirstName(it)
                                    }

                                    2 -> clientInfoViewModel.updateLastName(it)
                                    3 -> clientInfoViewModel.updatePhoneNumber(it)
                                    4 -> clientInfoViewModel.updateAddress(it)
                                    else -> clientInfoViewModel.updateEmail(it)
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
                            readOnly = !clientInfoViewModel.save()
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight().width(380.dp)
            ) {
                Button(
                    onClick = {
                        if (clientInfoViewModel.save()) {
                            Repository.setClientInfo(
                                secondName = clientInfoViewModel.secondName(),
                                firstName = clientInfoViewModel.firstName(),
                                lastName = clientInfoViewModel.lastName(),
                                address = clientInfoViewModel.address(),
                                phone = clientInfoViewModel.phoneNumber(),
                                email = clientInfoViewModel.email(),
                                clientId = clientInfoViewModel.clientId()
                            )
                        }
                        clientInfoViewModel.updateSave(!clientInfoViewModel.save())
                    },
                    modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text(if (!clientInfoViewModel.save()) "Редактировать" else "Сохранить")
                }
            }
        }
        Column {
            LazyRow(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .fillMaxWidth(1f)
                    .height(110.dp)
            ) {
                items(1) {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0, 191, 255)),
                        onClick = {
                            clientInfoViewModel.updateAddingNewPet(true)
                            clientInfoViewModel.updateNickname("")
                            clientInfoViewModel.updateKind("")
                            clientInfoViewModel.updateBreed("")
                            clientInfoViewModel.updateMale("")
                            clientInfoViewModel.updateAge("")
                        },
                        modifier = Modifier
                            .padding(top = 30.dp, end = 8.dp, start = 8.dp)
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
                items(clientInfoViewModel.countLines()) { row ->
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
                        for (it in 0..3) {
                            Text(
                                text = clientInfoViewModel.addInfo()[it + row * 4],
                                modifier = if (it != 0 || row == 0) Modifier
                                    .padding(13.dp)
                                    .width(275.dp)
                                else Modifier
                                    .clickable {
                                        CoroutineScope(Dispatchers.Default).launch {
                                            val info = withContext(Dispatchers.IO) {
                                                Repository.getInfoByPetId(clientInfoViewModel.petIds()[row - 1] to clientInfoViewModel.addInfo()[row * 4 + 3])
                                            }
                                            illnessHistoryViewModel.updateVisit(info.first.second)
                                            illnessHistoryViewModel.updateNote(info.first.first)
                                            illnessHistoryViewModel.updateId(info.second.first)
                                            illnessHistoryViewModel.updateVisitId(info.second.second)
                                            StateWrapper().fillPetInfoState(petInfoViewModel, info.first.first)
                                            tabViewModel.updateNicknameTab(petInfoViewModel.nickname())
                                        }

                                        tabViewModel.addTab(Screen.OutpatientCardScreen.name)
                                        tabViewModel.updateActiveTab(Screen.OutpatientCardScreen.name)
                                        navController.navigate(Screen.OutpatientCardScreen.name)
                                        illnessHistoryViewModel.updateIsPattern(true)
                                        illnessHistoryViewModel.updateIsNew(false)
                                    }
                                    .fillMaxHeight()
                                    .padding(13.dp)
                                    .width(275.dp),
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
}

@Composable
fun newPetAdder(
    clientInfoViewModel: ClientInfoViewModel,
    tabViewModel: TabViewModel,
    navController: NavController,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel
) {
    val labelsForPet = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")
    clientInfoViewModel.updateNickname("")
    clientInfoViewModel.updateKind("")
    clientInfoViewModel.updateBreed("")
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
                        clientInfoViewModel.updateAddingNewPet(false)
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
                                    clientInfoViewModel.nickname()
                                }

                                1 -> {
                                    clientInfoViewModel.kind()
                                }

                                2 -> {
                                    clientInfoViewModel.breed()
                                }

                                3 -> {
                                    clientInfoViewModel.male()
                                }

                                else -> {
                                    clientInfoViewModel.age()
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        clientInfoViewModel.updateNickname(it)
                                    }

                                    1 -> {
                                        clientInfoViewModel.updateKind(it)
                                    }

                                    2 -> {
                                        clientInfoViewModel.updateBreed(it)
                                    }

                                    3 -> {
                                        clientInfoViewModel.updateMale(it)
                                    }

                                    else -> {
                                        clientInfoViewModel.updateAge(it)
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
                        Repository.addPetInfo(
                            clientInfoViewModel.nickname(),
                            clientInfoViewModel.kind(),
                            clientInfoViewModel.breed(),
                            clientInfoViewModel.male(),
                            clientInfoViewModel.age(),
                            clientInfoViewModel.clientId()
                        )
                        clientInfoViewModel.updateAddingNewPet(false)
                        val petId = Repository.getPetId(
                            clientInfoViewModel.nickname(),
                            clientInfoViewModel.kind(),
                            clientInfoViewModel.breed(),
                            clientInfoViewModel.male(),
                            clientInfoViewModel.age(),
                            clientInfoViewModel.clientId()
                        )
                        StateWrapper().fillPetInfoState(
                            petInfoViewModel,
                            listOf(
                                clientInfoViewModel.nickname(),
                                clientInfoViewModel.kind(),
                                clientInfoViewModel.breed(),
                                clientInfoViewModel.male(),
                                clientInfoViewModel.age()
                            )
                        )
                        illnessHistoryViewModel.updateId(petId)
                        tabViewModel.addTab(Screen.OutpatientCardScreen.name)
                        tabViewModel.updateActiveTab(Screen.OutpatientCardScreen.name)
                        navController.navigate(Screen.OutpatientCardScreen.name)
                        tabViewModel.updateNicknameTab(clientInfoViewModel.nickname())
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
                        illnessHistoryViewModel.clearOwnerDrugs(listOf("false"))
                        illnessHistoryViewModel.clearMeasures(listOf(""))
                        illnessHistoryViewModel.updateCountLines(0)
                        StateWrapper().clearIllnessHistoryState(illnessHistoryViewModel)
                        clientInfoViewModel.updateKind("")
                        clientInfoViewModel.updateBreed("")
                        clientInfoViewModel.updateNickname("")
                        clientInfoViewModel.updateAge("")
                        clientInfoViewModel.updateMale("")
                        illnessHistoryViewModel.updatePrice(0)
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