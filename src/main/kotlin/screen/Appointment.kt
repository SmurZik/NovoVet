package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import components.DatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import state.HomeViewModel
import java.util.*

@Composable
fun buildAppointment(
    homeViewModel: HomeViewModel
) {
    var enableCalendar by remember { mutableStateOf(false) }


    if (homeViewModel.initDate()) {
        homeViewModel.updateDate(System.currentTimeMillis())
        homeViewModel.updateScheduleList(homeViewModel.makeSchedule(homeViewModel.dateInMillis()))
        homeViewModel.init()

    }
    if (enableCalendar) {

        DatePicker(
            initDate = Date(),
            onDismissRequest = { enableCalendar = false },
            onDateSelect = {
                homeViewModel.updateDate(
                    it.time
                )
                homeViewModel.updateInitDate(false)
                enableCalendar = false
                homeViewModel.init()
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.background(color = Color.LightGray).fillMaxWidth().height(64.dp)
            ) {

                IconButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        enableCalendar = true
                    },
                    enabled = !homeViewModel.isEnabledAdder()
                ) {
                    Icon(
                        painter = painterResource("calendar.png"),
                        contentDescription = "calendar",
                        modifier = Modifier.size(48.dp)
                            .background(Color(0, 191, 255), shape = RoundedCornerShape(8.dp))
                    )
                }

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0, 191, 255)),
                    onClick = {
                        homeViewModel.previousDay(homeViewModel.dateInMillis())
                        homeViewModel.updateInitDate(false)
                        homeViewModel.init()

                    },
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp, start = 350.dp),
                    enabled = !homeViewModel.isEnabledAdder()

                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                    Text("Предыдущий", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }

                Text(
                    text = homeViewModel.date(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).width(300.dp).height(40.dp)
                        .clickable(
                            enabled = !homeViewModel.isEnabledAdder()
                        ) {
                            enableCalendar = true
                        }
                )

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0, 191, 255)),
                    onClick = {
                        homeViewModel.nextDay(homeViewModel.dateInMillis())
                        homeViewModel.updateInitDate(false)
                        homeViewModel.init()
                    },
                    modifier = Modifier.padding(8.dp),
                    enabled = !homeViewModel.isEnabledAdder()

                ) {
                    Text("Следующий", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = ""
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        homeViewModel.init()
                    }
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "refresh",
                        modifier = Modifier.size(48.dp)
                            .background(Color(0, 191, 255), shape = RoundedCornerShape(8.dp))
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn {
                    items(homeViewModel.scheduleList().size) { row ->
                        Row(
                            modifier = if (row != 0) Modifier
                                .height(IntrinsicSize.Min)
                                .background(
                                    color = if (homeViewModel.colorMap()[row] == 1) Color(
                                        224,
                                        224,
                                        224
                                    ) else Color.White
                                ) else Modifier
                                .height(IntrinsicSize.Min)
                                .background(
                                    color = Color.Blue
                                ),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            for (it in 0..4) {

                                if (it != 4 || row == 0) {
                                    val temp = homeViewModel.fullSchedule()[it + row * 5]
                                    Text(
                                        text = temp,
                                        modifier = Modifier
                                            .padding(13.dp)
                                            .width(if (it == 1) 430.dp else if (it == 3) 400.dp else if (it != 4) 220.dp else 205.dp),
                                        textAlign = TextAlign.Center,
                                        color = if (row == 0) Color.White else Color.Black,
                                        fontSize = if (row == 0) 20.sp else 16.sp
                                    )

                                    Divider(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .fillMaxHeight(),
                                        color = Color.LightGray
                                    )
                                } else {
                                    val value = homeViewModel.fullSchedule()[it + row * 5]
                                    Checkbox(
                                        modifier = Modifier.padding(horizontal = 107.dp),
                                        checked = value == "true",
                                        onCheckedChange = {

                                        },
                                        enabled = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}