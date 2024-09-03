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
import data.DataImpl
import state.DrugsState
import state.ServicesState

@Composable
fun buildDrugs(
    drugsState: DrugsState,
) {
    val textButton = listOf("Добавить препарат")
    val stateHorizontal = rememberScrollState(0)
//    val scope = CoroutineScope(Dispatchers.Default)
    LazyRow(
        modifier = Modifier
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
            .height(110.dp)
    ) {
        items(1) {
            Button(
                onClick = {
                    drugsState.updateAddingNewDrug(true)
                },
                modifier = Modifier
                    .padding(top = 30.dp, end = 8.dp)
            ) {
                val icon = Icons.Filled.Add
                Icon(
                    icon,
                    contentDescription = ""
                )
                Text(textButton[it], modifier = Modifier.padding(8.dp))
            }
        }
    }

    val info = DataImpl().getDrugs()
    val currentNote = info.first
    val countLines = info.second.first
    val serviceIds = info.second.second

    if (drugsState.addingNewDrug() || drugsState.editingDrug()) {
        newDrugAdder(drugsState)
    }
    Card(
        modifier = Modifier
            .padding(top = 105.dp)
            .fillMaxSize()
    ) {
        LazyColumn {
            items(countLines) { row ->
                Row(
                    modifier = Modifier
                        .width(1480.dp)
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
                    for (it in 0..2) {
                        Text(
                            text = currentNote[it + row * 3],
                            modifier = if (it != 0 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 0) 800.dp else 300.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    drugsState.updateEditingDrug(true)
                                    drugsState.updateId(serviceIds[row - 1])
                                    drugsState.updateName(currentNote[row * 3])
                                    drugsState.updateMeasure(currentNote[row * 3 + 1])
                                    drugsState.updatePrice(currentNote[row * 3 + 2])

                                }
                                .padding(13.dp)
                                .width(800.dp),
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

@Composable
fun newDrugAdder(
    drugsState: DrugsState
) {
    val labels = listOf("Название препарата", "Единицы измерения", "Стоимость")
    Card(
        modifier = Modifier.padding(start = 450.dp, top = 100.dp).zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(176, 224, 230))
                .width(550.dp)
                .height(500.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Row {
                Text(
                    text = if (!drugsState.editingDrug()) "Введите данные об услуге: " else "Отредактируйте данные об услуге: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = if (!drugsState.editingDrug()) 150.dp else 80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        drugsState.updateAddingNewDrug(false)
                        drugsState.updateEditingDrug(false)
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
                    .padding(top = 80.dp, start = 45.dp, end = 8.dp)
                    .background(color = Color(64, 224, 208), shape = RoundedCornerShape(16.dp))
            ) {
                items(3) { count ->
                    Row(
                        modifier = Modifier
                            .padding(
                                top = if (count % 3 == 1) 3.dp else 0.dp
                            )
                    ) {
                        Text(
                            labels[count] + ": ",
                            modifier = Modifier
                                .width(170.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = when (count) {
                                0 -> {
                                    drugsState.name()
                                }

                                1 -> {
                                    drugsState.measure()
                                }

                                else -> {
                                    drugsState.price()
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        drugsState.updateName(it)
                                    }

                                    1 -> {
                                        drugsState.updateMeasure(it)
                                    }

                                    else -> {
                                        drugsState.updatePrice(it)
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(294.dp)
                                .padding(8.dp),
                            placeholder = {
                                Text(labels[count])
                            },
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.width(550.dp)
            ) {
                Button(
                    onClick = {
                        if (!drugsState.editingDrug()) {
                            DataImpl().addDrug(drugsState.name(), drugsState.price(), drugsState.measure())
                        } else {
                            DataImpl().editDrug(drugsState.name(), drugsState.price(), drugsState.measure(), drugsState.id())
                        }
                        drugsState.updateAddingNewDrug(false)
                        drugsState.updateEditingDrug(false)
                        drugsState.updatePrice("")
                        drugsState.updateName("")
                        drugsState.updateMeasure("")
                    },
                    modifier = Modifier.align(Alignment.Center).padding(top = 350.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text(if (!drugsState.editingDrug()) "Добавить" else "Применить")
                }
            }
        }
    }
}