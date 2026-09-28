package presentation.outpatient

import androidx.compose.foundation.*
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
import androidx.compose.ui.zIndex
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import data.Repository
import data.utils.Mail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.client.ClientInfoViewModel
import presentation.main.navcontroller.NavController
import presentation.main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@Composable
fun buildOutpatientCard(
    navController: NavController,
    shareDataState: ShareDataState,
    tabViewModel: TabViewModel,
    petInfoViewModel: PetInfoViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    outpatientViewModel: OutpatientViewModel
) {

    if (outpatientViewModel.manualVaccineAdder()) {
        manualVaccineAdder(outpatientViewModel, illnessHistoryViewModel, clientInfoViewModel, petInfoViewModel)
    }
    var firstNote by remember { mutableStateOf(false) }

    val labels2 = listOf("Кличка", "Вид", "Порода", "Пол", "Возраст")


//    CoroutineScope(Dispatchers.Default).launch {
//    if (shareDataState.shareData().second == "") illnessHistoryState.updateVisit(info.first.second)
//    StateWrapper().fillIllnessHistoryState(illnessHistoryState, illnessHistoryState.visit())
    val id = illnessHistoryViewModel.id()
    val note = illnessHistoryViewModel.note()
    val dates = Repository.getVisitDates(id)
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
                            0 -> petInfoViewModel.nickname()
                            1 -> petInfoViewModel.kind()
                            2 -> petInfoViewModel.breed()
                            3 -> petInfoViewModel.male()
                            else -> petInfoViewModel.age()
                        },
                        onValueChange = {
                            when (count) {
                                0 -> {
                                    petInfoViewModel.updateNickname(it)
                                }

                                1 -> {
                                    petInfoViewModel.updateKind(it)
                                }

                                2 -> petInfoViewModel.updateBreed(it)
                                3 -> petInfoViewModel.updateMale(it)
                                else -> petInfoViewModel.updateAge(it)
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
                        readOnly = !petInfoViewModel.save()
                    )
                }
            }
            item {
                Text(
                    "Сведения об активных вакцинах:",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    if (petInfoViewModel.vac() != "") petInfoViewModel.vac() else "Пусто",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp).width(350.dp)
                )
            }
            if (petInfoViewModel.vac() != "") {
                item {
                    Text(
                        "Дата вакцинирования: ${petInfoViewModel.vacDate()}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }
            } else {
                item {
                    Button(
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = {
                            outpatientViewModel.updateManualVaccineAdder(true)
                            outpatientViewModel.updateVaccineDate("")
                            outpatientViewModel.updateVaccine("")
                        }
                    ) {
                        Text("Добавить дату вакцинации вручную")
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight().width(380.dp)
        ) {
            Button(
                onClick = {
                    if (petInfoViewModel.save()) {
                        Repository.setPetInfo(
                            petInfoViewModel.nickname(),
                            petInfoViewModel.breed(),
                            petInfoViewModel.kind(),
                            petInfoViewModel.male(),
                            petInfoViewModel.age(),
                            id
                        )
                    }
                    petInfoViewModel.updateSave(!petInfoViewModel.save())
                },
                modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Text(if (!petInfoViewModel.save()) "Редактировать" else "Сохранить")
            }
        }
        if (illnessHistoryViewModel.loading()) {
            buildLoading()
        } else {
            buildVisitNote(
                dates,
                illnessHistoryViewModel,
                shareDataState,
                illnessHistoryViewModel.visit(),
                petInfoViewModel,
                clientInfoViewModel,
                outpatientViewModel
            )
            if (illnessHistoryViewModel.addingNewService() || illnessHistoryViewModel.editingService()) {
                newServiceAdder(illnessHistoryViewModel)
            }
        }
    }

}

@Composable
fun manualVaccineAdder(
    outpatientViewModel: OutpatientViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    petInfoViewModel: PetInfoViewModel
) {
    Card(
        modifier = Modifier.padding(start = 450.dp, top = 100.dp).zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(176, 224, 230))
                .width(700.dp)
                .height(600.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Row {
                Text(
                    text = "Введите данные о вакцинации: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = 300.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        outpatientViewModel.updateManualVaccineAdder(false)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }

            val labels = listOf("Название вакцины", "Дата вакцинирования")
            val hints = listOf("Название вакцины", "дд.мм.гггг")

            LazyColumn(
                modifier = Modifier
                    .padding(top = 80.dp, start = 50.dp, end = 8.dp)
                    .background(color = Color(64, 224, 208), shape = RoundedCornerShape(16.dp))
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        Text(
                            labels[0] + ": ",
                            modifier = Modifier
                                .width(200.dp)
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        val serviceFindRussian = Regex("[[а-яА-Я]*[a-zA-Z]* *,*/*(*)*:*\\d*\\+*]*")
                        val serviceFindEnglish = Regex("[[a-zA-Z]* *,*/*(*)*:*\\d*\\+*]*")
                        TextField(
                            value = outpatientViewModel.vaccine(),
                            placeholder = {
                                Text(hints[0])
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {
                                if (serviceFindEnglish.matches(it) || serviceFindRussian.matches(it)) {
                                    outpatientViewModel.updateVaccine(it)
                                    illnessHistoryViewModel.updateSearchingService(true)
                                    if (it != "" && !it.contains('(') && !it.contains(')')) {
                                        illnessHistoryViewModel.updateServicesList(
                                            Repository.getServiceByFirstLetter(
                                                it,
                                                true
                                            )
                                        )
                                    } else {
                                        illnessHistoryViewModel.updateSearchingService(false)
                                    }
                                }
                            },
                            modifier = Modifier.width(400.dp),
                        )

                        DropdownMenu(
                            focusable = false,
                            expanded = illnessHistoryViewModel.searchingService(),
                            onDismissRequest = {
                                illnessHistoryViewModel.updateSearchingService(false)
                            },
                            modifier = Modifier.background(color = Color(250, 240, 230))
                        ) {
                            Column(
                                modifier = Modifier.width(400.dp)
                            ) {
                                illnessHistoryViewModel.servicesList().forEach {
                                    Text(
                                        text = it,
                                        textAlign = TextAlign.Start,
                                        fontSize = 18.sp,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 4.dp, horizontal = 4.dp)
                                            .clickable {
                                                outpatientViewModel.updateVaccine(it)
                                                illnessHistoryViewModel.updateSearchingService(false)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        Text(
                            labels[1] + ": ",
                            modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = outpatientViewModel.vaccineDate(),
                            onValueChange = {
                                outpatientViewModel.updateVaccineDate(it)
                            },
                            modifier = Modifier
                                .width(400.dp),
                            placeholder = {
                                Text(hints[1])
                            },
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.width(700.dp)
            ) {
                Button(
                    onClick = {
                        val dateRegex = Regex("\\d{2}\\.\\d{2}\\.\\d{4}")
                        if (outpatientViewModel.vaccineDate() != "" && outpatientViewModel.vaccine() != "" && dateRegex.matches(
                                outpatientViewModel.vaccineDate()
                            )
                        ) {
                            val client =
                                clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                            Repository.setVacInfo(
                                info = outpatientViewModel.vaccine(),
                                date = outpatientViewModel.vaccineDate() + " 00:00:00",
                                petId = illnessHistoryViewModel.id(),
                                client = client,
                                pet = petInfoViewModel.nickname(),
                                visitId = illnessHistoryViewModel.visitId()
                            )
                            petInfoViewModel.updateVac(outpatientViewModel.vaccine())
                            petInfoViewModel.updateVacDate(outpatientViewModel.vaccineDate())
                            outpatientViewModel.updateManualVaccineAdder(false)
                        } else {
                            outpatientViewModel.updateVaccineDate("")
                        }
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

@Composable
fun buildLoading() {
    Box(
        modifier = Modifier.padding(start = 380.dp, top = 300.dp).fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Загрузка...",
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun buildVisitNote(
    dates: List<Pair<String, String>>,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    shareDataState: ShareDataState,
    visit: List<String>,
    petInfoViewModel: PetInfoViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    outpatientViewModel: OutpatientViewModel
) {
    val scope = rememberCoroutineScope(
        getContext = {
            Dispatchers.Default
        }
    )
    val dateNow = Date()
    val formatForDateNow = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    var enablePrevious by remember { mutableStateOf(true) }
    var enableNext by remember { mutableStateOf(true) }
    var visitDate =
        ""
    if (illnessHistoryViewModel.visit().size > 1 && illnessHistoryViewModel.getIsPattern()) {
        visitDate = Repository.readableDateFormat(visit[14] + " " + visit[15])
        illnessHistoryViewModel.updateDate(visitDate)
    }
    if (!illnessHistoryViewModel.getIsNewVisitInfo()) {
        val info = Repository.getCompleted(illnessHistoryViewModel.visitId())
        val countLines = info.second.first
        val completedIds = info.second.second
        val tempCompleted = info.first
        illnessHistoryViewModel.updateCompletedPair(Repository.parseCompleted(tempCompleted, countLines))
        illnessHistoryViewModel.updateCountLines(countLines)
        illnessHistoryViewModel.updateCompletedIds(completedIds)

        val price = Repository.getPrice(
            illnessHistoryViewModel.completedPair().first,
            illnessHistoryViewModel.completedPair().second[0],
            illnessHistoryViewModel.completedPair().second[1],
            illnessHistoryViewModel.visitId(),
            false,
            illnessHistoryViewModel.price(),
            illnessHistoryViewModel.completedPair().second[2]
        )
        illnessHistoryViewModel.updatePrice(price)
    }
    Box(
        modifier = Modifier
            .padding(start = 380.dp)
            .background(color = Color.LightGray)
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
                    illnessHistoryViewModel.updateIsPattern(false)
                    illnessHistoryViewModel.updateIsNewVisitInfo(false)
                    StateWrapper().fillIllnessHistoryState(illnessHistoryViewModel, visit)
                    illnessHistoryViewModel.updateDate(visitDate)
                },
                modifier = Modifier.padding(top = 16.dp, start = 16.dp).size(32.dp),
                enabled = !illnessHistoryViewModel.getIsNew() && illnessHistoryViewModel.getIsPattern(),
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                enabled = illnessHistoryViewModel.getIsPattern(),
                onClick = {
                    illnessHistoryViewModel.updateIsPattern(false)
                    illnessHistoryViewModel.updateIsNewVisitInfo(true)
                    StateWrapper().clearIllnessHistoryState(illnessHistoryViewModel)
                    illnessHistoryViewModel.updateDate(formatForDateNow.format(dateNow))
                    illnessHistoryViewModel.updateCompletedIds(listOf())
                    illnessHistoryViewModel.updateCompletedPair(
                        Pair(
                            listOf("Услуга"),
                            listOf(listOf(listOf("Препараты")), listOf(listOf("Количество")), listOf(listOf("Своё")))
                        )
                    )
                    illnessHistoryViewModel.updateOwnerDrugs("false")
                    illnessHistoryViewModel.updateCountLines(0)
                    illnessHistoryViewModel.updatePrice(0)
                },
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp).size(32.dp)
            ) {
                Icon(
                    painter = painterResource("/add.svg"),
                    contentDescription = "Add",
                    Modifier.size(32.dp)
                )
            }
            if (!illnessHistoryViewModel.getIsPattern()) {
                IconButton(
                    onClick = {
                        Repository.setVisitInfo(
                            id = illnessHistoryViewModel.visitId(),
                            isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                            petId = illnessHistoryViewModel.id(),
                            date = illnessHistoryViewModel.date(),
                            sum = illnessHistoryViewModel.price(),
                            ownerWords = illnessHistoryViewModel.ownerWords(),
                            temperature = illnessHistoryViewModel.temperature(),
                            extra = illnessHistoryViewModel.extra(),
                            diagnosis = illnessHistoryViewModel.diagnosis(),
                            completed = illnessHistoryViewModel.completed(),
                            recommendations = illnessHistoryViewModel.recommendations(),
                            illnessHistoryViewModel = illnessHistoryViewModel,
                            complete = "false"
                        )
                        illnessHistoryViewModel.completedPair().first.forEach {
                            if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                val client =
                                    clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                                Repository.setVacInfo(
                                    it,
                                    illnessHistoryViewModel.id(),
                                    illnessHistoryViewModel.date(),
                                    client,
                                    petInfoViewModel.nickname(),
                                    illnessHistoryViewModel.visitId()
                                )
                            }
                        }
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryViewModel.updateLoading(true)
                            val info = withContext(Dispatchers.IO) {
                                Repository.getInfoByPetId(illnessHistoryViewModel.id() to "${illnessHistoryViewModel.date()}:00")
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryViewModel, info.first.second)
                            illnessHistoryViewModel.updateIsPattern(true)
                            illnessHistoryViewModel.updateIsNewVisitInfo(false)
                            StateWrapper().fillPetInfoState(petInfoViewModel, info.first.first)
                            illnessHistoryViewModel.updateLoading(false)
                        }
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
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0, 191, 255)),
                enabled = illnessHistoryViewModel.getIsPattern() && enablePrevious,
                onClick = {
                    val currentDate = illnessHistoryViewModel.date()
                    val day = currentDate.split(" ")[0]
                    val time = currentDate.split(" ")[1]
                    val index = dates.indexOf(day to time)
                    if (index > 0) {
                        enableNext = true
                        val newDate = dates[index - 1]
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryViewModel.updateLoading(true)
                            val newVisitInfo = withContext(Dispatchers.IO) {
                                Repository.getInfoByPetId(illnessHistoryViewModel.id() to newDate.first + " " + newDate.second)
                            }
                            illnessHistoryViewModel.updateVisit(newVisitInfo.first.second)
                            illnessHistoryViewModel.updateVisitId(newVisitInfo.second.second)
                            illnessHistoryViewModel.updateLoading(false)
                        }
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
                    illnessHistoryViewModel.date(),
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
                            enabled = illnessHistoryViewModel.getIsPattern(),
                            onClick = {
                                illnessHistoryViewModel.updateLoading(true)
                                scope.launch {
                                    val newVisitInfo =
                                        Repository.getInfoByPetId(illnessHistoryViewModel.id() to tempDate.first + " " + tempDate.second)
                                    illnessHistoryViewModel.updateVisit(newVisitInfo.first.second)
                                    illnessHistoryViewModel.updateVisitId(newVisitInfo.second.second)
                                    expanded2 = false
                                    enableNext = true
                                    enablePrevious = true
                                    illnessHistoryViewModel.updateLoading(false)
                                }
                            }
                        ) {
                            Text(Repository.readableDateFormat(tempDate.first + " " + tempDate.second))
                        }
                    }
                }
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0, 191, 255)),
                enabled = illnessHistoryViewModel.getIsPattern() && enableNext,
                onClick = {
                    val currentDate = illnessHistoryViewModel.date()
                    val day = currentDate.split(" ")[0]
                    val time = currentDate.split(" ")[1]
                    val index = dates.indexOf(day to time)
                    if (index < dates.size - 1) {
                        enablePrevious = true
                        val newDate = dates[index + 1]
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryViewModel.updateLoading(true)
                            val newVisitInfo = withContext(Dispatchers.IO) {
                                Repository.getInfoByPetId(illnessHistoryViewModel.id() to newDate.first + " " + newDate.second)
                            }
                            illnessHistoryViewModel.updateVisit(newVisitInfo.first.second)
                            illnessHistoryViewModel.updateVisitId(newVisitInfo.second.second)
                            illnessHistoryViewModel.updateLoading(false)
                        }
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
            illnessHistoryViewModel,
            illnessHistoryViewModel.visit(),
            petInfoViewModel,
            clientInfoViewModel,
            outpatientViewModel
        )
    }
}

@Composable
fun buildExamination(
    illnessHistoryViewModel: IllnessHistoryViewModel,
    visit: List<String>,
    petInfoViewModel: PetInfoViewModel,
    clientInfoViewModel: ClientInfoViewModel,
    outpatientViewModel: OutpatientViewModel
) {
    val labels = listOf(
        "Врачи на приеме: ",
        "Со слов владельца: ",
        "Общее состояние: ",
        "Вес",
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
//        illnessHistoryState.updateDate(data.DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
//    }
    var active by remember { mutableStateOf(false) }
    priceDialog(
        active = active,
        onActiveChange = { active = it },
        outpatientViewModel = outpatientViewModel,
        illnessHistoryViewModel,
        visit,
        clientInfoViewModel,
        petInfoViewModel
    )

    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 66.dp)
            .background(color = Color.White)
            .fillMaxSize()
    ) {

        if (illnessHistoryViewModel.getIsPattern()) {
            if (visit.size != 1 && !illnessHistoryViewModel.getIsNew()) {
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
                            for (it in 0..9) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(all = 8.dp)
                                ) {
                                    Text(
                                        (labels + labelsBigger)[it],
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(200.dp)
                                    )
                                    Text(visit[it + 1], fontSize = 18.sp, modifier = Modifier.padding(start = 50.dp))
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
                            if (it != 1) {
                                Text(visit[it + 11], fontSize = 18.sp, modifier = Modifier.padding(all = 8.dp))
                            } else {
                                val tempMeasures = mutableListOf<MutableList<String>>()
                                illnessHistoryViewModel.completedPair().second[0].forEachIndexed { index, drugs ->
                                    tempMeasures.add(mutableListOf())
                                    drugs.forEach { drug ->
                                        tempMeasures[index].add(Repository.getMeasure(drug))
                                    }
                                }
                                illnessHistoryViewModel.fillMeasureComplex(tempMeasures)
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (row in 1..illnessHistoryViewModel.countLines() + 1) {
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = illnessHistoryViewModel.completedPair().first[row - 1],
                                                fontSize = 18.sp,
                                                modifier = Modifier.width(400.dp),
                                                textAlign = TextAlign.Center
                                            )

                                            Column(
                                                modifier = Modifier.width(400.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            ) {
                                                for (i in 1..illnessHistoryViewModel.completedPair().second[0][row - 1].size) {
                                                    Text(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        text = illnessHistoryViewModel.completedPair().second[0][row - 1][i - 1],
                                                        fontSize = 18.sp,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }

                                            Column(
                                                modifier = Modifier.width(400.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            ) {
                                                for (i in 1..illnessHistoryViewModel.completedPair().second[1][row - 1].size) {
                                                    Text(
                                                        text = illnessHistoryViewModel.completedPair().second[1][row - 1][i - 1] + " " + illnessHistoryViewModel.measureComplex()[row - 1][i - 1],
                                                        fontSize = 18.sp,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Стоимость: ${illnessHistoryViewModel.price()} руб",
                                        fontSize = 20.sp,
                                        fontStyle = FontStyle.Italic,
                                        modifier = Modifier.padding(start = 50.dp, top = 32.dp, bottom = 8.dp)
                                    )
                                }
                            }
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

            if (!illnessHistoryViewModel.next()) {
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
                    .wrapContentWidth(if (illnessHistoryViewModel.next()) Alignment.Start else Alignment.End)
            ) {
                Button(
                    onClick = {
                        illnessHistoryViewModel.updateNext(!illnessHistoryViewModel.next())
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0, 191, 255)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    if (illnessHistoryViewModel.next()) {
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
                        illnessHistoryViewModel.updateIsPattern(true)
                        illnessHistoryViewModel.updateIsNewVisitInfo(false)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0, 191, 255)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            }
            if (!illnessHistoryViewModel.next()) {
                LazyColumn(
                    modifier = Modifier.padding(top = 60.dp).wrapContentWidth(Alignment.CenterHorizontally)
                ) {

                    items(10) { count ->
                        var currentData = when (count) {
                            1 -> illnessHistoryViewModel.ownerWords()
                            3 -> illnessHistoryViewModel.weight()
                            4 -> illnessHistoryViewModel.temperature()
                            9 -> illnessHistoryViewModel.extra()
                            else -> ""
                        }

                        buildOneNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    1 -> illnessHistoryViewModel.updateOwnerWords(it)
                                    3 -> illnessHistoryViewModel.updateWeight(it)
                                    4 -> illnessHistoryViewModel.updateTemperature(it)
                                    9 -> illnessHistoryViewModel.updateExtra(it)
                                    else -> illnessHistoryViewModel.updateDrug(it)
                                }
                            },
                            labels[count],
                            count,
                            illnessHistoryViewModel
                        )
                    }
                    items(3) { count ->
                        var currentData = when (count) {
                            0 -> illnessHistoryViewModel.diagnosis()
                            1 -> illnessHistoryViewModel.completed()
                            else -> illnessHistoryViewModel.recommendations()
                        }
                        if (count == 1) illnessHistoryViewModel.updateIsCompleted(true) else illnessHistoryViewModel.updateIsCompleted(
                            false
                        )
                        buildBiggerNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    0 -> illnessHistoryViewModel.updateDiagnosis(it)
                                    1 -> illnessHistoryViewModel.updateCompleted(it)
                                    else -> illnessHistoryViewModel.updateRecommendations(it)
                                }
                            },
                            labelsBigger[count],
                            illnessHistoryViewModel.next(),
                            illnessHistoryViewModel.getIsCompleted(),
                            visit,
                            clientInfoViewModel,
                            petInfoViewModel,
                            illnessHistoryViewModel
                        )
                    }

                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                            onClick = {
                                Repository.setVisitInfo(
                                    id = illnessHistoryViewModel.visitId(),
                                    isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                                    petId = illnessHistoryViewModel.id(),
                                    date = illnessHistoryViewModel.date(),
                                    sum = illnessHistoryViewModel.price(),
                                    ownerWords = illnessHistoryViewModel.ownerWords(),
                                    temperature = illnessHistoryViewModel.temperature(),
                                    extra = illnessHistoryViewModel.extra(),
                                    diagnosis = illnessHistoryViewModel.diagnosis(),
                                    completed = illnessHistoryViewModel.completed(),
                                    recommendations = illnessHistoryViewModel.recommendations(),
                                    illnessHistoryViewModel = illnessHistoryViewModel,
                                    complete = "true"
                                )
                                illnessHistoryViewModel.completedPair().first.forEach {
                                    if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                        val client =
                                            clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                                        Repository.setVacInfo(
                                            it,
                                            illnessHistoryViewModel.id(),
                                            illnessHistoryViewModel.date(),
                                            client,
                                            petInfoViewModel.nickname(),
                                            illnessHistoryViewModel.visitId()
                                        )
                                    }
                                }
                                active = true
                            }
                        ) {
                            Text(
                                text = "Завершить прием",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            } else {
                Column {
                    buildOneNote(
                        text = "",
                        onTextChange = { illnessHistoryViewModel.updateWeight(it) },
                        labels[0],
                        count = 0,
                        illnessHistoryViewModel
                    )
                    illnessHistoryViewModel.updateIsCompleted(true)
                    buildBiggerNote(
                        illnessHistoryViewModel.completed(),
                        onTextChange = { illnessHistoryViewModel.updateCompleted(it) },
                        "Выполнено в клинике: ",
                        illnessHistoryViewModel.next(),
                        illnessHistoryViewModel.getIsCompleted(),
                        visit,
                        clientInfoViewModel,
                        petInfoViewModel,
                        illnessHistoryViewModel
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                        onClick = {
                            Repository.setVisitInfo(
                                id = illnessHistoryViewModel.visitId(),
                                isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                                petId = illnessHistoryViewModel.id(),
                                date = illnessHistoryViewModel.date(),
                                sum = illnessHistoryViewModel.price(),
                                ownerWords = illnessHistoryViewModel.ownerWords(),
                                temperature = illnessHistoryViewModel.temperature(),
                                extra = illnessHistoryViewModel.extra(),
                                diagnosis = illnessHistoryViewModel.diagnosis(),
                                completed = illnessHistoryViewModel.completed(),
                                recommendations = illnessHistoryViewModel.recommendations(),
                                illnessHistoryViewModel = illnessHistoryViewModel,
                                complete = "true"
                            )
                            illnessHistoryViewModel.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                                    Repository.setVacInfo(
                                        it,
                                        illnessHistoryViewModel.id(),
                                        illnessHistoryViewModel.date(),
                                        client,
                                        petInfoViewModel.nickname(),
                                        illnessHistoryViewModel.visitId()
                                    )
                                }
                            }
                            active = true
                        }
                    ) {
                        Text(
                            text = "Завершить прием",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun priceDialog(
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    outpatientViewModel: OutpatientViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel,
    visit: List<String>,
    clientInfoViewModel: ClientInfoViewModel,
    petInfoViewModel: PetInfoViewModel
) {
    if (active) {
        AlertDialog(
            onDismissRequest = {
                onActiveChange(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onActiveChange(false)
                        illnessHistoryViewModel.updateIsPattern(true)
                        illnessHistoryViewModel.updateIsNewVisitInfo(false)
                        CoroutineScope(Dispatchers.Default).launch {
                            Repository.setVisitInfo(
                                id = illnessHistoryViewModel.visitId(),
                                isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                                petId = illnessHistoryViewModel.id(),
                                date = illnessHistoryViewModel.date(),
                                sum = illnessHistoryViewModel.price(),
                                ownerWords = illnessHistoryViewModel.ownerWords(),
                                temperature = illnessHistoryViewModel.temperature(),
                                extra = illnessHistoryViewModel.extra(),
                                diagnosis = illnessHistoryViewModel.diagnosis(),
                                completed = illnessHistoryViewModel.completed(),
                                recommendations = illnessHistoryViewModel.recommendations(),
                                illnessHistoryViewModel = illnessHistoryViewModel,
                                complete = "true"
                            )
                            val info =
                                Repository.getInfoByPetId(illnessHistoryViewModel.id() to "${illnessHistoryViewModel.date()}:00")
                            illnessHistoryViewModel.updateVisit(info.first.second)
                            illnessHistoryViewModel.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                                    Repository.setVacInfo(
                                        it,
                                        illnessHistoryViewModel.id(),
                                        illnessHistoryViewModel.date(),
                                        client,
                                        petInfoViewModel.nickname(),
                                        illnessHistoryViewModel.visitId()
                                    )
                                }
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryViewModel, info.first.second)
                            StateWrapper().fillPetInfoState(petInfoViewModel, info.first.first)
                            val doc = com.itextpdf.text.Document()
                            val bf =
                                BaseFont.createFont(
                                    "C:\\Windows\\Fonts\\Arial.ttf",
                                    BaseFont.IDENTITY_H,
                                    BaseFont.EMBEDDED
                                )
                            val font = Font(bf, 14f, Font.NORMAL)
                            try {
                                val writer = PdfWriter.getInstance(
                                    doc,
                                    FileOutputStream(
                                        "C://new//${
                                            illnessHistoryViewModel.visit()[14].split(" ")[0]
                                        }_${petInfoViewModel.nickname()}.pdf"
                                    )
                                )
                                doc.open()
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиника: НовоВет         ${illnessHistoryViewModel.visit()[14]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиент: ${clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Питомец: ${petInfoViewModel.kind() + " " + petInfoViewModel.nickname()}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Осмотр", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Врачи на приеме: ${illnessHistoryViewModel.visit()[1]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Со слов владельца: ${illnessHistoryViewModel.visit()[2]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Общее состояние: ${illnessHistoryViewModel.visit()[3]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph("Вес: ${illnessHistoryViewModel.visit()[4]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Температура: ${illnessHistoryViewModel.visit()[5]}°C",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph("Аппетит: ${illnessHistoryViewModel.visit()[6]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Рвота: ${illnessHistoryViewModel.visit()[7]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дефекация: ${illnessHistoryViewModel.visit()[8]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Мочеиспускание: ${illnessHistoryViewModel.visit()[9]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дополнительная информация: ${illnessHistoryViewModel.visit()[10]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Предварительный диагноз: ${illnessHistoryViewModel.visit()[11]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Выполнено в клинике:", font))
                                val completed = illnessHistoryViewModel.completedPair()

                                val services = completed.first
                                val drugs = completed.second[0]
                                val amounts = completed.second[1]

                                drugs.forEachIndexed { index, drugList ->

                                    val drugsText = drugList.mapIndexed { drugIndex, drugName ->
                                        val amount = amounts[index][drugIndex]
                                        val measure = Repository.getMeasure(drugName)

                                        "$drugName — $amount $measure"
                                    }.joinToString(", ")

                                    val text = "Услуга: ${services[index]}. Препараты: $drugsText"

                                    doc.add(
                                        Paragraph(text, font).apply {
                                            spacingAfter = 5f
                                        }
                                    )
                                }
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Рекомендации: ${illnessHistoryViewModel.visit()[13]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Стоимость: ${illnessHistoryViewModel.price()}",
                                        font
                                    )
                                )
                                doc.close()
                                writer.close()
                            } catch (e: DocumentException) {
                                e.printStackTrace()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                            val message = MimeMessage(Mail().session)
                            message.setFrom(InternetAddress("novovetsend@gmail.com"))
                            var text1 = ""
                            var sum = 0
                            illnessHistoryViewModel.completedPair().first.forEach {
                                if (it != "Услуга" && it != "") {
                                    val price =
                                        Repository.getRealServicePrice(it, illnessHistoryViewModel.visitId())
                                    sum += price.toInt()
                                    text1 += "$it - $price\n"
                                }
                            }
                            text1 += "Препараты - ${illnessHistoryViewModel.price() - sum}"
                            message.subject = "${illnessHistoryViewModel.visit()[14]}_${petInfoViewModel.nickname()}"
                            val textPart = MimeBodyPart()
                            textPart.setText(text1)
                            val attachmentPart = MimeBodyPart()
                            attachmentPart.attachFile(
                                File("C://new//${illnessHistoryViewModel.visit()[14]}_${petInfoViewModel.nickname()}.pdf")
                            )
                            val multipart = MimeMultipart()
                            multipart.addBodyPart(textPart)
                            multipart.addBodyPart(attachmentPart)
                            message.setContent(multipart)
                            message.addRecipient(
                                Message.RecipientType.TO,
                                InternetAddress("novovetget@gmail.com")
                            )
                            message.sentDate = Date()
                            val transport = Mail().session.getTransport("smtp")
                            transport.connect()
                            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO))
                        }
                    }
                ) {
                    Text("Да")
                }
            },
            title = {
                Text(text = "Добавить цену для печати?")
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onActiveChange(false)
                        illnessHistoryViewModel.updateIsPattern(true)
                        illnessHistoryViewModel.updateIsNewVisitInfo(false)
                        CoroutineScope(Dispatchers.Default).launch {
                            Repository.setVisitInfo(
                                id = illnessHistoryViewModel.visitId(),
                                isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                                petId = illnessHistoryViewModel.id(),
                                date = illnessHistoryViewModel.date(),
                                sum = illnessHistoryViewModel.price(),
                                ownerWords = illnessHistoryViewModel.ownerWords(),
                                temperature = illnessHistoryViewModel.temperature(),
                                extra = illnessHistoryViewModel.extra(),
                                diagnosis = illnessHistoryViewModel.diagnosis(),
                                completed = illnessHistoryViewModel.completed(),
                                recommendations = illnessHistoryViewModel.recommendations(),
                                illnessHistoryViewModel = illnessHistoryViewModel,
                                complete = "true"
                            )
                            val info =
                                Repository.getInfoByPetId(illnessHistoryViewModel.id() to "${illnessHistoryViewModel.date()}:00")
                            illnessHistoryViewModel.updateVisit(info.first.second)
                            illnessHistoryViewModel.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()
                                    Repository.setVacInfo(
                                        it,
                                        illnessHistoryViewModel.id(),
                                        illnessHistoryViewModel.date(),
                                        client,
                                        petInfoViewModel.nickname(),
                                        illnessHistoryViewModel.visitId()
                                    )
                                }
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryViewModel, info.first.second)
                            StateWrapper().fillPetInfoState(petInfoViewModel, info.first.first)
                            val doc = com.itextpdf.text.Document()
                            val bf =
                                BaseFont.createFont(
                                    /*"C:\\Windows\\Fonts\\Arial.ttf"*/
                                    "C:\\Windows\\Fonts\\Arial.ttf",
                                    BaseFont.IDENTITY_H,
                                    BaseFont.EMBEDDED
                                )
                            val font = Font(bf, 14f, Font.NORMAL)
                            try {
                                val writer = PdfWriter.getInstance(
                                    doc,
                                    FileOutputStream(
                                        /*C://new// */
                                        "C://new//${
                                            illnessHistoryViewModel.visit()[14].split(" ")[0]
                                        }_${petInfoViewModel.nickname()}.pdf"
                                    )
                                )
                                doc.open()
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиника: НовоВет         ${illnessHistoryViewModel.visit()[14]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиент: ${clientInfoViewModel.secondName() + " " + clientInfoViewModel.firstName() + " " + clientInfoViewModel.lastName()}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Питомец: ${petInfoViewModel.kind() + " " + petInfoViewModel.nickname()}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Осмотр", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Врачи на приеме: ${illnessHistoryViewModel.visit()[1]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Со слов владельца: ${illnessHistoryViewModel.visit()[2]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Общее состояние: ${illnessHistoryViewModel.visit()[3]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph("Вес: ${illnessHistoryViewModel.visit()[4]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Температура: ${illnessHistoryViewModel.visit()[5]}°C",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph("Аппетит: ${illnessHistoryViewModel.visit()[6]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Рвота: ${illnessHistoryViewModel.visit()[7]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дефекация: ${illnessHistoryViewModel.visit()[8]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Мочеиспускание: ${illnessHistoryViewModel.visit()[9]}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дополнительная информация: ${illnessHistoryViewModel.visit()[10]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Предварительный диагноз: ${illnessHistoryViewModel.visit()[11]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Выполнено в клинике:", font))
                                val completed = illnessHistoryViewModel.completedPair()

                                val services = completed.first
                                val drugs = completed.second[0]
                                val amounts = completed.second[1]

                                drugs.forEachIndexed { index, drugList ->

                                    val drugsText = drugList.mapIndexed { drugIndex, drugName ->
                                        val amount = amounts[index][drugIndex]
                                        val measure = Repository.getMeasure(drugName)

                                        "$drugName — $amount $measure"
                                    }.joinToString(", ")

                                    val text = "Услуга: ${services[index]}. Препараты: $drugsText"

                                    doc.add(
                                        Paragraph(text, font).apply {
                                            spacingAfter = 5f
                                        }
                                    )
                                }
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Рекомендации: ${illnessHistoryViewModel.visit()[13]}",
                                        font
                                    )
                                )
                                doc.close()
                                writer.close()
                            } catch (e: DocumentException) {
                                e.printStackTrace()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                            val message = MimeMessage(Mail().session)
                            /*novovetget@gmail.com*/
                            message.setFrom(InternetAddress("novovetdsend@gmail.com"))
                            var text1 = ""
                            var sum = 0
                            illnessHistoryViewModel.completedPair().first.forEach {
                                if (it != "Услуга" && it != "") {
                                    val price =
                                        Repository.getRealServicePrice(it, illnessHistoryViewModel.visitId())
                                    sum += price.toInt()
                                    text1 += "$it - $price\n"
                                }
                            }
                            text1 += "Препараты - ${illnessHistoryViewModel.price() - sum}"
                            message.subject = "${visit[14]}_${petInfoViewModel.nickname()}"
                            val textPart = MimeBodyPart()
                            textPart.setText(text1)
                            val attachmentPart = MimeBodyPart()
                            attachmentPart.attachFile(
                                /*C://new// */
                                File("C://new//${illnessHistoryViewModel.visit()[14]}_${petInfoViewModel.nickname()}.pdf")
                            )
                            val multipart = MimeMultipart()
                            multipart.addBodyPart(textPart)
                            multipart.addBodyPart(attachmentPart)
                            message.setContent(multipart)
                            message.addRecipient(
                                Message.RecipientType.TO,
                                InternetAddress("novovetget@gmail.com")
                            )
                            message.sentDate = Date()
                            val transport = Mail().session.getTransport("smtp")
                            transport.connect()
                            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO))
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
fun buildBiggerNote(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    next: Boolean,
    isCompleted: Boolean,
    visit: List<String>,
    clientInfoViewModel: ClientInfoViewModel,
    petInfoViewModel: PetInfoViewModel,
    illnessHistoryViewModel: IllnessHistoryViewModel
) {
    Box(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
            .fillMaxWidth()
            .background(color = Color(224, 224, 224), shape = RoundedCornerShape(8.dp))
            .height(if (!isCompleted) 100.dp else 500.dp)
    ) {
        Text(
            label,
            fontSize = 18.sp,
            modifier = Modifier.padding(all = 8.dp)
        )
        if (!isCompleted) {
            TextField(
                value = text,
                onValueChange = { onTextChange(it) },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(250, 240, 230)),
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 8.dp, end = 8.dp),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                placeholder = { Text("Введите текст здесь") }
            )
        } else {
            if (!illnessHistoryViewModel.getIsNewVisitInfo()) {
                val info = Repository.getCompleted(illnessHistoryViewModel.visitId())
                val countLines = info.second.first
                val completedIds = info.second.second
                val tempCompleted = info.first
                illnessHistoryViewModel.updateCompletedPair(Repository.parseCompleted(tempCompleted, countLines))
                illnessHistoryViewModel.updateCountLines(countLines)
                illnessHistoryViewModel.updateCompletedIds(completedIds)
                val tempMeasures = mutableListOf<MutableList<String>>()
                illnessHistoryViewModel.completedPair().second[0].forEachIndexed { index, it ->
                    tempMeasures.add(mutableListOf())
                    it.forEach {
                        tempMeasures[index].add(Repository.getMeasure(it))
                    }
                }
                illnessHistoryViewModel.fillMeasureComplex(tempMeasures)
            }
            LazyColumn(
                modifier = Modifier.padding(top = 40.dp).fillMaxWidth()
            ) {
                items(count = illnessHistoryViewModel.countLines() + 1) { row ->
                    Row(
                        modifier = if (row != 0 && !illnessHistoryViewModel.addingNewService() && !illnessHistoryViewModel.editingService()) Modifier.fillMaxWidth()
                            .background(color = Color(250, 240, 230))
                            .clickable {
                                illnessHistoryViewModel.updateService(illnessHistoryViewModel.completedPair().first[row])
                                illnessHistoryViewModel.clearDrugs(illnessHistoryViewModel.completedPair().second[0][row])
                                illnessHistoryViewModel.clearAmounts(illnessHistoryViewModel.completedPair().second[1][row])
                                illnessHistoryViewModel.clearOwnerDrugs(illnessHistoryViewModel.completedPair().second[2][row])
                                illnessHistoryViewModel.updateDrugCount(illnessHistoryViewModel.drugs().size)
                                illnessHistoryViewModel.updateCompletedId(illnessHistoryViewModel.completedIds()[row - 1])
                                illnessHistoryViewModel.updatePriceTemplate(Repository.getServicePrice(illnessHistoryViewModel.service()))
                                illnessHistoryViewModel.updateServicePrice(
                                    Repository.getRealServicePrice(
                                        illnessHistoryViewModel.service(),
                                        illnessHistoryViewModel.visitId()
                                    )
                                )
                                val tempMeasures = mutableListOf<String>()
                                illnessHistoryViewModel.drugs().forEach { s ->
                                    tempMeasures.add(Repository.getMeasure(s))
                                }
                                illnessHistoryViewModel.clearMeasures(tempMeasures)
                                // байтрил - 10, амокс - 20, серения - 350
                                // вес добавить
                                illnessHistoryViewModel.updateEditingService(true)
                            }
                        else Modifier.fillMaxWidth()
                            .background(color = Color(64, 224, 208))
                    ) {
                        Text(
                            illnessHistoryViewModel.completedPair().first[row],
                            fontSize = 18.sp,
                            modifier = Modifier.width(400.dp).padding(vertical = 10.dp),
                            textAlign = TextAlign.Center
                        )

                        Column(
                            modifier = Modifier.width(400.dp).wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(vertical = 10.dp)
                        ) {
                            for (str in illnessHistoryViewModel.completedPair().second[0][row]) {
                                Text(
                                    str,
                                    fontSize = 18.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.width(400.dp).wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(vertical = 10.dp)
                        ) {
                            illnessHistoryViewModel.completedPair().second[1][row].forEachIndexed { index, str ->
                                var adText = ""
                                adText = try {
                                    illnessHistoryViewModel.measureComplex()[row][index]
                                } catch (e: IndexOutOfBoundsException) {
                                    ""
                                }
                                Text(
                                    text = "$str $adText",
                                    fontSize = 18.sp,
                                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                            onClick = {
                                illnessHistoryViewModel.updateAddingNewService(true)
                                illnessHistoryViewModel.fillMeasureComplex(
                                    mutableListOf(
                                        mutableListOf(""),
                                        mutableListOf("")
                                    )
                                )
                                Repository.setVisitInfo(
                                    id = illnessHistoryViewModel.visitId(),
                                    isNew = illnessHistoryViewModel.getIsNewVisitInfo(),
                                    petId = illnessHistoryViewModel.id(),
                                    date = illnessHistoryViewModel.date(),
                                    sum = illnessHistoryViewModel.price(),
                                    ownerWords = illnessHistoryViewModel.ownerWords(),
                                    temperature = illnessHistoryViewModel.temperature(),
                                    extra = illnessHistoryViewModel.extra(),
                                    diagnosis = illnessHistoryViewModel.diagnosis(),
                                    completed = illnessHistoryViewModel.completed(),
                                    recommendations = illnessHistoryViewModel.recommendations(),
                                    illnessHistoryViewModel = illnessHistoryViewModel,
                                    complete = "false"
                                )
                                illnessHistoryViewModel.updateIsNewVisitInfo(false)
                                val price = Repository.getPrice(
                                    illnessHistoryViewModel.completedPair().first,
                                    illnessHistoryViewModel.completedPair().second[0],
                                    illnessHistoryViewModel.completedPair().second[1],
                                    illnessHistoryViewModel.visitId(),
                                    false,
                                    illnessHistoryViewModel.price(),
                                    illnessHistoryViewModel.completedPair().second[2]
                                )
                                illnessHistoryViewModel.updatePrice(price)
                                Repository.updatePrice(price, illnessHistoryViewModel.visitId())
                            }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                "add"
                            )
                            Text("Добавить услугу", modifier = Modifier.padding(horizontal = 8.dp))
                        }
                        Text(
                            "Стоимость: ${illnessHistoryViewModel.price()} руб",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(start = 600.dp, top = 8.dp)
                        )
                    }
                }
                item {
//                    Button(
//                        onClick = {
//                            data.DataImpl().setVisitInfo(
//                                id = illnessHistoryState.visitId(),
//                                isNew = illnessHistoryState.getIsNewVisitInfo(),
//                                petId = illnessHistoryState.id(),
//                                date = illnessHistoryState.date(),
//                                sum = illnessHistoryState.price(),
//                                ownerWords = illnessHistoryState.ownerWords(),
//                                temperature = illnessHistoryState.temperature(),
//                                extra = illnessHistoryState.extra(),
//                                diagnosis = illnessHistoryState.diagnosis(),
//                                completed = illnessHistoryState.completed(),
//                                recommendations = illnessHistoryState.recommendations(),
//                                illnessHistoryState = illnessHistoryState
//                            )
//                            illnessHistoryState.completedPair().first.forEach {
//                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
//                                    val client =
//                                        clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
//                                    data.DataImpl().setVacInfo(
//                                        it,
//                                        illnessHistoryState.id(),
//                                        illnessHistoryState.date(),
//                                        client,
//                                        petInfoState.nickname(),
//                                        illnessHistoryState.visitId()
//                                    )
//                                }
//                            }
//                            val info =
//                                data.DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
//                            StateWrapper().fillIllnessHistoryState(illnessHistoryState, info.first.second)
//                            illnessHistoryState.updateIsPattern(true)
//                            illnessHistoryState.updateIsNewVisitInfo(false)
//                            StateWrapper().fillPetInfoState(petInfoState, info.first.first)
//                            val doc = com.itextpdf.text.Document()
//                            val bf =
//                                BaseFont.createFont(
//                                    "C:\\Windows\\Fonts\\Arial.ttf",
//                                    BaseFont.IDENTITY_H,
//                                    BaseFont.EMBEDDED
//                                )
//                            val font = Font(bf, 14f, Font.NORMAL)
//                            try {
//                                val writer = PdfWriter.getInstance(
//                                    doc,
//                                    FileOutputStream("C://new//${visit[14]}_${petInfoState.nickname()}.pdf")
//                                )
//                                doc.open()
//                                doc.add(com.itextpdf.text.Paragraph("Клиника: НовоВет         ${visit[14]}", font))
//                                doc.add(com.itextpdf.text.Paragraph(" ", font))
//                                doc.add(com.itextpdf.text.Paragraph("Осмотр", font))
//                                doc.add(com.itextpdf.text.Paragraph("Врачи на приеме: ${visit[1]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Со слов владельца: ${visit[2]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Общее состояние: ${visit[3]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Вес: ${visit[4]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Температура: ${visit[5]}°C", font))
//                                doc.add(com.itextpdf.text.Paragraph("Аппетит: ${visit[6]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Рвота: ${visit[7]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Дефекация: ${visit[8]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Мочеиспускание: ${visit[9]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Доплнительная информация: ${visit[10]}", font))
//                                doc.add(com.itextpdf.text.Paragraph(" ", font))
//                                doc.add(com.itextpdf.text.Paragraph("Предварительный диагноз: ${visit[11]}", font))
//                                doc.add(com.itextpdf.text.Paragraph(" ", font))
//                                doc.add(com.itextpdf.text.Paragraph("Выполнено в клинике:", font))
//                                val table = PdfPTable(3)
//                                table.totalWidth = 260f
//                                println(illnessHistoryState.completedPair())
//                                illnessHistoryState.completedPair().second.first.forEachIndexed() { index, drugs ->
//                                    var newText: String
//                                    drugs.forEachIndexed { drugIndex, s ->
//                                        newText = if (drugIndex == 0) {
//                                            illnessHistoryState.completedPair().first[index]
//                                        } else ""
//                                        val serviceCell = PdfPCell(Phrase(newText, font))
//                                        serviceCell.border = Rectangle.NO_BORDER
//                                        table.addCell(serviceCell)
//                                        val measure = data.DataImpl().getMeasure(s)
//                                        val drugCell = PdfPCell(Phrase(s, font))
//                                        drugCell.border = Rectangle.NO_BORDER
//                                        table.addCell(drugCell)
//                                        val amountCell = PdfPCell(
//                                            Phrase(
//                                                "${illnessHistoryState.completedPair().second.second[index][drugIndex]} $measure",
//                                                font
//                                            )
//                                        )
//                                        amountCell.border = Rectangle.NO_BORDER
//                                        table.addCell(amountCell)
//                                    }
//                                }
//                                doc.add(table)
//                                doc.add(com.itextpdf.text.Paragraph(" ", font))
//                                doc.add(com.itextpdf.text.Paragraph("Рекомендации: ${visit[13]}", font))
//                                doc.add(com.itextpdf.text.Paragraph("Стоимость: ${illnessHistoryState.price()}", font))
//                                doc.close()
//                                writer.close()
//                            } catch (e: DocumentException) {
//                                e.printStackTrace()
//                            } catch (e: FileNotFoundException) {
//                                e.printStackTrace()
//                            }
//                            val message = MimeMessage(Mail().session)
//                            message.setFrom(InternetAddress("murca2403@gmail.com"))
//                            var text1 = ""
//                            var sum = 0
//                            illnessHistoryState.completedPair().first.forEach {
//                                if (it != "Услуга") {
//                                    val price = data.DataImpl().getRealServicePrice(it, illnessHistoryState.visitId())
//                                    sum += price.toInt()
//                                    text1 += "$it - $price\n"
//                                }
//                            }
//                            text1 += "Препараты - ${illnessHistoryState.price() - sum}"
//                            message.subject = "${visit[14]}_${petInfoState.nickname()}"
//                            val textPart = MimeBodyPart()
//                            textPart.setText(text1)
//                            val attachmentPart = MimeBodyPart()
//                            attachmentPart.attachFile(
//                                File("C://new//${visit[14]}_${petInfoState.nickname()}.pdf")
//                            )
//                            val multipart = MimeMultipart()
//                            multipart.addBodyPart(textPart)
//                            multipart.addBodyPart(attachmentPart)
//                            message.setContent(multipart)
//                            message.addRecipient(Message.RecipientType.TO, InternetAddress("appadvert66@gmail.com"))
//                            message.sentDate = Date()
//                            val transport = Mail().session.getTransport("smtp")
//                            transport.connect()
//                            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO))
//                        }
//                    ) {
//                        Text(
//                            text = "Завершить прием",
//                            fontSize = 18.sp
//                        )
//                    }
                }
            }
        }
    }
}

@Composable
fun newServiceAdder(illnessHistoryViewModel: IllnessHistoryViewModel) {
    Box(
        modifier = Modifier.padding(start = 4.dp, top = 200.dp).height(700.dp).width(1550.dp)
            .background(color = Color(176, 224, 230), shape = RoundedCornerShape(4.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
    ) {
        Column() {
            Row {
                Text(
                    "Выберите нужную услугу:",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = {
                        illnessHistoryViewModel.updateEditingService(false)
                        illnessHistoryViewModel.updateAddingNewService(false)
                        illnessHistoryViewModel.updateService("")
                        illnessHistoryViewModel.updateDrugCount(1)
                        illnessHistoryViewModel.updateDrug("")
                        illnessHistoryViewModel.updateAmount("")
                        illnessHistoryViewModel.clearDrugs(listOf(""))
                        illnessHistoryViewModel.clearAmounts(listOf(""))
                        illnessHistoryViewModel.clearOwnerDrugs(listOf("false"))
                        illnessHistoryViewModel.updateServicePrice("")
                        illnessHistoryViewModel.updatePriceTemplate("")
                        illnessHistoryViewModel.updateRangeServicePrice("")
                    },
                    modifier = Modifier.padding(start = 1242.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        "close"
                    )
                }
            }
            Column() {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                        .background(color = Color(64, 224, 208), shape = RoundedCornerShape(8.dp)).fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        "Услуга",
                        fontSize = 18.sp,
                        modifier = Modifier.width(400.dp).padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )
                    Text(
                        "Стоимость услуги",
                        fontSize = 18.sp,
                        modifier = Modifier.width(300.dp).padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )
                    Text(
                        "Препараты",
                        fontSize = 18.sp,
                        modifier = Modifier.width(400.dp).padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )
                    Text(
                        "Количество",
                        fontSize = 18.sp,
                        modifier = Modifier.width(300.dp).padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )
                    Text(
                        "Своё",
                        fontSize = 18.sp,
                        modifier = Modifier.width(400.dp).padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        .background(color = Color(250, 240, 230), shape = RoundedCornerShape(8.dp)).fillMaxWidth()
                        .height(500.dp)
                )
                {
                    Column() {
                        val serviceFindRussian = Regex("[[а-яА-Я]*[a-zA-Z]* *,*/*(*)*:*\\d*\\+*]*")
                        val serviceFindEnglish = Regex("[[a-zA-Z]* *,*/*(*)*:*\\d*\\+*]*")
                        TextField(
                            value = illnessHistoryViewModel.service(),
                            placeholder = {
                                Text("Введите название услуги")
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {
                                if (serviceFindEnglish.matches(it) || serviceFindRussian.matches(it)) {
                                    illnessHistoryViewModel.updateService(it)
                                    illnessHistoryViewModel.updateSearchingService(true)
                                    if (it != "" && !it.contains('(') && !it.contains(')')) {
                                        illnessHistoryViewModel.updateServicesList(
                                            Repository.getServiceByFirstLetter(
                                                it,
                                                false
                                            )
                                        )
                                    } else {
                                        illnessHistoryViewModel.updateSearchingService(false)
                                    }
                                }
                            },
                            modifier = Modifier.width(400.dp),
                        )

                        DropdownMenu(
                            focusable = false,
                            expanded = illnessHistoryViewModel.searchingService(),
                            onDismissRequest = {
                                illnessHistoryViewModel.updateSearchingService(false)
                            },
                            modifier = Modifier.background(color = Color(250, 240, 230))
                        ) {
                            Column(
                                modifier = Modifier.width(400.dp)
                            ) {
                                illnessHistoryViewModel.servicesList().forEach {
                                    Text(
                                        text = it,
                                        textAlign = TextAlign.Start,
                                        fontSize = 18.sp,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 4.dp, horizontal = 4.dp)
                                            .clickable {
                                                illnessHistoryViewModel.updateService(it)
                                                illnessHistoryViewModel.updateSearchingService(false)
                                                illnessHistoryViewModel.updatePriceTemplate(Repository.getServicePrice(it))
                                                illnessHistoryViewModel.updateServicePrice("")
                                            }
                                    )
                                }
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )

                    Column {
                        if (illnessHistoryViewModel.priceTemplate().contains('-')) {
                            illnessHistoryViewModel.updateWritingPrice(true)
                            val temp = illnessHistoryViewModel.priceTemplate()
                            if (temp.contains(" ")) {
                                temp.replace(" ", "")
                            }
                            val priceMin = temp.split('-').first()
                            val priceMax = temp.split('-').last()
                            illnessHistoryViewModel.updateRangeServicePrice("Введите от $priceMin до $priceMax")
                        } else {
                            illnessHistoryViewModel.updateWritingPrice(false)
                            illnessHistoryViewModel.updateServicePrice(illnessHistoryViewModel.priceTemplate())
                        }
                        val priceRegex = Regex("\\d*")
                        TextField(
                            value = illnessHistoryViewModel.servicePrice(),
                            readOnly = !illnessHistoryViewModel.writingPrice(),
                            placeholder = {
                                Text(
                                    illnessHistoryViewModel.rangeServicePrice()
                                )
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {
                                if (priceRegex.matches(it)) illnessHistoryViewModel.updateServicePrice(it)
                            },
                            modifier = Modifier.width(300.dp),
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = Color.Cyan
                    )

                    LazyColumn(modifier = Modifier.width(800.dp)) {
                        val drugFindRussian = Regex("[[а-яА-Я]* *,*\\d*(*)*\\-*]*")
                        val drugFindEnglish = Regex("[[a-zA-Z]* *,*\\d*(*)*\\-*]*")
                        items(illnessHistoryViewModel.drugCount()) { count ->
                            Row {
                                TextField(
                                    placeholder = {
                                        Text("Введите название препарата")
                                    },
                                    modifier = Modifier.width(400.dp),
                                    value = illnessHistoryViewModel.drugs()[count],
                                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                    onValueChange = {
                                        if (drugFindEnglish.matches(it) || drugFindRussian.matches(it)) {
                                            illnessHistoryViewModel.updateSelectedDrug(it, count)
                                            illnessHistoryViewModel.updateSearchingDrug(true)
                                            illnessHistoryViewModel.updateCurrentIndex(count)
                                            if (it != "" && !it.contains('(') && !it.contains(')')) {
                                                illnessHistoryViewModel.updateDrugsList(Repository.getDrugByFirstLetter(it))
                                            } else {
                                                illnessHistoryViewModel.updateSearchingDrug(false)
                                            }
                                        }
                                    },
                                )

                                DropdownMenu(
                                    focusable = false,
                                    expanded = illnessHistoryViewModel.searchingDrug() && count == illnessHistoryViewModel.currentIndex(),
                                    onDismissRequest = {
                                        illnessHistoryViewModel.updateSearchingDrug(false)
                                    },
                                    modifier = Modifier.background(color = Color(250, 240, 230))
                                ) {
                                    Column {
                                        illnessHistoryViewModel.drugsList().forEach {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                fontSize = 18.sp,
                                                modifier = Modifier.height(30.dp)
                                                    .width(400.dp)
                                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                                                    .clickable {
                                                        illnessHistoryViewModel.updateSelectedDrug(
                                                            it,
                                                            illnessHistoryViewModel.currentIndex()
                                                        )
                                                        illnessHistoryViewModel.updateSearchingDrug(false)
                                                        illnessHistoryViewModel.updateSelectedMeasure(
                                                            Repository.getMeasure(
                                                                it
                                                            ), illnessHistoryViewModel.currentIndex()
                                                        )
                                                    }
                                            )
                                        }
                                    }
                                }
                                Divider(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .fillMaxHeight(),
                                    color = Color.Cyan
                                )
                                val amount = Regex("\\d*\\.?\\d*")
                                TextField(
                                    placeholder = {
                                        Text("Введите количество")
                                    },
                                    value = illnessHistoryViewModel.amounts()[count],
                                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                    modifier = Modifier.width(200.dp),
                                    onValueChange = {
                                        if (amount.matches(it)) {
                                            illnessHistoryViewModel.updateSelectedAmount(it, count)
                                            illnessHistoryViewModel.updateCurrentIndex(count)
                                        }
                                    },
                                )

                                Text(
                                    text = if (count < illnessHistoryViewModel.measures().size) illnessHistoryViewModel.measures()[count] else "",
                                    fontSize = 18.sp,
                                    modifier = Modifier.width(100.dp).padding(vertical = 12.dp, horizontal = 8.dp)
                                )

                                Divider(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .fillMaxHeight(),
                                    color = Color.Cyan
                                )
                                val checked =
                                    if (count < illnessHistoryViewModel.ownerDrugs().size) illnessHistoryViewModel.ownerDrugs()[count] else "false"

                                val update = if (checked == "true") "false" else "true"
                                Checkbox(
                                    checked = checked == "true",
                                    onCheckedChange = {
                                        illnessHistoryViewModel.updateCurrentOwnerDrugs(update, count)
                                    },
                                    modifier = Modifier.padding(start = 40.dp)
                                )

                            }
                        }
                        if (!illnessHistoryViewModel.searchingDrug()) {
                            item {
                                Button(
                                    onClick = {
                                        illnessHistoryViewModel.updateDrugCount(illnessHistoryViewModel.drugCount() + 1)
                                        illnessHistoryViewModel.updateDrugs("")
                                        illnessHistoryViewModel.updateAmounts("")
                                        illnessHistoryViewModel.updateMeasures("")
                                        illnessHistoryViewModel.updateOwnerDrugs("false")
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                                ) {
                                    Icon(
                                        Icons.Filled.Add,
                                        "add"
                                    )
                                    Text("Добавить препарат")
                                }
                            }
                        }
                    }
                }
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                onClick = {
                    if (!illnessHistoryViewModel.editingService()) {
                        Repository.addCompleted(
                            service = illnessHistoryViewModel.service(),
                            drugs = illnessHistoryViewModel.drugs(),
                            amounts = illnessHistoryViewModel.amounts(),
                            visitId = illnessHistoryViewModel.visitId(),
                            servicePrice = illnessHistoryViewModel.servicePrice(),
                            ownerDrug = illnessHistoryViewModel.ownerDrugs()
                        )
                    } else {
                        Repository.editCompleted(
                            service = illnessHistoryViewModel.service(),
                            drugs = illnessHistoryViewModel.drugs(),
                            amounts = illnessHistoryViewModel.amounts(),
                            id = illnessHistoryViewModel.completedId(),
                            servicePrice = illnessHistoryViewModel.servicePrice(),
                            ownerDrug = illnessHistoryViewModel.ownerDrugs()
                        )
                    }
                    val info = Repository.getCompleted(illnessHistoryViewModel.visitId())
                    val countLines = info.second.first
                    val completedIds = info.second.second
                    val tempCompleted = info.first
                    illnessHistoryViewModel.updateCountLines(countLines)
                    illnessHistoryViewModel.updateCompletedIds(completedIds)
                    illnessHistoryViewModel.updateCompletedPair(Repository.parseCompleted(tempCompleted, countLines))
                    val price = Repository.getPrice(
                        illnessHistoryViewModel.completedPair().first,
                        illnessHistoryViewModel.completedPair().second[0],
                        illnessHistoryViewModel.completedPair().second[1],
                        illnessHistoryViewModel.visitId(),
                        false,
                        illnessHistoryViewModel.price(),
                        illnessHistoryViewModel.completedPair().second[2]
                    )
                    illnessHistoryViewModel.updatePrice(price)
                    Repository.updatePrice(price, illnessHistoryViewModel.visitId())
                    illnessHistoryViewModel.updateEditingService(false)
                    illnessHistoryViewModel.updateAddingNewService(false)
                    illnessHistoryViewModel.updateService("")
                    illnessHistoryViewModel.updateDrugCount(1)
                    illnessHistoryViewModel.updateDrug("")
                    illnessHistoryViewModel.updateAmount("")
                    illnessHistoryViewModel.clearDrugs(listOf(""))
                    illnessHistoryViewModel.clearAmounts(listOf(""))
                    illnessHistoryViewModel.clearOwnerDrugs(listOf("false"))
                    illnessHistoryViewModel.updateServicePrice("")
                    illnessHistoryViewModel.updateRangeServicePrice("")
                    illnessHistoryViewModel.updatePriceTemplate("")
                }
            ) {
                Text(if (!illnessHistoryViewModel.editingService()) "Добавить услугу" else "Сохранить изменения")
            }
        }
    }
}

@Composable
fun buildOneNote(
    text: String,
    onTextChange: (String) -> (Unit),
    label: String,
    count: Int,
    illnessHistoryViewModel: IllnessHistoryViewModel
) {
    val fontSize = 18
    Row(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            top = if (!illnessHistoryViewModel.next()) 8.dp else 50.dp,
            bottom = 8.dp
        ).fillMaxWidth()
            .background(color = Color(224, 224, 224), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            label,
            fontSize = fontSize.sp,
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp).width(250.dp)
        )
        when (count) {
            0 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Мурзина И.В.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 25.dp),
                        checked = illnessHistoryViewModel.checked1(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateChecked1(it)
                        }
                    )
                    Text(
                        "Кленкова С.В.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 25.dp),
                        checked = illnessHistoryViewModel.checked2(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateChecked2(it)
                        }
                    )
                    Text(
                        "Камышенцева С.Вл.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryViewModel.checked3(),
                        modifier = Modifier.padding(end = 25.dp),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateChecked3(it)
                        }
                    )
                    Text(
                        "Францкевич Э.Р.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 25.dp),
                        checked = illnessHistoryViewModel.checked4(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateChecked4(it)
                        }
                    )
                }
            }

            3 -> {
                TextField(
                    value = illnessHistoryViewModel.weight(),
                    onValueChange = { illnessHistoryViewModel.updateWeight(it) },
                    modifier = Modifier.padding(start = 80.dp).width(100.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(250, 240, 230)),
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            4 -> {
                TextField(
                    value = text,
                    onValueChange = { onTextChange(it) },
                    modifier = Modifier.padding(start = 80.dp).width(100.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(250, 240, 230)),
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

            2 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Удовлетворительное",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.feelingNorm(),
                        enabled = !illnessHistoryViewModel.feelingHard() && !illnessHistoryViewModel.feelingVeryHard(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateFeelingNorm(it)
                        }
                    )
                    Text(
                        "Тяжелое",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.feelingHard(),
                        enabled = !illnessHistoryViewModel.feelingNorm() && !illnessHistoryViewModel.feelingVeryHard(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateFeelingHard(it)
                        }
                    )
                    Text(
                        "Крайне тяжелое",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryViewModel.feelingVeryHard(),
                        enabled = !illnessHistoryViewModel.feelingHard() && !illnessHistoryViewModel.feelingNorm(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateFeelingVeryHard(it)
                        }
                    )
                }
            }

            5 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Отсутствует",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.appetiteLack(),
                        enabled = !illnessHistoryViewModel.appetiteSave() && !illnessHistoryViewModel.appetiteRarely(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateAppetiteLack(it)
                        }
                    )
                    Text(
                        "Сохранен",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.appetiteSave(),
                        enabled = !illnessHistoryViewModel.appetiteLack() && !illnessHistoryViewModel.appetiteRarely(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateAppetiteSave(it)
                        }
                    )
                    Text(
                        "Снижен",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.appetiteRarely(),
                        enabled = !illnessHistoryViewModel.appetiteLack() && !illnessHistoryViewModel.appetiteSave(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateAppetiteRarely(it)
                        }
                    )
                }
            }

            6 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Нет",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.vomitNo(),
                        enabled = !illnessHistoryViewModel.vomitYesOften() && !illnessHistoryViewModel.vomitYesRarely(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateVomitNo(it)
                        }
                    )
                    Text(
                        "Да (редко)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.vomitYesRarely(),
                        enabled = !illnessHistoryViewModel.vomitNo() && !illnessHistoryViewModel.vomitYesOften(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateVomitYesRarely(it)
                        }
                    )
                    Text(
                        "Да (часто)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryViewModel.vomitYesOften(),
                        enabled = !illnessHistoryViewModel.vomitNo() && !illnessHistoryViewModel.vomitYesRarely(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateVomitYesOften(it)
                        }
                    )
                }
            }

            7 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Нормальная",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.deficationNorm(),
                        enabled = !illnessHistoryViewModel.deficationOften() && !illnessHistoryViewModel.deficationRarely() && !illnessHistoryViewModel.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateDeficationNorm(it)
                        }
                    )
                    Text(
                        "Неоформленная (редко)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.deficationRarely(),
                        enabled = !illnessHistoryViewModel.deficationNorm() && !illnessHistoryViewModel.deficationOften() && !illnessHistoryViewModel.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateDeficationRarely(it)
                        }
                    )
                    Text(
                        "Неоформленная (часто)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.deficationOften(),
                        enabled = !illnessHistoryViewModel.deficationNorm() && !illnessHistoryViewModel.deficationRarely() && !illnessHistoryViewModel.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateDeficationOften(it)
                        }
                    )
                    Text(
                        "Нет",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryViewModel.deficationLack(),
                        enabled = !illnessHistoryViewModel.deficationNorm() && !illnessHistoryViewModel.deficationRarely() && !illnessHistoryViewModel.deficationOften(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateDeficationLack(it)
                        }
                    )
                }
            }

            8 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 80.dp)
                ) {
                    Text(
                        "Нормальное",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.urinationNorm(),
                        enabled = !illnessHistoryViewModel.urinationLack() && !illnessHistoryViewModel.urinationOften(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateUrinationNorm(it)
                        }
                    )
                    Text(
                        "Отсутствует",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryViewModel.urinationLack(),
                        enabled = !illnessHistoryViewModel.urinationNorm() && !illnessHistoryViewModel.urinationOften(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateUrinationLack(it)
                        }
                    )
                    Text(
                        "Учащенное",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryViewModel.urinationOften(),
                        enabled = !illnessHistoryViewModel.urinationNorm() && !illnessHistoryViewModel.urinationLack(),
                        onCheckedChange = {
                            illnessHistoryViewModel.updateUrinationOften(it)
                        }
                    )
                }
            }

            else -> {
                TextField(
                    value = text,
                    onValueChange = { onTextChange(it) },
                    placeholder = { Text("Введите текст здесь") },
                    modifier = Modifier.padding(start = 80.dp).fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(250, 240, 230)),
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}