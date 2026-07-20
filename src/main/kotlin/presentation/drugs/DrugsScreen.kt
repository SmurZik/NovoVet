package presentation.drugs

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
import androidx.compose.material.icons.filled.Delete
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

@Composable
fun buildDrugs(
    drugsViewModel: DrugsViewModel,
) {
    val textButton = listOf("Добавить препарат")
    val stateHorizontal = rememberScrollState(0)
//    val scope = CoroutineScope(Dispatchers.Default)
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
                    drugsViewModel.updateAddingNewDrug(true)
                    drugsViewModel.updatePrice("")
                    drugsViewModel.updateName("")
                    drugsViewModel.updateMeasure("")
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

    val info = Repository.getDrugs()
    val currentNote = info.first
    val countLines = info.second.first
    val serviceIds = info.second.second

    if (drugsViewModel.addingNewDrug() || drugsViewModel.editingDrug()) {
        newDrugAdder(drugsViewModel)
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
                    for (it in 0..2) {
                        Text(
                            text = currentNote[it + row * 3],
                            modifier = if (it != 0 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 0) 800.dp else 360.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {
                                    drugsViewModel.updateEditingDrug(true)
                                    drugsViewModel.updateId(serviceIds[row - 1])
                                    drugsViewModel.updateName(currentNote[row * 3])
                                    drugsViewModel.updateMeasure(currentNote[row * 3 + 1])
                                    drugsViewModel.updatePrice(currentNote[row * 3 + 2])

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
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun newDrugAdder(
    drugsViewModel: DrugsViewModel
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
                    text = if (!drugsViewModel.editingDrug()) "Введите данные об услуге: " else "Отредактируйте данные об услуге: ",
                    modifier = Modifier.padding(top = 35.dp, start = 30.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )

                Button(
                    modifier = Modifier.padding(top = 20.dp, start = if (!drugsViewModel.editingDrug()) 150.dp else 80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    onClick = {
                        drugsViewModel.updateAddingNewDrug(false)
                        drugsViewModel.updateEditingDrug(false)
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
                        val priceRegex = Regex("\\d*")
                        TextField(
                            value = when (count) {
                                0 -> {
                                    drugsViewModel.name()
                                }

                                1 -> {
                                    drugsViewModel.measure()
                                }

                                else -> {
                                    drugsViewModel.price()
                                }
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        drugsViewModel.updateName(it)
                                    }

                                    1 -> {
                                        drugsViewModel.updateMeasure(it)
                                    }

                                    else -> {
                                        if (priceRegex.matches(it)) drugsViewModel.updatePrice(it)
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
                        if (!drugsViewModel.editingDrug()) {
                            Repository.addDrug(drugsViewModel.name(), drugsViewModel.price(), drugsViewModel.measure())
                        } else {
                            Repository.editDrug(drugsViewModel.name(), drugsViewModel.price(), drugsViewModel.measure(), drugsViewModel.id())
                        }
                        drugsViewModel.updateAddingNewDrug(false)
                        drugsViewModel.updateEditingDrug(false)
                        drugsViewModel.updatePrice("")
                        drugsViewModel.updateName("")
                        drugsViewModel.updateMeasure("")
                    },
                    modifier = Modifier.align(Alignment.Center).padding(top = 350.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text(if (!drugsViewModel.editingDrug()) "Добавить" else "Применить")
                }
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier
                    .clickable {
                        Repository.deleteDrug(drugsViewModel.id())
                        drugsViewModel.updateEditingDrug(false)
                        drugsViewModel.updateAddingNewDrug(false)
                    }
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            )
        }
    }
}