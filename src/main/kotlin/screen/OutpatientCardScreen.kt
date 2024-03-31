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
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun buildOutpatientCard(
    navController: NavController,
    shareData: Pair<Int, String>,
    onActiveTabChange: (String) -> Unit,
    onTabsSub: (String) -> Unit,
    onTabsAdd: (String) -> Unit,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    kind: String,
    onKindChange: (String) -> Unit,
    breed: String,
    onBreedChange: (String) -> Unit,
    male: String,
    onMaleChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    save: Boolean,
    onSaveChange: (Boolean) -> Unit,
    ownerWords: String,
    onOwnerWordsChange: (String) -> Unit,
    commonFeeling: String,
    onCommonFeelingChange: (String) -> Unit,
    temperature: String,
    onTemperatureChange: (String) -> Unit,
    appetite: String,
    onAppetiteChange: (String) -> Unit,
    vomit: String,
    onVomitChange: (String) -> Unit,
    defication: String,
    onDeficationChange: (String) -> Unit,
    urination: String,
    onUrinationChange: (String) -> Unit,
    extra: String,
    onExtraChange: (String) -> Unit,
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    completed: String,
    onCompletedChange: (String) -> Unit,
    recommendations: String,
    onRecommendationsChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    isPattern: Boolean,
    onIsPatternChange: (Boolean) -> Unit,
    isEdit: Boolean,
    onIsEditChange: (Boolean) -> Unit
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

        if (!save) {
            onNicknameChange(note[0])
            onKindChange(note[1])
            onBreedChange(note[2])
            onMaleChange(note[3])
            onAgeChange(note[4])
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
                            0 -> nickname
                            1 -> kind
                            2 -> breed
                            3 -> male
                            else -> age
                        },
                        onValueChange = {
                            when (count) {
                                0 -> {
                                    onNicknameChange(it)
                                }

                                1 -> {
                                    onKindChange(it)
                                }

                                2 -> onBreedChange(it)
                                3 -> onMaleChange(it)
                                else -> onAgeChange(it)
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
                        readOnly = !save
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight().width(380.dp)
        ) {
            Button(
                onClick = {
                    if (save && firstNote) {
                        DataImpl().setPersonInfo(
                            firstName,
                            secondName,
                            lastName,
                            nickname,
                            address,
                            phoneNumber,
                            breed,
                            kind,
                            male,
                            age
                        )
                    }
                    onSaveChange(!save)
                },
                modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Text(if (!save) "Редактировать" else "Сохранить")
            }
        }
        buildVisitNote(
            visit,
            dates,
            onVisitChange = { visit = it },
            ownerWords,
            onOwnerWordsChange,
            commonFeeling,
            onCommonFeelingChange,
            temperature,
            onTemperatureChange,
            appetite,
            onAppetiteChange,
            vomit,
            onVomitChange,
            defication,
            onDeficationChange,
            urination,
            onUrinationChange,
            extra,
            onExtraChange,
            diagnosis,
            onDiagnosisChange,
            completed,
            onCompletedChange,
            recommendations,
            onRecommendationsChange,
            date,
            onDateChange,
            isPattern,
            onIsPatternChange,
            isEdit,
            onIsEditChange
        )
    }
}

@Composable
fun buildVisitNote(
    visit: List<String>, dates: List<String>, onVisitChange: (List<String>) -> Unit,
    ownerWords: String,
    onOwnerWordsChange: (String) -> Unit,
    commonFeeling: String,
    onCommonFeelingChange: (String) -> Unit,
    temperature: String,
    onTemperatureChange: (String) -> Unit,
    appetite: String,
    onAppetiteChange: (String) -> Unit,
    vomit: String,
    onVomitChange: (String) -> Unit,
    defication: String,
    onDeficationChange: (String) -> Unit,
    urination: String,
    onUrinationChange: (String) -> Unit,
    extra: String,
    onExtraChange: (String) -> Unit,
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    completed: String,
    onCompletedChange: (String) -> Unit,
    recommendations: String,
    onRecommendationsChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    isPattern: Boolean,
    onIsPatternChange: (Boolean) -> Unit,
    isEdit: Boolean,
    onIsEditChange: (Boolean) -> Unit
) {

    val dateNow = Date()
    val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm")
    val dateTrue =
        if (visit.isEmpty()) formatForDateNow.format(dateNow) else date
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
                    onIsPatternChange(true)
                    onIsEditChange(true)
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
                    onIsPatternChange(true)
                    onIsEditChange(false)
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
            isPattern,
            visit,
            isEdit,
            onIsPatternChange,
            ownerWords,
            onOwnerWordsChange,
            commonFeeling,
            onCommonFeelingChange,
            temperature,
            onTemperatureChange,
            appetite,
            onAppetiteChange,
            vomit,
            onVomitChange,
            defication,
            onDeficationChange,
            urination,
            onUrinationChange,
            extra,
            onExtraChange,
            diagnosis,
            onDiagnosisChange,
            completed,
            onCompletedChange,
            recommendations,
            onRecommendationsChange,
            dateTrue,
            onDateChange
        )
    }
}

@Composable
fun buildExamination(
    isPattern: Boolean,
    visit: List<String>,
    isEdit: Boolean,
    onPatternChange: (Boolean) -> Unit,
    ownerWords: String,
    onOwnerWordsChange: (String) -> Unit,
    commonFeeling: String,
    onCommonFeelingChange: (String) -> Unit,
    temperature: String,
    onTemperatureChange: (String) -> Unit,
    appetite: String,
    onAppetiteChange: (String) -> Unit,
    vomit: String,
    onVomitChange: (String) -> Unit,
    defication: String,
    onDeficationChange: (String) -> Unit,
    urination: String,
    onUrinationChange: (String) -> Unit,
    extra: String,
    onExtraChange: (String) -> Unit,
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    completed: String,
    onCompletedChange: (String) -> Unit,
    recommendations: String,
    onRecommendationsChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit
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

    if (visit.isNotEmpty() && !isEdit) {
        onOwnerWordsChange(visit[0])
        onCommonFeelingChange(visit[1])
        onTemperatureChange(visit[2])
        onAppetiteChange(visit[3])
        onVomitChange(visit[4])
        onDeficationChange(visit[5])
        onUrinationChange(visit[6])
        onExtraChange(visit[7])

        onDiagnosisChange(visit[8])
        onCompletedChange(visit[9])
        onRecommendationsChange(visit[10])

        onDateChange(DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
    }
    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 66.dp)
            .background(color = Color.White)
            .fillMaxSize()
    ) {

        if (!isPattern) {
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

            if (!isEdit) {
                onOwnerWordsChange("")
                onCommonFeelingChange("")
                onTemperatureChange("")
                onAppetiteChange("")
                onVomitChange("")
                onDeficationChange("")
                onUrinationChange("")
                onExtraChange("")

                onDiagnosisChange("")
                onCompletedChange("")
                onRecommendationsChange("")
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
                        onPatternChange(false)
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
                            0 -> ownerWords
                            1 -> commonFeeling
                            2 -> temperature
                            3 -> appetite
                            4 -> vomit
                            5 -> defication
                            6 -> urination
                            else -> extra
                        }

                        buildOneNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    0 -> onOwnerWordsChange(it)
                                    1 -> onCommonFeelingChange(it)
                                    2 -> onTemperatureChange(it)
                                    3 -> onAppetiteChange(it)
                                    4 -> onVomitChange(it)
                                    5 -> onDeficationChange(it)
                                    6 -> onUrinationChange(it)
                                }
                            },
                            labels[count],
                            count
                        )
                    }
                    items(3) { count ->
                        var currentData by remember { mutableStateOf("") }
                        currentData = when (count) {
                            0 -> diagnosis
                            1 -> completed
                            else -> recommendations
                        }
                        buildBiggerNote(
                            currentData,
                            onTextChange = { currentData = it },
                            labelsBigger[count],
                            next
                        )
                    }
                }
            } else {
                buildBiggerNote(completed, onTextChange = { onCompletedChange(it) }, "Выполнено в клинике: ", next)
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