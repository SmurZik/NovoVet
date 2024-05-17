package screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.DataImpl
import navcontroller.NavController
import navcontroller.Screen
import state.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun markup(
    navController: NavController,
    shareDataState: ShareDataState,
    tabState: TabState,
    outpatientScreenState: OutpatientScreenState,
    illnessHistoryState: IllnessHistoryState,
    petInfoState: PetInfoState
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
    LazyRow(
        modifier = Modifier
//            .padding(start = 60.dp)
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
    ) {
        item {
            Text(
                text = "Амбулаторные приемы",
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
            )
        }
    }
    val textButton = listOf("Начать прием", "Найти")
    val stateHorizontal = rememberScrollState(0)
//    val scope = CoroutineScope(Dispatchers.Default)
    LazyRow(
        modifier = Modifier
            .padding(top = 30.dp)
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
            .height(100.dp)
    ) {
        items(2) {
            Button(
                onClick = {
                    if (it == 1) {
                        outpatientScreenState.updateIsSearch(!outpatientScreenState.getIsSearch())
                        outpatientScreenState.updateSearchText("")
                        outpatientScreenState.updateSearchBy("secondName")
                        outpatientScreenState.updateAddText(" фамилии")
                    } else {
                        shareDataState.updateShareData(Pair(0, ""))
                        tabState.addTab(Screen.OutpatientCardScreen.name)
                        tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
                        navController.navigate(Screen.OutpatientCardScreen.name)
                        tabState.updateNicknameTab("Новый")
                        StateWrapper().clearIllnessHistoryState(illnessHistoryState)
                        val dateNow = Date()
                        val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm")
                        illnessHistoryState.updateDate(formatForDateNow.format(dateNow))
                        StateWrapper().clearPetInfoState(petInfoState)
                        illnessHistoryState.updateIsNew(true)
                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp, end = 8.dp)
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
    if (outpatientScreenState.getIsSearch()) {
        Column(
            modifier = Modifier
                .padding(start = 400.dp, top = 5.dp)
                .background(
                    color = /*Color(218, 189, 171)*/ /*Color.Blue*/ Color(
                        127,
                        199,
                        255
                    ),
                    shape = CutCornerShape(5.dp)
                )
        ) {
            var expanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.background(color = Color.Transparent)
            ) {
                Text(
                    "Поиск по: ${outpatientScreenState.addText()}",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(color = Color.Cyan)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                outpatientScreenState.updateSearchBy("secondName")
                                outpatientScreenState.updateAddText(" фамилии")
                                expanded = false
                            }
                        ) {
                            Text("Фамилия")
                        }
                        DropdownMenuItem(
                            onClick = {
                                outpatientScreenState.updateSearchBy("firstName")
                                outpatientScreenState.updateAddText(" имени")
                                expanded = false
                            }
                        ) {
                            Text("Имя")
                        }
                        DropdownMenuItem(
                            onClick = {
                                outpatientScreenState.updateAddText(" кличке")
                                outpatientScreenState.updateSearchBy("nickname")
                                expanded = false
                            }
                        ) {
                            Text("Кличка")
                        }
                    }
                }
                IconButton(
                    onClick = {
                        outpatientScreenState.updateIsSearch(false)
                        outpatientScreenState.updateSearchText("")
                        outpatientScreenState.updateSearchBy("secondName")
                        outpatientScreenState.updateAddText(" фамилии")
                    },
                    modifier = Modifier.padding(start = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }
            TextField(
                value = outpatientScreenState.searchText(),
                onValueChange = {
                    outpatientScreenState.updateSearchText(it)
                },
                label = { Text("Введите слово для поиска") },
                modifier = Modifier.width(290.dp).height(50.dp)
            )
        }
    }
    Card(
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxSize()
    ) {

        val tempPair =
            DataImpl().getOutpatientCard(outpatientScreenState.searchText(), outpatientScreenState.searchBy())
        val currentNote = tempPair.first
        val countLines = tempPair.second.first
        val petId = tempPair.second.second
//        val date = currentNote[5]

        LazyColumn {
            items(countLines) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        val temp = currentNote[it + row * 4]
                        Text(
                            text = temp,
                            modifier = if (it != 1 && it != 2 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 1) 400.dp else 300.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    if (it == 2) {
                                        shareDataState.updateShareData(petId to currentNote[it + row * 4 - 2])
                                        val info = DataImpl().getInfoByPetId(shareDataState.shareData())
                                        tabState.addTab(Screen.OutpatientCardScreen.name)
                                        tabState.updateActiveTab(Screen.OutpatientCardScreen.name)
                                        navController.navigate(Screen.OutpatientCardScreen.name)
                                        tabState.updateNicknameTab(temp)
                                        illnessHistoryState.updateVisit(info.first.second)
                                        illnessHistoryState.updateNote(info.first.first)
                                        illnessHistoryState.updateId(info.second)
                                        StateWrapper().fillPetInfoState(petInfoState, info.first.first)
                                        illnessHistoryState.updateIsNew(false)
                                    }
                                }
                                .padding(13.dp)
                                .width(if (it == 1) 400.dp else 300.dp),
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