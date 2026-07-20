package presentation.vaccine

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.components.DatePicker
import presentation.components.search
import data.Repository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun buildVaccine(
    vaccineState: VaccineState
) {
    val textButton = listOf("Найти")
    val stateHorizontal = rememberScrollState(0)
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
                    if (it == 0) {
                        vaccineState.updateIsSearch(!vaccineState.getIsSearch())
                        vaccineState.updateSearchText("")
                        vaccineState.updateSearchBy("secondName")
                        vaccineState.updateAddText(" фамилии")
                    }
                },
                modifier = Modifier
                    .padding(top = 30.dp, end = 8.dp)
            ) {
                val icon = Icons.Filled.Search
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
                text = if (vaccineState.dateResult() == formattedDate) "Показаны последние 100 вакцинаций"
                else "Показаны вакцинации за " + formatDate.format(vaccineState.dateResult()),
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 40.dp, start = 500.dp)
            )
        }
        if (vaccineState.dateResult() != formattedDate) {
            item {
                IconButton(
                    onClick = {
                        vaccineState.updateDateResult(formattedDate)
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
                    vaccineState.updateOpenDialog(true)
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
    if (vaccineState.openDialog()) {

        //implement here the logic to show datepicker and use de return value

        DatePicker(
            initDate = Date(),
            onDismissRequest = { vaccineState.updateOpenDialog(false) },
            onDateSelect = {
                vaccineState.updateDateResult(it)
                vaccineState.updateOpenDialog(false)
            }
        )
    }

    var expanded by remember { mutableStateOf(false) }
    if (vaccineState.getIsSearch()) {
        search(vaccineState, expanded, onExpandedChange = { expanded = it })
    }
    Card(
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxSize()
    ) {
        val tempPair =
            Repository.getVaccineJournal(vaccineState.searchText(), vaccineState.searchBy(), vaccineState.dateResult())
        val currentNote = tempPair.first
        val visitIds = tempPair.second.second
        val countLines = tempPair.second.first

        LazyColumn {
            items(countLines) { row ->
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
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
                            modifier = Modifier
                                .padding(13.dp)
                                .width(if (it == 1 || it == 3) 446.dp else 300.dp),
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

//    Box(modifier = Modifier.fillMaxSize()) {
//        HorizontalScrollbar(
//            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
//            adapter = rememberScrollbarAdapter(stateHorizontal),
//            style = ScrollbarStyle(
//                hoverColor = Color.DarkGray,
//                minimalHeight = 1.dp,
//                hoverDurationMillis = 3,
//                shape = CircleShape,
//                thickness = 10.dp,
//                unhoverColor = Color.Gray
//            )
//        )
//    }
}