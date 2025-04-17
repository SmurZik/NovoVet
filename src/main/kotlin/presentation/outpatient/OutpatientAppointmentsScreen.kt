package presentation.outpatient

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.components.DatePicker
import presentation.components.search
import data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.client.ClientInfoViewModel
import presentation.client.ClientsViewModel
import presentation.main.navcontroller.NavController
import presentation.main.navcontroller.Screen
import presentation.main.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dialog(
    active: Boolean,
    changeActive: (Boolean) -> Unit,
    navController: NavController,
    shareDataState: ShareDataState,
    outpatientViewModel: OutpatientViewModel,
    tabViewModel: TabViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel,
    clientsViewModel: ClientsViewModel,
) {
    var skipFirstNeg by remember { mutableStateOf(false) }
    val firstText =
        "Если есть история болезни у питомца, то нажмите да и воспользуйтесь поиском, иначе нажмите нет и создайте для него карточку"
    val thirdText = "Есть ли уже данные о клиенте?"
    if (active) {
        AlertDialog(
            onDismissRequest = {
                changeActive(false)
                skipFirstNeg = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (!skipFirstNeg) {
                            outpatientViewModel.updateIsSearch(true)
                            changeActive(false)
                            skipFirstNeg = false
                        } else {
                            tabViewModel.addTab(Screen.ClientsScreen.name)
                            tabViewModel.updateActiveTab(Screen.ClientsScreen.name)
                            navController.navigate(Screen.ClientsScreen.name)
                            clientsViewModel.updateIsSearch(true)
                        }
//                        shareDataState.updateShareData(Pair(0, ""))
//                        tabState.addTab(Screen.OutpatientCardScreen.name)
//                        tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
//                        navController.navigate(Screen.OutpatientCardScreen.name)
//                        tabState.updateNicknameTab("Новый")
//                        StateWrapper().clearIllnessHistoryState(illnessHistoryState)
//                        val dateNow = Date()
//                        val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm")
//                        illnessHistoryState.updateDate(formatForDateNow.format(dateNow))
//                        StateWrapper().clearPetInfoState(petInfoState)
//                        illnessHistoryState.updateIsNew(true)
                    }
                ) {
                    Text("Да")
                }
            },
            title = {
                Text(text = "Был раньше?")
            },
            text = {
                Text(text = if (!skipFirstNeg) firstText else thirdText)
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (!skipFirstNeg) {
                            skipFirstNeg = true
                        } else {
                            tabViewModel.addTab(Screen.ClientsScreen.name)
                            tabViewModel.updateActiveTab(Screen.ClientsScreen.name)
                            navController.navigate(Screen.ClientsScreen.name)
                            clientsViewModel.updateAddingNewClient(true)
                        }
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}

@Composable
fun markup(
    navController: NavController,
    shareDataState: ShareDataState,
    tabViewModel: TabViewModel,
    outpatientViewModel: OutpatientViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    petInfoViewModel: PetInfoViewModel,
    clientsViewModel: ClientsViewModel,
    clientInfoViewModel: ClientInfoViewModel
) {
//    LazyColumn(
//        modifier = Modifier
//            .background(color = Color(218, 189, 171))
//            .fillMaxHeight(1f)
//            .width(60.dp)
//    ) {
//        items(2) {
//            Button(
//                onClick = {},
//                modifier = Modifier.padding(horizontal = 4.dp).height(50.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
//            ) {
//                val icon = if (it == 0) Icons.Filled.Create else Icons.Filled.Home
//                Icon(
//                    icon,
//                    contentDescription = null
//                )
//            }
//        }
//    }
    var active by remember { mutableStateOf(false) }
    dialog(
        active = active,
        changeActive = { active = it },
        navController = navController,
        shareDataState = shareDataState,
        tabViewModel = tabViewModel,
        illnessHistoryViewModel = illnessHistoryViewModel,
        petInfoViewModel = petInfoViewModel,
        outpatientViewModel = outpatientViewModel,
        clientsViewModel = clientsViewModel
    )
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
    val textButton = listOf("Начать прием", "Найти")
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
                        outpatientViewModel.updateIsSearch(!outpatientViewModel.getIsSearch())
                        outpatientViewModel.updateSearchText("")
                        outpatientViewModel.updateSearchBy("secondName")
                        outpatientViewModel.updateAddText(" фамилии")
                    } else {
                        active = true
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
        val dateString = "01.01.1999"
        val formatDate = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatDate.parse(dateString)
        item {
            Text(
                text = if (outpatientViewModel.dateResult() == formattedDate) "Показаны последние 100 приемов"
                else "Показаны приемы за " + formatDate.format(outpatientViewModel.dateResult()),
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 40.dp, start = 300.dp)
            )
        }
        if (outpatientViewModel.dateResult() != formattedDate) {
            item {
                IconButton(
                    onClick = {
                        outpatientViewModel.updateDateResult(formattedDate)
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        "close"
                    )
                }
            }
        }
        item {
            IconButton(
                modifier = Modifier.padding(start = 300.dp, top = 30.dp),
                onClick = {
                    outpatientViewModel.updateOpenDialog(true)
                }
            ) {
                Icon(
                    painter = painterResource("/calendar.png"),
                    contentDescription = "calendar",
                    modifier = Modifier.size(48.dp).background(Color(0, 191, 255), shape = RoundedCornerShape(8.dp))
                )
            }
        }
    }

    if (outpatientViewModel.openDialog()) {

        //implement here the logic to show datepicker and use de return value

        DatePicker(
            initDate = Date(),
            onDismissRequest = { outpatientViewModel.updateOpenDialog(false) },
            onDateSelect = {
                outpatientViewModel.updateDateResult(it)
                outpatientViewModel.updateOpenDialog(false)
            }
        )
    }

    var expanded by remember { mutableStateOf(false) }
    if (outpatientViewModel.getIsSearch()) {
        search(outpatientViewModel, expanded, onExpandedChange = { expanded = it })
    }
    Card(
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxSize()
    ) {


        val tempPair =
            Repository().getOutpatientCard(
                outpatientViewModel.searchText(),
                outpatientViewModel.searchBy(),
                outpatientViewModel.dateResult()
            )
        val currentNote = tempPair.first.first
        val clientIds = tempPair.first.second
        val countLines = tempPair.second.first
        val petId = tempPair.second.second
//        val date = currentNote[5]

        LazyColumn {
            items(countLines) { row ->
                Row(
                    modifier = Modifier
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
                        val temp = currentNote[it + row * 4]
                        Text(
                            text = temp,
                            modifier = if (it != 1 && it != 2 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 1 || it == 2) 446.dp else 300.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    if (it == 2) {
                                        shareDataState.updateShareData(petId[row - 1] to currentNote[it + row * 4 - 2])
                                        CoroutineScope(Dispatchers.Default).launch {
                                            illnessHistoryViewModel.updateLoading(true)
                                            val info = withContext(Dispatchers.IO) {
                                                Repository().getInfoByPetId(shareDataState.shareData())
                                            }
                                            illnessHistoryViewModel.updateVisit(info.first.second)
                                            illnessHistoryViewModel.updateNote(info.first.first)
                                            illnessHistoryViewModel.updateId(info.second.first)
                                            illnessHistoryViewModel.updateVisitId(info.second.second)
                                            StateWrapper().fillPetInfoState(petInfoViewModel, info.first.first)
                                            tabViewModel.updateNicknameTab(temp)
                                            illnessHistoryViewModel.updateIsPattern(true)
                                            illnessHistoryViewModel.updateIsNew(false)
                                            val infoClient = Repository().getClientInfo(clientIds[row - 1])
                                            StateWrapper().fillClientInfoState(
                                                clientInfoViewModel,
                                                infoClient.first
                                            )
                                            illnessHistoryViewModel.updateLoading(false)
                                        }
                                        tabViewModel.addTab(Screen.OutpatientCardScreen.name)
                                        tabViewModel.updateActiveTab(Screen.OutpatientCardScreen.name)
                                        navController.navigate(Screen.OutpatientCardScreen.name)
                                    } else {
                                        val info = Repository().getClientInfo(clientIds[row - 1])
                                        StateWrapper().fillClientInfoState(
                                            clientInfoViewModel,
                                            info.first
                                        )
                                        clientInfoViewModel.updateAddInfo(info.first.second)
                                        clientInfoViewModel.updateCountLines(info.second.first)
                                        clientInfoViewModel.updatePetIds(info.second.second)
                                        tabViewModel.addTab(Screen.ClientInfoScreen.name)
                                        tabViewModel.updateActiveTab(Screen.ClientInfoScreen.name)
                                        navController.navigate(Screen.ClientInfoScreen.name)
                                    }
                                }
                                .padding(13.dp)
                                .width(if (it == 1 || it == 2) 446.dp else 300.dp),
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

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            adapter = rememberScrollbarAdapter(stateHorizontal),
            style = ScrollbarStyle(
                hoverColor = Color.DarkGray,
                minimalHeight = 1.dp,
                hoverDurationMillis = 3,
                shape = CircleShape,
                thickness = 10.dp,
                unhoverColor = Color.Gray
            )
        )
    }
}