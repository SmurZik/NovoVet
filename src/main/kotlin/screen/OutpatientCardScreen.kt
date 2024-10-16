package screen

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
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import data.DataImpl
import data.Mail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navcontroller.NavController
import state.*
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
    tabState: TabState,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState,
    clientInfoState: ClientInfoState,
    outpatientScreenState: OutpatientScreenState
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
                    if (petInfoState.vac() != "") petInfoState.vac() else "Пусто",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (petInfoState.vac() != "") {
                item {
                    Text(
                        "Дата вакцинирования: ${petInfoState.vacDate()}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }
                // вакцина не на визит, а на питомца
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
        if (illnessHistoryState.loading()) {
            buildLoading()
        } else {
            buildVisitNote(
                dates,
                illnessHistoryState,
                shareDataState,
                illnessHistoryState.visit(),
                petInfoState,
                clientInfoState,
                outpatientScreenState
            )
            if (illnessHistoryState.addingNewService() || illnessHistoryState.editingService()) {
                newServiceAdder(illnessHistoryState)
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
    illnessHistoryState: IllnessHistoryState,
    shareDataState: ShareDataState,
    visit: List<String>,
    petInfoState: PetInfoState,
    clientInfoState: ClientInfoState,
    outpatientScreenState: OutpatientScreenState
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
    if (illnessHistoryState.visit().size > 1 && illnessHistoryState.getIsPattern()) {
        visitDate = DataImpl().readableDateFormat(visit[14] + " " + visit[15])
        illnessHistoryState.updateDate(visitDate)
    }
    if (!illnessHistoryState.getIsNewVisitInfo()) {
        val info = DataImpl().getCompleted(illnessHistoryState.visitId())
        val countLines = info.second.first
        val completedIds = info.second.second
        val tempCompleted = info.first
        illnessHistoryState.updateCompletedPair(DataImpl().parseCompleted(tempCompleted, countLines))
        illnessHistoryState.updateCountLines(countLines)
        illnessHistoryState.updateCompletedIds(completedIds)
        val price = DataImpl().getPrice(
            illnessHistoryState.completedPair().first,
            illnessHistoryState.completedPair().second.first,
            illnessHistoryState.completedPair().second.second,
            illnessHistoryState.visitId(),
            false,
            illnessHistoryState.price()
        )
        illnessHistoryState.updatePrice(price)
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
                enabled = !illnessHistoryState.getIsNew() && illnessHistoryState.getIsPattern(),
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                enabled = illnessHistoryState.getIsPattern(),
                onClick = {
                    illnessHistoryState.updateIsPattern(false)
                    illnessHistoryState.updateIsNewVisitInfo(true)
                    StateWrapper().clearIllnessHistoryState(illnessHistoryState)
                    illnessHistoryState.updateDate(formatForDateNow.format(dateNow))
                    illnessHistoryState.updateCompletedIds(listOf())
                    illnessHistoryState.updateCompletedPair(
                        Pair(
                            listOf("Услуга"),
                            Pair(listOf(listOf("Препараты")), listOf(listOf("Количество")))
                        )
                    )
                    illnessHistoryState.updateCountLines(0)
                    illnessHistoryState.updatePrice(0)
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
                            sum = illnessHistoryState.price(),
                            ownerWords = illnessHistoryState.ownerWords(),
                            temperature = illnessHistoryState.temperature(),
                            extra = illnessHistoryState.extra(),
                            diagnosis = illnessHistoryState.diagnosis(),
                            completed = illnessHistoryState.completed(),
                            recommendations = illnessHistoryState.recommendations(),
                            illnessHistoryState = illnessHistoryState
                        )
                        illnessHistoryState.completedPair().first.forEach {
                            if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                val client =
                                    clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
                                DataImpl().setVacInfo(
                                    it,
                                    illnessHistoryState.id(),
                                    illnessHistoryState.date(),
                                    client,
                                    petInfoState.nickname(),
                                    illnessHistoryState.visitId()
                                )
                            }
                        }
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryState.updateLoading(true)
                            val info = withContext(Dispatchers.IO) {
                                DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryState, info.first.second)
                            illnessHistoryState.updateIsPattern(true)
                            illnessHistoryState.updateIsNewVisitInfo(false)
                            StateWrapper().fillPetInfoState(petInfoState, info.first.first)
                            illnessHistoryState.updateLoading(false)
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
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryState.updateLoading(true)
                            val newVisitInfo = withContext(Dispatchers.IO) {
                                DataImpl().getInfoByPetId(illnessHistoryState.id() to newDate.first + " " + newDate.second)
                            }
                            illnessHistoryState.updateVisit(newVisitInfo.first.second)
                            illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                            illnessHistoryState.updateLoading(false)
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
                                illnessHistoryState.updateLoading(true)
                                scope.launch {
                                    val newVisitInfo =
                                        DataImpl().getInfoByPetId(illnessHistoryState.id() to tempDate.first + " " + tempDate.second)
                                    illnessHistoryState.updateVisit(newVisitInfo.first.second)
                                    illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                                    expanded2 = false
                                    enableNext = true
                                    enablePrevious = true
                                    illnessHistoryState.updateLoading(false)
                                }
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
                        CoroutineScope(Dispatchers.Default).launch {
                            illnessHistoryState.updateLoading(true)
                            val newVisitInfo = withContext(Dispatchers.IO) {
                                DataImpl().getInfoByPetId(illnessHistoryState.id() to newDate.first + " " + newDate.second)
                            }
                            illnessHistoryState.updateVisit(newVisitInfo.first.second)
                            illnessHistoryState.updateVisitId(newVisitInfo.second.second)
                            illnessHistoryState.updateLoading(false)
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
            illnessHistoryState,
            illnessHistoryState.visit(),
            petInfoState,
            clientInfoState,
            outpatientScreenState
        )
    }
}

@Composable
fun buildExamination(
    illnessHistoryState: IllnessHistoryState,
    visit: List<String>,
    petInfoState: PetInfoState,
    clientInfoState: ClientInfoState,
    outpatientScreenState: OutpatientScreenState
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
//        illnessHistoryState.updateDate(DataImpl().readableDateFormat(visit[11] + " " + visit[12]))
//    }
    var active by remember { mutableStateOf(false) }
    priceDialog(
        active = active,
        onActiveChange = { active = it },
        outpatientScreenState = outpatientScreenState,
        illnessHistoryState,
        visit,
        clientInfoState,
        petInfoState
    )

    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 66.dp)
            .background(color = Color.White)
            .fillMaxSize()
    ) {

        if (illnessHistoryState.getIsPattern()) {
            if (visit.size != 1 && !illnessHistoryState.getIsNew()) {
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
                                illnessHistoryState.completedPair().second.first.forEachIndexed { index, drugs ->
                                    tempMeasures.add(mutableListOf())
                                    drugs.forEach { drug ->
                                        tempMeasures[index].add(DataImpl().getMeasure(drug))
                                    }
                                }
                                illnessHistoryState.fillMeasureComplex(tempMeasures)
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (row in 1..illnessHistoryState.countLines() + 1) {
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = illnessHistoryState.completedPair().first[row - 1],
                                                fontSize = 18.sp,
                                                modifier = Modifier.width(400.dp),
                                                textAlign = TextAlign.Center
                                            )

                                            Column(
                                                modifier = Modifier.width(400.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            ) {
                                                for (i in 1..illnessHistoryState.completedPair().second.first[row - 1].size) {
                                                    Text(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        text = illnessHistoryState.completedPair().second.first[row - 1][i - 1],
                                                        fontSize = 18.sp,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }

                                            Column(
                                                modifier = Modifier.width(400.dp)
                                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                            ) {
                                                for (i in 1..illnessHistoryState.completedPair().second.second[row - 1].size) {
                                                    Text(
                                                        text = illnessHistoryState.completedPair().second.second[row - 1][i - 1] + " " + illnessHistoryState.measureComplex()[row - 1][i - 1],
                                                        fontSize = 18.sp,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Стоимость: ${illnessHistoryState.price()} руб",
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
                    modifier = Modifier.padding(top = 60.dp).wrapContentWidth(Alignment.CenterHorizontally)
                ) {

                    items(10) { count ->
                        var currentData = when (count) {
                            1 -> illnessHistoryState.ownerWords()
                            3 -> illnessHistoryState.weight()
                            4 -> illnessHistoryState.temperature()
                            9 -> illnessHistoryState.extra()
                            else -> ""
                        }

                        buildOneNote(
                            currentData,
                            onTextChange = {
                                when (count) {
                                    1 -> illnessHistoryState.updateOwnerWords(it)
                                    3 -> illnessHistoryState.updateWeight(it)
                                    4 -> illnessHistoryState.updateTemperature(it)
                                    9 -> illnessHistoryState.updateExtra(it)
                                    else -> illnessHistoryState.updateDrug(it)
                                }
                            },
                            labels[count],
                            count,
                            illnessHistoryState
                        )
                    }
                    items(3) { count ->
                        var currentData = when (count) {
                            0 -> illnessHistoryState.diagnosis()
                            1 -> illnessHistoryState.completed()
                            else -> illnessHistoryState.recommendations()
                        }
                        if (count == 1) illnessHistoryState.updateIsCompleted(true) else illnessHistoryState.updateIsCompleted(
                            false
                        )
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
                            illnessHistoryState.next(),
                            illnessHistoryState.getIsCompleted(),
                            visit,
                            clientInfoState,
                            petInfoState,
                            illnessHistoryState
                        )
                    }

                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                            onClick = {
                                DataImpl().setVisitInfo(
                                    id = illnessHistoryState.visitId(),
                                    isNew = illnessHistoryState.getIsNewVisitInfo(),
                                    petId = illnessHistoryState.id(),
                                    date = illnessHistoryState.date(),
                                    sum = illnessHistoryState.price(),
                                    ownerWords = illnessHistoryState.ownerWords(),
                                    temperature = illnessHistoryState.temperature(),
                                    extra = illnessHistoryState.extra(),
                                    diagnosis = illnessHistoryState.diagnosis(),
                                    completed = illnessHistoryState.completed(),
                                    recommendations = illnessHistoryState.recommendations(),
                                    illnessHistoryState = illnessHistoryState
                                )
                                illnessHistoryState.completedPair().first.forEach {
                                    if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                        val client =
                                            clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
                                        DataImpl().setVacInfo(
                                            it,
                                            illnessHistoryState.id(),
                                            illnessHistoryState.date(),
                                            client,
                                            petInfoState.nickname(),
                                            illnessHistoryState.visitId()
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
                        onTextChange = { illnessHistoryState.updateWeight(it) },
                        labels[0],
                        count = 0,
                        illnessHistoryState
                    )
                    illnessHistoryState.updateIsCompleted(true)
                    buildBiggerNote(
                        illnessHistoryState.completed(),
                        onTextChange = { illnessHistoryState.updateCompleted(it) },
                        "Выполнено в клинике: ",
                        illnessHistoryState.next(),
                        illnessHistoryState.getIsCompleted(),
                        visit,
                        clientInfoState,
                        petInfoState,
                        illnessHistoryState
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(250, 240, 230)),
                        onClick = {
                            DataImpl().setVisitInfo(
                                id = illnessHistoryState.visitId(),
                                isNew = illnessHistoryState.getIsNewVisitInfo(),
                                petId = illnessHistoryState.id(),
                                date = illnessHistoryState.date(),
                                sum = illnessHistoryState.price(),
                                ownerWords = illnessHistoryState.ownerWords(),
                                temperature = illnessHistoryState.temperature(),
                                extra = illnessHistoryState.extra(),
                                diagnosis = illnessHistoryState.diagnosis(),
                                completed = illnessHistoryState.completed(),
                                recommendations = illnessHistoryState.recommendations(),
                                illnessHistoryState = illnessHistoryState
                            )
                            illnessHistoryState.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
                                    DataImpl().setVacInfo(
                                        it,
                                        illnessHistoryState.id(),
                                        illnessHistoryState.date(),
                                        client,
                                        petInfoState.nickname(),
                                        illnessHistoryState.visitId()
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
    outpatientScreenState: OutpatientScreenState,
    illnessHistoryState: IllnessHistoryState,
    visit: List<String>,
    clientInfoState: ClientInfoState,
    petInfoState: PetInfoState
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
                        illnessHistoryState.updateIsPattern(true)
                        illnessHistoryState.updateIsNewVisitInfo(false)
                        CoroutineScope(Dispatchers.Default).launch {
                            DataImpl().setVisitInfo(
                                id = illnessHistoryState.visitId(),
                                isNew = illnessHistoryState.getIsNewVisitInfo(),
                                petId = illnessHistoryState.id(),
                                date = illnessHistoryState.date(),
                                sum = illnessHistoryState.price(),
                                ownerWords = illnessHistoryState.ownerWords(),
                                temperature = illnessHistoryState.temperature(),
                                extra = illnessHistoryState.extra(),
                                diagnosis = illnessHistoryState.diagnosis(),
                                completed = illnessHistoryState.completed(),
                                recommendations = illnessHistoryState.recommendations(),
                                illnessHistoryState = illnessHistoryState
                            )
                            val info =
                                DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
                            illnessHistoryState.updateVisit(info.first.second)
                            illnessHistoryState.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
                                    DataImpl().setVacInfo(
                                        it,
                                        illnessHistoryState.id(),
                                        illnessHistoryState.date(),
                                        client,
                                        petInfoState.nickname(),
                                        illnessHistoryState.visitId()
                                    )
                                }
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryState, info.first.second)
                            StateWrapper().fillPetInfoState(petInfoState, info.first.first)
                            val doc = com.itextpdf.text.Document()
                            val bf =
                                BaseFont.createFont(
                                    "/home/smurz/Загрузки/Arial.ttf",
                                    BaseFont.IDENTITY_H,
                                    BaseFont.EMBEDDED
                                )
                            val font = Font(bf, 14f, Font.NORMAL)
                            try {
                                val writer = PdfWriter.getInstance(
                                    doc,
                                    FileOutputStream(
                                        "/home/smurz/new/${
                                            illnessHistoryState.visit()[14].split(" ")[0]
                                        }_${petInfoState.nickname()}.pdf"
                                    )
                                )
                                doc.open()
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиника: НовоВет         ${illnessHistoryState.visit()[14]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиент: ${clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Питомец: ${petInfoState.kind() + " " + petInfoState.nickname()}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Осмотр", font))
                                doc.add(com.itextpdf.text.Paragraph("Врачи на приеме: ${illnessHistoryState.visit()[1]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Со слов владельца: ${illnessHistoryState.visit()[2]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Общее состояние: ${illnessHistoryState.visit()[3]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Вес: ${illnessHistoryState.visit()[4]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Температура: ${illnessHistoryState.visit()[5]}°C", font))
                                doc.add(com.itextpdf.text.Paragraph("Аппетит: ${illnessHistoryState.visit()[6]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Рвота: ${illnessHistoryState.visit()[7]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Дефекация: ${illnessHistoryState.visit()[8]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Мочеиспускание: ${illnessHistoryState.visit()[9]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дополнительная информация: ${illnessHistoryState.visit()[10]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Предварительный диагноз: ${illnessHistoryState.visit()[11]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Выполнено в клинике:", font))
                                val table = PdfPTable(3)
                                table.totalWidth = 260f
                                illnessHistoryState.completedPair().second.first.forEachIndexed() { index, drugs ->
                                    var newText: String
                                    drugs.forEachIndexed { drugIndex, s ->
                                        newText = if (drugIndex == 0) {
                                            illnessHistoryState.completedPair().first[index]
                                        } else ""
                                        val serviceCell = PdfPCell(Phrase(newText, font))
                                        serviceCell.border = Rectangle.NO_BORDER
                                        table.addCell(serviceCell)
                                        val measure = DataImpl().getMeasure(s)
                                        val drugCell = PdfPCell(Phrase(s, font))
                                        drugCell.border = Rectangle.NO_BORDER
                                        table.addCell(drugCell)
                                        val amountCell = PdfPCell(
                                            Phrase(
                                                "${illnessHistoryState.completedPair().second.second[index][drugIndex]} $measure",
                                                font
                                            )
                                        )
                                        amountCell.border = Rectangle.NO_BORDER
                                        table.addCell(amountCell)
                                    }
                                }
                                doc.add(table)
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Рекомендации: ${illnessHistoryState.visit()[13]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Стоимость: ${illnessHistoryState.price()}",
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
                            illnessHistoryState.completedPair().first.forEach {
                                if (it != "Услуга" && it != "") {
                                    val price =
                                        DataImpl().getRealServicePrice(it, illnessHistoryState.visitId())
                                    sum += price.toInt()
                                    text1 += "$it - $price\n"
                                }
                            }
                            text1 += "Препараты - ${illnessHistoryState.price() - sum}"
                            message.subject = "${illnessHistoryState.visit()[14]}_${petInfoState.nickname()}"
                            val textPart = MimeBodyPart()
                            textPart.setText(text1)
                            val attachmentPart = MimeBodyPart()
                            attachmentPart.attachFile(
                                File("/home/smurz/new/${illnessHistoryState.visit()[14]}_${petInfoState.nickname()}.pdf")
                            )
                            val multipart = MimeMultipart()
                            multipart.addBodyPart(textPart)
                            multipart.addBodyPart(attachmentPart)
                            message.setContent(multipart)
                            message.addRecipient(
                                Message.RecipientType.TO,
                                InternetAddress("appadvert66@gmail.com")
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
                        illnessHistoryState.updateIsPattern(true)
                        illnessHistoryState.updateIsNewVisitInfo(false)
                        CoroutineScope(Dispatchers.Default).launch {
                            DataImpl().setVisitInfo(
                                id = illnessHistoryState.visitId(),
                                isNew = illnessHistoryState.getIsNewVisitInfo(),
                                petId = illnessHistoryState.id(),
                                date = illnessHistoryState.date(),
                                sum = illnessHistoryState.price(),
                                ownerWords = illnessHistoryState.ownerWords(),
                                temperature = illnessHistoryState.temperature(),
                                extra = illnessHistoryState.extra(),
                                diagnosis = illnessHistoryState.diagnosis(),
                                completed = illnessHistoryState.completed(),
                                recommendations = illnessHistoryState.recommendations(),
                                illnessHistoryState = illnessHistoryState
                            )
                            val info = DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
                            illnessHistoryState.updateVisit(info.first.second)
                            illnessHistoryState.completedPair().first.forEach {
                                if (it.lowercase(Locale.getDefault()).contains("вакцинация")) {
                                    val client =
                                        clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()
                                    DataImpl().setVacInfo(
                                        it,
                                        illnessHistoryState.id(),
                                        illnessHistoryState.date(),
                                        client,
                                        petInfoState.nickname(),
                                        illnessHistoryState.visitId()
                                    )
                                }
                            }
                            StateWrapper().fillIllnessHistoryState(illnessHistoryState, info.first.second)
                            StateWrapper().fillPetInfoState(petInfoState, info.first.first)
                            val doc = com.itextpdf.text.Document()
                            val bf =
                                BaseFont.createFont(
                                    /*"C:\\Windows\\Fonts\\Arial.ttf"*/
                                    "/home/smurz/Загрузки/Arial.ttf",
                                    BaseFont.IDENTITY_H,
                                    BaseFont.EMBEDDED
                                )
                            val font = Font(bf, 14f, Font.NORMAL)
                            try {
                                val writer = PdfWriter.getInstance(
                                    doc,
                                    FileOutputStream(
                                        /*C://new// */
                                        "/home/smurz/new/${
                                            illnessHistoryState.visit()[14].split(" ")[0]
                                        }_${petInfoState.nickname()}.pdf"
                                    )
                                )
                                doc.open()
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиника: НовоВет         ${illnessHistoryState.visit()[14]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Клиент: ${clientInfoState.secondName() + " " + clientInfoState.firstName() + " " + clientInfoState.lastName()}",
                                        font
                                    )
                                )
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Питомец: ${petInfoState.kind() + " " + petInfoState.nickname()}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Осмотр", font))
                                doc.add(com.itextpdf.text.Paragraph("Врачи на приеме: ${illnessHistoryState.visit()[1]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Со слов владельца: ${illnessHistoryState.visit()[2]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Общее состояние: ${illnessHistoryState.visit()[3]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Вес: ${illnessHistoryState.visit()[4]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Температура: ${illnessHistoryState.visit()[5]}°C", font))
                                doc.add(com.itextpdf.text.Paragraph("Аппетит: ${illnessHistoryState.visit()[6]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Рвота: ${illnessHistoryState.visit()[7]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Дефекация: ${illnessHistoryState.visit()[8]}", font))
                                doc.add(com.itextpdf.text.Paragraph("Мочеиспускание: ${illnessHistoryState.visit()[9]}", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Дополнительная информация: ${illnessHistoryState.visit()[10]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(
                                    com.itextpdf.text.Paragraph(
                                        "Предварительный диагноз: ${illnessHistoryState.visit()[11]}",
                                        font
                                    )
                                )
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Выполнено в клинике:", font))
                                val table = PdfPTable(3)
                                table.totalWidth = 260f
                                illnessHistoryState.completedPair().second.first.forEachIndexed() { index, drugs ->
                                    var newText: String
                                    drugs.forEachIndexed { drugIndex, s ->
                                        newText = if (drugIndex == 0) {
                                            illnessHistoryState.completedPair().first[index]
                                        } else ""
                                        val serviceCell = PdfPCell(Phrase(newText, font))
                                        serviceCell.border = Rectangle.NO_BORDER
                                        table.addCell(serviceCell)
                                        val measure = DataImpl().getMeasure(s)
                                        val drugCell = PdfPCell(Phrase(s, font))
                                        drugCell.border = Rectangle.NO_BORDER
                                        table.addCell(drugCell)
                                        val amountCell = PdfPCell(
                                            Phrase(
                                                "${illnessHistoryState.completedPair().second.second[index][drugIndex]} $measure",
                                                font
                                            )
                                        )
                                        amountCell.border = Rectangle.NO_BORDER
                                        table.addCell(amountCell)
                                    }
                                }
                                doc.add(table)
                                doc.add(com.itextpdf.text.Paragraph(" ", font))
                                doc.add(com.itextpdf.text.Paragraph("Рекомендации: ${illnessHistoryState.visit()[13]}", font))
                                doc.close()
                                writer.close()
                            } catch (e: DocumentException) {
                                e.printStackTrace()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                            val message = MimeMessage(Mail().session)
                            /*novovetget@gmail.com*/
                            message.setFrom(InternetAddress("appadvert66@gmail.com"))
                            var text1 = ""
                            var sum = 0
                            illnessHistoryState.completedPair().first.forEach {
                                if (it != "Услуга" && it != "") {
                                    val price =
                                        DataImpl().getRealServicePrice(it, illnessHistoryState.visitId())
                                    sum += price.toInt()
                                    text1 += "$it - $price\n"
                                }
                            }
                            text1 += "Препараты - ${illnessHistoryState.price() - sum}"
                            message.subject = "${visit[14]}_${petInfoState.nickname()}"
                            val textPart = MimeBodyPart()
                            textPart.setText(text1)
                            val attachmentPart = MimeBodyPart()
                            attachmentPart.attachFile(
                                /*C://new// */
                                File("/home/smurz/new/${illnessHistoryState.visit()[14]}_${petInfoState.nickname()}.pdf")
                            )
                            val multipart = MimeMultipart()
                            multipart.addBodyPart(textPart)
                            multipart.addBodyPart(attachmentPart)
                            message.setContent(multipart)
                            message.addRecipient(
                                Message.RecipientType.TO,
                                InternetAddress("appadvert66@gmail.com")
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
    clientInfoState: ClientInfoState,
    petInfoState: PetInfoState,
    illnessHistoryState: IllnessHistoryState
) {
    Box(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)
            .fillMaxWidth()
            .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
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
            if (!illnessHistoryState.getIsNewVisitInfo()) {
                val info = DataImpl().getCompleted(illnessHistoryState.visitId())
                val countLines = info.second.first
                val completedIds = info.second.second
                val tempCompleted = info.first
                illnessHistoryState.updateCompletedPair(DataImpl().parseCompleted(tempCompleted, countLines))
                illnessHistoryState.updateCountLines(countLines)
                illnessHistoryState.updateCompletedIds(completedIds)
                val tempMeasures = mutableListOf<MutableList<String>>()
                illnessHistoryState.completedPair().second.first.forEachIndexed { index, it ->
                    tempMeasures.add(mutableListOf())
                    it.forEach {
                        tempMeasures[index].add(DataImpl().getMeasure(it))
                    }
                }
                illnessHistoryState.fillMeasureComplex(tempMeasures)
            }
            LazyColumn(
                modifier = Modifier.padding(top = 40.dp).fillMaxWidth()
            ) {
                items(count = illnessHistoryState.countLines() + 1) { row ->
                    Row(
                        modifier = if (row != 0 && !illnessHistoryState.addingNewService() && !illnessHistoryState.editingService()) Modifier.fillMaxWidth()
                            .background(color = Color(250, 240, 230))
                            .clickable {
                                illnessHistoryState.updateService(illnessHistoryState.completedPair().first[row])
                                illnessHistoryState.clearDrugs(illnessHistoryState.completedPair().second.first[row])
                                illnessHistoryState.clearAmounts(illnessHistoryState.completedPair().second.second[row])
                                illnessHistoryState.updateDrugCount(illnessHistoryState.drugs().size)
                                illnessHistoryState.updateCompletedId(illnessHistoryState.completedIds()[row - 1])
                                illnessHistoryState.updatePriceTemplate(DataImpl().getServicePrice(illnessHistoryState.service()))
                                illnessHistoryState.updateServicePrice(
                                    DataImpl().getRealServicePrice(
                                        illnessHistoryState.service(),
                                        illnessHistoryState.visitId()
                                    )
                                )
                                val tempMeasures = mutableListOf<String>()
                                illnessHistoryState.drugs().forEach { s ->
                                    tempMeasures.add(DataImpl().getMeasure(s))
                                }
                                illnessHistoryState.clearMeasures(tempMeasures)
                                // байтрил - 10, амокс - 20, серения - 350
                                // вес добавить
                                illnessHistoryState.updateEditingService(true)
                            }
                        else Modifier.fillMaxWidth()
                            .background(color = Color(64, 224, 208))
                    ) {
                        Text(
                            illnessHistoryState.completedPair().first[row],
                            fontSize = 18.sp,
                            modifier = Modifier.width(400.dp).padding(vertical = 10.dp),
                            textAlign = TextAlign.Center
                        )

                        Column(
                            modifier = Modifier.width(400.dp).wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(vertical = 10.dp)
                        ) {
                            for (str in illnessHistoryState.completedPair().second.first[row]) {
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
                            illnessHistoryState.completedPair().second.second[row].forEachIndexed { index, str ->
                                var adText = ""
                                adText = try {
                                    illnessHistoryState.measureComplex()[row][index]
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
                                illnessHistoryState.updateAddingNewService(true)
                                illnessHistoryState.fillMeasureComplex(
                                    mutableListOf(
                                        mutableListOf(""),
                                        mutableListOf("")
                                    )
                                )
                                DataImpl().setVisitInfo(
                                    id = illnessHistoryState.visitId(),
                                    isNew = illnessHistoryState.getIsNewVisitInfo(),
                                    petId = illnessHistoryState.id(),
                                    date = illnessHistoryState.date(),
                                    sum = illnessHistoryState.price(),
                                    ownerWords = illnessHistoryState.ownerWords(),
                                    temperature = illnessHistoryState.temperature(),
                                    extra = illnessHistoryState.extra(),
                                    diagnosis = illnessHistoryState.diagnosis(),
                                    completed = illnessHistoryState.completed(),
                                    recommendations = illnessHistoryState.recommendations(),
                                    illnessHistoryState = illnessHistoryState
                                )
                                illnessHistoryState.updateIsNewVisitInfo(false)
                                val price = DataImpl().getPrice(
                                    illnessHistoryState.completedPair().first,
                                    illnessHistoryState.completedPair().second.first,
                                    illnessHistoryState.completedPair().second.second,
                                    illnessHistoryState.visitId(),
                                    false,
                                    illnessHistoryState.price()
                                )
                                illnessHistoryState.updatePrice(price)
                                DataImpl().updatePrice(price, illnessHistoryState.visitId())
                            }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                "add"
                            )
                            Text("Добавить услугу", modifier = Modifier.padding(horizontal = 8.dp))
                        }
                        Text(
                            "Стоимость: ${illnessHistoryState.price()} руб",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(start = 600.dp, top = 8.dp)
                        )
                    }
                }
                item {
//                    Button(
//                        onClick = {
//                            DataImpl().setVisitInfo(
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
//                                    DataImpl().setVacInfo(
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
//                                DataImpl().getInfoByPetId(illnessHistoryState.id() to "${illnessHistoryState.date()}:00")
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
//                                        val measure = DataImpl().getMeasure(s)
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
//                                    val price = DataImpl().getRealServicePrice(it, illnessHistoryState.visitId())
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
fun newServiceAdder(illnessHistoryState: IllnessHistoryState) {
    Box(
        modifier = Modifier.padding(start = 100.dp, top = 200.dp).height(700.dp).width(1450.dp)
            .background(color = Color(176, 224, 230), shape = RoundedCornerShape(4.dp))
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
                        illnessHistoryState.updateEditingService(false)
                        illnessHistoryState.updateAddingNewService(false)
                        illnessHistoryState.updateService("")
                        illnessHistoryState.updateDrugCount(1)
                        illnessHistoryState.updateDrug("")
                        illnessHistoryState.updateAmount("")
                        illnessHistoryState.clearDrugs(listOf(""))
                        illnessHistoryState.clearAmounts(listOf(""))
                        illnessHistoryState.updateServicePrice("")
                        illnessHistoryState.updatePriceTemplate("")
                        illnessHistoryState.updateRangeServicePrice("")
                    },
                    modifier = Modifier.padding(start = 1150.dp),
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
                            value = illnessHistoryState.service(),
                            placeholder = {
                                Text("Введите название услуги")
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {
                                if (serviceFindEnglish.matches(it) || serviceFindRussian.matches(it)) {
                                    illnessHistoryState.updateService(it)
                                    illnessHistoryState.updateSearchingService(true)
                                    if (it != "" && !it.contains('(') && !it.contains(')')) {
                                        illnessHistoryState.updateServicesList(DataImpl().getServiceByFirstLetter(it))
                                    } else {
                                        illnessHistoryState.updateSearchingService(false)
                                    }
                                }
                            },
                            modifier = Modifier.width(400.dp),
                        )

                        DropdownMenu(
                            focusable = false,
                            expanded = illnessHistoryState.searchingService(),
                            onDismissRequest = {
                                illnessHistoryState.updateSearchingService(false)
                            },
                            modifier = Modifier.background(color = Color(250, 240, 230))
                        ) {
                            Column(
                                modifier = Modifier.width(400.dp)
                            ) {
                                illnessHistoryState.servicesList().forEach {
                                    Text(
                                        text = it,
                                        textAlign = TextAlign.Start,
                                        fontSize = 18.sp,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 4.dp, horizontal = 4.dp)
                                            .clickable {
                                                illnessHistoryState.updateService(it)
                                                illnessHistoryState.updateSearchingService(false)
                                                illnessHistoryState.updatePriceTemplate(DataImpl().getServicePrice(it))
                                                illnessHistoryState.updateServicePrice("")
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
                        if (illnessHistoryState.priceTemplate().contains('-')) {
                            illnessHistoryState.updateWritingPrice(true)
                            val temp = illnessHistoryState.priceTemplate()
                            if (temp.contains(" ")) {
                                temp.replace(" ", "")
                            }
                            val priceMin = temp.split('-').first()
                            val priceMax = temp.split('-').last()
                            illnessHistoryState.updateRangeServicePrice("Введите от $priceMin до $priceMax")
                        } else {
                            illnessHistoryState.updateWritingPrice(false)
                            illnessHistoryState.updateServicePrice(illnessHistoryState.priceTemplate())
                        }
                        val priceRegex = Regex("\\d*")
                        TextField(
                            value = illnessHistoryState.servicePrice(),
                            readOnly = !illnessHistoryState.writingPrice(),
                            placeholder = {
                                Text(
                                    illnessHistoryState.rangeServicePrice()
                                )
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {
                                if (priceRegex.matches(it)) illnessHistoryState.updateServicePrice(it)
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
                        items(illnessHistoryState.drugCount()) { count ->
                            Row() {
                                TextField(
                                    placeholder = {
                                        Text("Введите название препарата")
                                    },
                                    modifier = Modifier.width(400.dp),
                                    value = illnessHistoryState.drugs()[count],
                                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                    onValueChange = {
                                        if (drugFindEnglish.matches(it) || drugFindRussian.matches(it)) {
                                            illnessHistoryState.updateSelectedDrug(it, count)
                                            illnessHistoryState.updateSearchingDrug(true)
                                            illnessHistoryState.updateCurrentIndex(count)
                                            if (it != "" && !it.contains('(') && !it.contains(')')) {
                                                illnessHistoryState.updateDrugsList(DataImpl().getDrugByFirstLetter(it))
                                            } else {
                                                illnessHistoryState.updateSearchingDrug(false)
                                            }
                                        }
                                    },
                                )

                                DropdownMenu(
                                    focusable = false,
                                    expanded = illnessHistoryState.searchingDrug() && count == illnessHistoryState.currentIndex(),
                                    onDismissRequest = {
                                        illnessHistoryState.updateSearchingDrug(false)
                                    },
                                    modifier = Modifier.background(color = Color(250, 240, 230))
                                ) {
                                    Column {
                                        illnessHistoryState.drugsList().forEach {
                                            Text(
                                                text = it,
                                                textAlign = TextAlign.Start,
                                                fontSize = 18.sp,
                                                modifier = Modifier.height(30.dp)
                                                    .width(400.dp)
                                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                                                    .clickable {
                                                        illnessHistoryState.updateSelectedDrug(
                                                            it,
                                                            illnessHistoryState.currentIndex()
                                                        )
                                                        illnessHistoryState.updateSearchingDrug(false)
                                                        illnessHistoryState.updateSelectedMeasure(
                                                            DataImpl().getMeasure(
                                                                it
                                                            ), illnessHistoryState.currentIndex()
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
                                    value = illnessHistoryState.amounts()[count],
                                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                    modifier = Modifier.width(250.dp),
                                    onValueChange = {
                                        if (amount.matches(it)) {
                                            illnessHistoryState.updateSelectedAmount(it, count)
                                            illnessHistoryState.updateCurrentIndex(count)
                                        }
                                    },
                                )

                                Text(
                                    text = if (count < illnessHistoryState.measures().size) illnessHistoryState.measures()[count] else "",
                                    fontSize = 18.sp,
                                    modifier = Modifier.width(100.dp).padding(vertical = 12.dp, horizontal = 8.dp)
                                )
                            }
                        }
                        if (!illnessHistoryState.searchingDrug()) {
                            item {
                                Button(
                                    onClick = {
                                        illnessHistoryState.updateDrugCount(illnessHistoryState.drugCount() + 1)
                                        illnessHistoryState.updateDrugs("")
                                        illnessHistoryState.updateAmounts("")
                                        illnessHistoryState.updateMeasures("")
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
                    if (!illnessHistoryState.editingService()) {
                        DataImpl().addCompleted(
                            service = illnessHistoryState.service(),
                            drugs = illnessHistoryState.drugs(),
                            amounts = illnessHistoryState.amounts(),
                            visitId = illnessHistoryState.visitId(),
                            servicePrice = illnessHistoryState.servicePrice()
                        )
                    } else {
                        DataImpl().editCompleted(
                            service = illnessHistoryState.service(),
                            drugs = illnessHistoryState.drugs(),
                            amounts = illnessHistoryState.amounts(),
                            id = illnessHistoryState.completedId(),
                            servicePrice = illnessHistoryState.servicePrice()
                        )
                    }
                    val info = DataImpl().getCompleted(illnessHistoryState.visitId())
                    val countLines = info.second.first
                    val completedIds = info.second.second
                    val tempCompleted = info.first
                    illnessHistoryState.updateCountLines(countLines)
                    illnessHistoryState.updateCompletedIds(completedIds)
                    illnessHistoryState.updateCompletedPair(DataImpl().parseCompleted(tempCompleted, countLines))
                    val price = DataImpl().getPrice(
                        illnessHistoryState.completedPair().first,
                        illnessHistoryState.completedPair().second.first,
                        illnessHistoryState.completedPair().second.second,
                        illnessHistoryState.visitId(),
                        false,
                        illnessHistoryState.price()
                    )
                    illnessHistoryState.updatePrice(price)
                    DataImpl().updatePrice(price, illnessHistoryState.visitId())
                    illnessHistoryState.updateEditingService(false)
                    illnessHistoryState.updateAddingNewService(false)
                    illnessHistoryState.updateService("")
                    illnessHistoryState.updateDrugCount(1)
                    illnessHistoryState.updateDrug("")
                    illnessHistoryState.updateAmount("")
                    illnessHistoryState.clearDrugs(listOf(""))
                    illnessHistoryState.clearAmounts(listOf(""))
                    illnessHistoryState.updateServicePrice("")
                    illnessHistoryState.updateRangeServicePrice("")
                    illnessHistoryState.updatePriceTemplate("")
                }
            ) {
                Text(if (!illnessHistoryState.editingService()) "Добавить услугу" else "Сохранить изменения")
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
    illnessHistoryState: IllnessHistoryState
) {
    val fontSize = 18
    Row(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            top = if (!illnessHistoryState.next()) 8.dp else 50.dp,
            bottom = 8.dp
        ).fillMaxWidth()
            .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
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
                        checked = illnessHistoryState.checked1(),
                        onCheckedChange = {
                            illnessHistoryState.updateChecked1(it)
                        }
                    )
                    Text(
                        "Кленкова С.В.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 25.dp),
                        checked = illnessHistoryState.checked2(),
                        onCheckedChange = {
                            illnessHistoryState.updateChecked2(it)
                        }
                    )
                    Text(
                        "Камышенцева С.Вл.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryState.checked3(),
                        modifier = Modifier.padding(end = 25.dp),
                        onCheckedChange = {
                            illnessHistoryState.updateChecked3(it)
                        }
                    )
                    Text(
                        "Францкевич Э.Р.",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 25.dp),
                        checked = illnessHistoryState.checked4(),
                        onCheckedChange = {
                            illnessHistoryState.updateChecked4(it)
                        }
                    )
                }
            }

            3 -> {
                TextField(
                    value = illnessHistoryState.weight(),
                    onValueChange = { illnessHistoryState.updateWeight(it) },
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
                        checked = illnessHistoryState.feelingNorm(),
                        enabled = !illnessHistoryState.feelingHard() && !illnessHistoryState.feelingVeryHard(),
                        onCheckedChange = {
                            illnessHistoryState.updateFeelingNorm(it)
                        }
                    )
                    Text(
                        "Тяжелое",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.feelingHard(),
                        enabled = !illnessHistoryState.feelingNorm() && !illnessHistoryState.feelingVeryHard(),
                        onCheckedChange = {
                            illnessHistoryState.updateFeelingHard(it)
                        }
                    )
                    Text(
                        "Крайне тяжелое",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryState.feelingVeryHard(),
                        enabled = !illnessHistoryState.feelingHard() && !illnessHistoryState.feelingNorm(),
                        onCheckedChange = {
                            illnessHistoryState.updateFeelingVeryHard(it)
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
                        checked = illnessHistoryState.appetiteLack(),
                        enabled = !illnessHistoryState.appetiteSave(),
                        onCheckedChange = {
                            illnessHistoryState.updateAppetiteLack(it)
                        }
                    )
                    Text(
                        "Сохранен",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.appetiteSave(),
                        enabled = !illnessHistoryState.appetiteLack(),
                        onCheckedChange = {
                            illnessHistoryState.updateAppetiteSave(it)
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
                        checked = illnessHistoryState.vomitNo(),
                        enabled = !illnessHistoryState.vomitYesOften() && !illnessHistoryState.vomitYesRarely(),
                        onCheckedChange = {
                            illnessHistoryState.updateVomitNo(it)
                        }
                    )
                    Text(
                        "Да (редко)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.vomitYesRarely(),
                        enabled = !illnessHistoryState.vomitNo() && !illnessHistoryState.vomitYesOften(),
                        onCheckedChange = {
                            illnessHistoryState.updateVomitYesRarely(it)
                        }
                    )
                    Text(
                        "Да (часто)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryState.vomitYesOften(),
                        enabled = !illnessHistoryState.vomitNo() && !illnessHistoryState.vomitYesRarely(),
                        onCheckedChange = {
                            illnessHistoryState.updateVomitYesOften(it)
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
                        checked = illnessHistoryState.deficationNorm(),
                        enabled = !illnessHistoryState.deficationOften() && !illnessHistoryState.deficationRarely() && !illnessHistoryState.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryState.updateDeficationNorm(it)
                        }
                    )
                    Text(
                        "Неоформленная (редко)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.deficationRarely(),
                        enabled = !illnessHistoryState.deficationNorm() && !illnessHistoryState.deficationOften() && !illnessHistoryState.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryState.updateDeficationRarely(it)
                        }
                    )
                    Text(
                        "Неоформленная (часто)",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.deficationOften(),
                        enabled = !illnessHistoryState.deficationNorm() && !illnessHistoryState.deficationRarely() && !illnessHistoryState.deficationLack(),
                        onCheckedChange = {
                            illnessHistoryState.updateDeficationOften(it)
                        }
                    )
                    Text(
                        "Нет",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryState.deficationLack(),
                        enabled = !illnessHistoryState.deficationNorm() && !illnessHistoryState.deficationRarely() && !illnessHistoryState.deficationOften(),
                        onCheckedChange = {
                            illnessHistoryState.updateDeficationLack(it)
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
                        checked = illnessHistoryState.urinationNorm(),
                        enabled = !illnessHistoryState.urinationLack() && !illnessHistoryState.urinationOften(),
                        onCheckedChange = {
                            illnessHistoryState.updateUrinationNorm(it)
                        }
                    )
                    Text(
                        "Отсутствует",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = 30.dp),
                        checked = illnessHistoryState.urinationLack(),
                        enabled = !illnessHistoryState.urinationNorm() && !illnessHistoryState.urinationOften(),
                        onCheckedChange = {
                            illnessHistoryState.updateUrinationLack(it)
                        }
                    )
                    Text(
                        "Учащенное",
                        fontSize = 18.sp
                    )
                    Checkbox(
                        checked = illnessHistoryState.urinationOften(),
                        enabled = !illnessHistoryState.urinationNorm() && !illnessHistoryState.urinationLack(),
                        onCheckedChange = {
                            illnessHistoryState.updateUrinationOften(it)
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