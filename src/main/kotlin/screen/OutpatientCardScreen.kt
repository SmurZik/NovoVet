package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.DataImpl
import navcontroller.NavController
import navcontroller.Screen
import state.IllnessHistoryState
import state.PetInfoState
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun buildOutpatientCard(
    navController: NavController,
    shareData: Pair<Int, String>,
    onActiveTabChange: (String) -> Unit,
    onTabsSub: (String) -> Unit,
    onTabsAdd: (String) -> Unit,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState
) {
    var secondName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val labels = listOf("Фамилия", "Имя", "Отчество", "Телефон", "Адрес")


    var firstNote by remember { mutableStateOf(false) }

    val labels2 = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")


//    CoroutineScope(Dispatchers.Default).launch {
    val info = DataImpl().getInfoByPetId(shareData)
    val note = info.first.first
    var visit by remember { mutableStateOf(info.first.second) }
    val id = info.second
    val dates = DataImpl().getVisitDates(id)
    if (note.isNotEmpty()) {
//        onSaveChange(true)
        //secondName = note[0]
        //firstName = note[1]
        //lastName = note[2]
        //phoneNumber = note[7]
        //address = note[6]

        if (!petInfoState.save()) {
            petInfoState.updateNickname(note[0])
            petInfoState.updateKind(note[1])
            petInfoState.updateBreed(note[2])
            petInfoState.updateMale(note[3])
            petInfoState.updateAge(note[4])
        }
    } else {
        firstNote = true
    }

    Box(
        modifier = Modifier
            .background(color = Color(176, 224, 230))
            .fillMaxSize()
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screen.JournalScreen.name)
                onActiveTabChange(Screen.JournalScreen.name)
                onTabsAdd(Screen.JournalScreen.name)
                onTabsSub(Screen.OutpatientCardScreen.name)
            }
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(50.dp)
            )
        }

//        Text(
//            text = "Данные о хозяине: ",
//            modifier = Modifier
//                .padding(top = 50.dp, start = 16.dp),
//            fontSize = 20.sp,
//            fontStyle = FontStyle.Italic
//        )
//
//        LazyHorizontalGrid(
//            rows = GridCells.Fixed(3),
//            modifier = Modifier
//                .padding(top = 80.dp, start = 8.dp)
//                .height(195.dp)
//                .background(color = Color(64, 224, 208), shape = RoundedCornerShape(16.dp))
//        ) {
//            items(5) { count ->
//                Row(
//                    modifier = Modifier
//                        .height(60.dp)
//                        .padding(
//                            top = if (count % 3 == 1) 3.dp else 0.dp
//                        )
//                ) {
//                    Text(
//                        labels[count] + ": ",
//                        modifier = Modifier
//                            .width(100.dp)
//                            .padding(
//                                start = 8.dp,
//                                top = if (count % 3 == 0) 16.dp else 0.dp
//                            )
//                            .align(Alignment.CenterVertically),
//                        textAlign = TextAlign.Start,
//                        fontSize = 18.sp
//                    )
//                    TextField(
//                        value = when (count) {
//                            0 -> secondName
//                            1 -> firstName
//                            2 -> lastName
//                            3 -> phoneNumber
//                            else -> address
//                        },
//                        onValueChange = {
//                            when (count) {
//                                0 -> {
//                                    secondName = it
//                                }
//
//                                1 -> {
//                                    firstName = it
//                                }
//
//                                2 -> lastName = it
//                                3 -> phoneNumber = it
//                                else -> address = it
//                            }
//                        },
//                        modifier = Modifier
//                            .width(200.dp)
//                            .padding(
//                                top = if (count == 0) 8.dp else if (count == 3) 8.dp else 0.dp,
//                                bottom = if (count == 2) 8.dp else 0.dp,
//                                end = if (count > 2) 8.dp else 0.dp
//                            )
//                            .height(55.dp),
//                        placeholder = {
//                            Text(labels[count])
//                        },
//                        singleLine = true,
//                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
//                        readOnly = !save
//                    )
//                }
//            }
//        }

        Text(
            text = "Данные о питомце: ",
            modifier = Modifier
                .padding(top = 50.dp, start = 16.dp),
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 80.dp, start = 8.dp)
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
                        labels2[count] + ": ",
                        modifier = Modifier
                            .width(100.dp)
                            .padding(
                                8.dp
                            )
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp
                    )
                    TextField(
                        value = when (count) {
                            0 -> petInfoState.nickname()
                            1 -> petInfoState.kind()
                            2 -> petInfoState.breed()
                            3 -> petInfoState.male()
                            else -> petInfoState.age()
                        },
                        onValueChange = {
                            when (count) {
                                0 -> {
                                    petInfoState.updateNickname(it)
                                }

                                1 -> {
                                    petInfoState.updateKind(it)
                                }

                                2 -> petInfoState.updateBreed(it)
                                3 -> petInfoState.updateMale(it)
                                else -> petInfoState.updateAge(it)
                            }
                        },
                        modifier = Modifier
                            .width(264.dp)
                            .padding(
                                8.dp
                            ),
                        placeholder = {
                            Text(labels2[count])
                        },
                        singleLine = true,
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        readOnly = !petInfoState.save()
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight().width(380.dp)
        ) {
            Button(
                onClick = {
                    if (petInfoState.save() && firstNote) {
                        DataImpl().setPersonInfo(
                            firstName,
                            secondName,
                            lastName,
                            petInfoState.nickname(),
                            address,
                            phoneNumber,
                            petInfoState.breed(),
                            petInfoState.kind(),
                            petInfoState.male(),
                            petInfoState.age()
                        )
                    }
                    petInfoState.updateSave(!petInfoState.save())
                },
                modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Text(if (!petInfoState.save()) "Редактировать" else "Сохранить")
            }
        }
        buildVisitNote(
            visit,
            dates,
            onVisitChange = { visit = it },
            illnessHistoryState
        )
    }
}

@Composable
fun buildVisitNote(
    visit: List<String>, dates: List<String>, onVisitChange: (List<String>) -> Unit,
    illnessHistoryState: IllnessHistoryState
) {

    val dateNow = Date()
    val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm")
    val dateTrue =
        if (visit.isEmpty()) formatForDateNow.format(dateNow) else illnessHistoryState.date()
    Box(
        modifier = Modifier
            .padding(start = 380.dp)
            .background(color = Color.Cyan)
            .fillMaxSize()
    ) {
        var expanded2 by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
//                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = {
                    illnessHistoryState.updateIsPattern(true)
                    illnessHistoryState.updateIsEdit(true)
                },
                modifier = Modifier.padding(top = 14.dp, start = 16.dp).size(20.dp, 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit"
                )
            }

            IconButton(
                onClick = {
                    illnessHistoryState.updateIsPattern(true)
                    illnessHistoryState.updateIsEdit(false)
                },
                modifier = Modifier.padding(start = 0.dp, bottom = 8.dp, top = 8.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }

            Button(
                modifier = Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Previous",
                )
                Text(
                    "Предыдущая",
                    fontStyle = FontStyle.Normal,
                    letterSpacing = 0.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Box(
                modifier = Modifier.wrapContentSize(Alignment.CenterEnd).padding(all = 8.dp)
            ) {

                Text(
                    dateTrue,
                    modifier = Modifier
                        .clickable {
                            expanded2 = !expanded2
                        }
                        .height(35.dp)
                        .width(200.dp)
                        .background(color = Color.White)
                        .align(Alignment.Center)
                        .padding(top = 5.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                    modifier = Modifier
                        .background(color = Color.Cyan)
                        .padding(horizontal = 18.dp)
                ) {
                    for (i in dates.size - 1 downTo 0 step 2) {
                        DropdownMenuItem(
                            onClick = {
//                                onVisitChange(DataImpl().getInfoByPetId(0).first.second)
                                expanded2 = false
                            }
                        ) {
                            Text(DataImpl().readableDateFormat(dates[i - 1] + " " + dates[i]))
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = {

                }
            ) {
                Text(
                    "Следующая",
                    fontStyle = FontStyle.Normal,
                    letterSpacing = 0.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next",
                )
            }

            IconButton(
                onClick = {

                },
                modifier = Modifier.padding(top = 10.dp, start = 16.dp).size(30.dp, 30.dp)
            ) {
                Image(
                    painter = painterResource("/save.png"),
                    contentDescription = "Save"
                )
            }
        }
        buildExamination(
            illnessHistoryState,
            visit,
            dateTrue
        )
    }
}

@Composable
fun buildExamination(
    illnessHistoryState: IllnessHistoryState,
    visit: List<String>,
    date: String
) {
    val labels = listOf(
        "Со слов владельца: ",
        "Общее состояние: ",
        "Температура: ",
        "Аппетит: ",
        "Рвота: ",
        "Дефекация: ",
        "Мочеиспускание: ",
        "Дополнительная информация: "
    )
    val labelsBigger = listOf(
        "Предварительный диагноз: ",
        "Выполнено в клинике: ",
        "Рекомендации: "
    )

    var next by remember { mutableStateOf(false) }

    if (visit.isNotEmpty() && !illnessHistoryState.getIsEdit()) {
        illnessHistoryState.updateOwnerWords(visit[0])
        illnessHistoryState.updateCommonFeeling(visit[1])
        illnessHistoryState.updateTemperature(visit[2])
        illnessHistoryState.updateAppetite(visit[3])
        illnessHistoryState.updateVomit(visit[4])
        illnessHistoryState.updateDefication(visit[5])
        illnessHistoryState.updateUrination(visit[6])
        illnessHistoryState.updateExtra(visit[7])

        illnessHistoryState.updateDiagnosis(visit[8])
        illnessHistoryState.updateCompleted(visit[9])
        illnessHistoryState.updateRecommendations(visit[10])

        illnessHistoryState.updateDate(DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
    }
    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 66.dp)
            .background(color = Color.White)
            .fillMaxSize()
    ) {

        if (!illnessHistoryState.getIsPattern()) {
            if (visit.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    item {
                        Text(
                            "Осмотр: ",
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier.border(width = 2.dp, color = Color.Black)
                        ) {
                            for (it in 0..7) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(all = 8.dp)
                                ) {
                                    Text(
                                        (labels + labelsBigger)[it],
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(200.dp)
                                    )
                                    Text(visit[it], fontSize = 18.sp, modifier = Modifier.padding(start = 50.dp))
                                }
                            }
                        }
                    }
                    items(3) {
                        Text(
                            labelsBigger[it],
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().border(2.dp, Color.Black)
                        ) {
                            Text(visit[it + 8], fontSize = 18.sp, modifier = Modifier.padding(all = 8.dp))
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "Нет истории посещений",
                    fontSize = 48.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {

            if (!illnessHistoryState.getIsEdit()) {
                illnessHistoryState.updateOwnerWords("")
                illnessHistoryState.updateCommonFeeling("")
                illnessHistoryState.updateTemperature("")
                illnessHistoryState.updateAppetite("")
                illnessHistoryState.updateVomit("")
                illnessHistoryState.updateDefication("")
                illnessHistoryState.updateUrination("")
                illnessHistoryState.updateExtra("")

                illnessHistoryState.updateDiagnosis("")
                illnessHistoryState.updateCompleted("")
                illnessHistoryState.updateRecommendations("")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .wrapContentWidth(if (next) Alignment.Start else Alignment.End)
            ) {
                if (!next) {
                    Text(
                        "Шаблон осмотра: ",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(end = 80.dp, top = 8.dp)
                    )
                }
                Button(
                    onClick = {
                        next = !next
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    if (next) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    } else {
                        Text(
                            text = "Пропустить заполнение шаблона",
                            letterSpacing = 0.sp
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "next"
                        )
                    }
                }
                Button(
                    onClick = {
                        illnessHistoryState.updateIsPattern(false)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }

            if (!next) {
                LazyColumn(
                    modifier = Modifier.padding(top = 60.dp)
                ) {

                    items(8) { count ->
                        var currentData = when (count) {
                            0 -> illnessHistoryState.ownerWords()
                            1 -> illnessHistoryState.commonFeeling()
                            2 -> illnessHistoryState.temperature()
                            3 -> illnessHistoryState.appetite()
                            4 -> illnessHistoryState.vomit()
                            5 -> illnessHistoryState.defication()
                            6 -> illnessHistoryState.urination()
                            else -> illnessHistoryState.extra()
                        }

                        buildOneNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    0 -> illnessHistoryState.updateOwnerWords(it)
                                    1 -> illnessHistoryState.updateCommonFeeling(it)
                                    2 -> illnessHistoryState.updateTemperature(it)
                                    3 -> illnessHistoryState.updateAppetite(it)
                                    4 -> illnessHistoryState.updateVomit(it)
                                    5 -> illnessHistoryState.updateDefication(it)
                                    6 -> illnessHistoryState.updateUrination(it)
                                    else -> illnessHistoryState.updateExtra(it)
                                }
                            },
                            labels[count],
                            count
                        )
                    }
                    items(3) { count ->
                        var currentData = when (count) {
                            0 -> illnessHistoryState.diagnosis()
                            1 -> illnessHistoryState.completed()
                            else -> illnessHistoryState.recommendations()
                        }
                        buildBiggerNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    0 -> illnessHistoryState.updateDiagnosis(it)
                                    1 -> illnessHistoryState.updateCompleted(it)
                                    else -> illnessHistoryState.updateRecommendations(it)
                                }
                            },
                            labelsBigger[count],
                            next
                        )
                    }
                }
            } else {
                buildBiggerNote(
                    illnessHistoryState.completed(),
                    onTextChange = { illnessHistoryState.updateCompleted(it) },
                    "Выполнено в клинике: ",
                    next
                )
            }
        }
    }
}

@Composable
fun buildBiggerNote(text: String, onTextChange: (String) -> Unit, label: String, next: Boolean) {
    Box(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = if (next) 50.dp else 8.dp)
            .fillMaxWidth()
            .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            label,
            fontSize = 18.sp,
            modifier = Modifier.padding(all = 8.dp)
        )
        TextField(
            value = text,
            onValueChange = { onTextChange(it) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0, 255, 210)),
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 8.dp, end = 8.dp),
            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
            placeholder = { Text("Введите текст здесь") }
        )
    }
}

@Composable
fun buildOneNote(text: String, onTextChange: (String) -> (Unit), label: String, count: Int) {
    val fontSize = 18
    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth()
            .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            label,
            fontSize = fontSize.sp,
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp).width(250.dp)
        )
        if (count != 2) {
            TextField(
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = { Text("Введите текст здесь") },
                modifier = Modifier.padding(start = 80.dp).fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0, 255, 210)),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                shape = RoundedCornerShape(8.dp)
            )
        } else {
            TextField(
                value = text,
                onValueChange = { onTextChange(it) },
                modifier = Modifier.padding(start = 80.dp).width(100.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0, 255, 210)),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                shape = RoundedCornerShape(8.dp)
            )
            Text(
                "°C",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}