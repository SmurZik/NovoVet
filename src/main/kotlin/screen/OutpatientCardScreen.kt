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
import state.*
import java.text.SimpleDateFormat
import java.util.Date
import javax.xml.crypto.Data

@Composable
fun buildOutpatientCard(
    navController: NavController,
    shareDataState: ShareDataState,
    tabState: TabState,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState,
) {
    var firstNote by remember { mutableStateOf(false) }

    val labels2 = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")


//    CoroutineScope(Dispatchers.Default).launch {
//    if (shareDataState.shareData().second == "") illnessHistoryState.updateVisit(info.first.second)
//    StateWrapper().fillIllnessHistoryState(illnessHistoryState, illnessHistoryState.visit())
    val id = illnessHistoryState.id()
    val note = illnessHistoryState.note()
    val dates = DataImpl().getVisitDates(id)
    if (note.isNotEmpty()) {
//        onSaveChange(true)
        //secondName = note[0]
        //firstName = note[1]
        //lastName = note[2]
        //phoneNumber = note[7]
        //address = note[6]
    } else {
        firstNote = true
    }

    Box(
        modifier = Modifier
            .background(color = Color(176, 224, 230))
            .fillMaxSize()
    ) {

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
                    if (petInfoState.save()) {
                        DataImpl().setPetInfo(
                            petInfoState.nickname(),
                            petInfoState.breed(),
                            petInfoState.kind(),
                            petInfoState.male(),
                            petInfoState.age(),
                            id
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
            dates,
            illnessHistoryState,
            shareDataState,
            illnessHistoryState.visit()
        )
    }
}

@Composable
fun buildVisitNote(
    dates: List<Pair<String, String>>,
    illnessHistoryState: IllnessHistoryState,
    shareDataState: ShareDataState,
    visit: List<String>
) {

    val dateNow = Date()
    val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    var enablePrevious by remember { mutableStateOf(true) }
    var enableNext by remember { mutableStateOf(true) }
    val visitDate =
        DataImpl().readableDateFormat(visit[11] + " " + visit[12])
    if (illnessHistoryState.visit().isNotEmpty() && illnessHistoryState.getIsPattern()) {
        illnessHistoryState.updateDate(visitDate)
    }
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
        ) {
            IconButton(
                onClick = {
                    illnessHistoryState.updateIsPattern(false)
                    illnessHistoryState.updateIsNewVisitInfo(false)
                    StateWrapper().fillIllnessHistoryState(illnessHistoryState, visit)
                    illnessHistoryState.updateDate(visitDate)
                },
                modifier = Modifier.padding(top = 16.dp, start = 16.dp).size(32.dp),
                enabled = !illnessHistoryState.getIsNew()
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = {
                    illnessHistoryState.updateIsPattern(false)
                    illnessHistoryState.updateIsNewVisitInfo(true)
                    StateWrapper().clearIllnessHistoryState(illnessHistoryState)
                    illnessHistoryState.updateDate(formatForDateNow.format(dateNow))
                },
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp).size(32.dp)
            ) {
                Icon(
                    painter = painterResource("/add.svg"),
                    contentDescription = "Add",
                    Modifier.size(32.dp)
                )
            }
            if (!illnessHistoryState.getIsPattern()) {
                IconButton(
                    onClick = {
                        DataImpl().setVisitInfo(
                            id = illnessHistoryState.visitId(),
                            isNew = illnessHistoryState.getIsNewVisitInfo(),
                            petId = illnessHistoryState.id(),
                            date = illnessHistoryState.date(),
                            sum = "500",
                            ownerWords = illnessHistoryState.ownerWords(),
                            commonFeeling = illnessHistoryState.commonFeeling(),
                            temperature = illnessHistoryState.temperature(),
                            appetite = illnessHistoryState.appetite(),
                            vomit = illnessHistoryState.vomit(),
                            defication = illnessHistoryState.defication(),
                            urination = illnessHistoryState.urination(),
                            extra = illnessHistoryState.extra(),
                            diagnosis = illnessHistoryState.diagnosis(),
                            completed = illnessHistoryState.completed(),
                            recommendations = illnessHistoryState.recommendations()
                        )
                        val info =
                            DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
                        StateWrapper().fillIllnessHistoryState(illnessHistoryState, info.first.second)
                        illnessHistoryState.updateIsPattern(true)
                        println(illnessHistoryState.visit())
                    },
                    modifier = Modifier.padding(top = 16.dp).size(32.dp)
                ) {
                    Image(
                        painter = painterResource("/save.svg"),
                        contentDescription = "Save",
                        Modifier.size(32.dp)
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp, start = 128.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                enabled = illnessHistoryState.getIsPattern() && enablePrevious,
                onClick = {
                    val currentDate = illnessHistoryState.date()
                    val day = currentDate.split(" ")[0]
                    val time = currentDate.split(" ")[1]
                    val index = dates.indexOf(day to time)
                    if (index > 0) {
                        enableNext = true
                        val newDate = dates[index - 1]
                        val newVisitInfo =
                            DataImpl().getInfoByPetId(shareDataState.shareData().first to newDate.first + " " + newDate.second)
                        illnessHistoryState.updateVisit(newVisitInfo.first.second)
                        illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                    } else {
                        enablePrevious = false
                    }
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
                modifier = Modifier.wrapContentSize(Alignment.CenterEnd).padding(start = 8.dp, end = 8.dp, top = 16.dp)
            ) {

                Text(
                    illnessHistoryState.date(),
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
                    for (i in dates.size - 1 downTo 0) {
                        val tempDate = dates[i]
                        DropdownMenuItem(
                            enabled = illnessHistoryState.getIsPattern(),
                            onClick = {
                                val newVisitInfo =
                                    DataImpl().getInfoByPetId(shareDataState.shareData().first to tempDate.first + " " + tempDate.second)
                                illnessHistoryState.updateVisit(newVisitInfo.first.second)
                                illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                                expanded2 = false
                                enableNext = true
                                enablePrevious = true
                            }
                        ) {
                            Text(DataImpl().readableDateFormat(tempDate.first + " " + tempDate.second))
                        }
                    }
                }
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                enabled = illnessHistoryState.getIsPattern() && enableNext,
                onClick = {
                    val currentDate = illnessHistoryState.date()
                    val day = currentDate.split(" ")[0]
                    val time = currentDate.split(" ")[1]
                    val index = dates.indexOf(day to time)
                    if (index < dates.size - 1) {
                        enablePrevious = true
                        val newDate = dates[index + 1]
                        val newVisitInfo =
                            DataImpl().getInfoByPetId(shareDataState.shareData().first to newDate.first + " " + newDate.second)
                        illnessHistoryState.updateVisit(newVisitInfo.first.second)
                        illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                    } else {
                        enableNext = false

                    }
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
        }
        buildExamination(
            illnessHistoryState,
            illnessHistoryState.visit()
        )
    }
}

@Composable
fun buildExamination(
    illnessHistoryState: IllnessHistoryState,
    visit: List<String>
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

//    if (visit.isNotEmpty()) {
//        illnessHistoryState.updateOwnerWords(visit[0])
//        illnessHistoryState.updateCommonFeeling(visit[1])
//        illnessHistoryState.updateTemperature(visit[2])
//        illnessHistoryState.updateAppetite(visit[3])
//        illnessHistoryState.updateVomit(visit[4])
//        illnessHistoryState.updateDefication(visit[5])
//        illnessHistoryState.updateUrination(visit[6])
//        illnessHistoryState.updateExtra(visit[7])
//
//        illnessHistoryState.updateDiagnosis(visit[8])
//        illnessHistoryState.updateCompleted(visit[9])
//        illnessHistoryState.updateRecommendations(visit[10])
//
//        illnessHistoryState.updateDate(DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
//    }
    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 66.dp)
            .background(color = Color.White)
            .fillMaxSize()
    ) {

        if (illnessHistoryState.getIsPattern()) {
            if (visit.isNotEmpty() && !illnessHistoryState.getIsNew()) {
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

//            if (illnessHistoryState.getIsAdded()) {
//                illnessHistoryState.updateOwnerWords("")
//                illnessHistoryState.updateCommonFeeling("")
//                illnessHistoryState.updateTemperature("")
//                illnessHistoryState.updateAppetite("")
//                illnessHistoryState.updateVomit("")
//                illnessHistoryState.updateDefication("")
//                illnessHistoryState.updateUrination("")
//                illnessHistoryState.updateExtra("")
//
//                illnessHistoryState.updateDiagnosis("")
//                illnessHistoryState.updateCompleted("")
//                illnessHistoryState.updateRecommendations("")
//            }

            if (!illnessHistoryState.next()) {
                Text(
                    "Шаблон осмотра: ",
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 32.dp, top = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .wrapContentWidth(if (illnessHistoryState.next()) Alignment.Start else Alignment.End)
            ) {
                Button(
                    onClick = {
                        illnessHistoryState.updateNext(!illnessHistoryState.next())
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    if (illnessHistoryState.next()) {
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
                        illnessHistoryState.updateIsPattern(true)
                        illnessHistoryState.updateIsNewVisitInfo(false)
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

            if (!illnessHistoryState.next()) {
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
                            illnessHistoryState.next()
                        )
                    }
                }
            } else {
                buildBiggerNote(
                    illnessHistoryState.completed(),
                    onTextChange = { illnessHistoryState.updateCompleted(it) },
                    "Выполнено в клинике: ",
                    illnessHistoryState.next()
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