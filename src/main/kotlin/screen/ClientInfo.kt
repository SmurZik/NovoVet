package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.DataImpl
import state.ClientInfoState

@Composable
fun buildClientInfo(
    clientInfoState: ClientInfoState
) {
    val labels = listOf("Фамилия", "Имя", "Отчество", "Телефон", "Адрес")
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(176, 224, 230))
                .fillMaxHeight()
        ) {
            Text(
                text = "Данные о хозяине: ",
                modifier = Modifier.padding(top = 50.dp, start = 16.dp),
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 80.dp, start = 8.dp, end = 8.dp)
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
                            labels[count] + ": ",
                            modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = when (count) {
                                0 -> clientInfoState.secondName()
                                1 -> clientInfoState.firstName()
                                2 -> clientInfoState.lastName()
                                3 -> clientInfoState.phoneNumber()
                                else -> clientInfoState.address()
                            },
                            onValueChange = {
                                when (count) {
                                    0 -> {
                                        clientInfoState.updateSecondName(it)
                                    }

                                    1 -> {
                                        clientInfoState.updateFirstName(it)
                                    }

                                    2 -> clientInfoState.updateLastName(it)
                                    3 -> clientInfoState.updatePhoneNumber(it)
                                    else -> clientInfoState.updateAddress(it)
                                }
                            },
                            modifier = Modifier
                                .width(264.dp)
                                .padding(8.dp),
                            placeholder = {
                                Text(labels[count])
                            },
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            readOnly = !clientInfoState.save()
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight().width(380.dp)
            ) {
                Button(
                    onClick = {
                        if (clientInfoState.save()) {
                            DataImpl().setClientInfo(
                                secondName = clientInfoState.secondName(),
                                firstName = clientInfoState.firstName(),
                                lastName = clientInfoState.lastName(),
                                address = clientInfoState.address(),
                                phone = clientInfoState.phoneNumber(),
                                clientId = clientInfoState.clientId()
                            )
                        }
                        clientInfoState.updateSave(!clientInfoState.save())
                    },
                    modifier = Modifier.padding(top = 350.dp).align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                ) {
                    Text(if (!clientInfoState.save()) "Редактировать" else "Сохранить")
                }
            }
        }
        val stateHorizontal = rememberScrollState(0)
        LazyColumn(
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            items(clientInfoState.countLines()) { row ->
                Row(
                    modifier = Modifier
                        .width(1108.dp)
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
                    println(clientInfoState.countLines())
                    println(clientInfoState.addInfo())
                    for (it in 0..3) {
                        Text(
                            text = clientInfoState.addInfo()[it + row * 4],
                            modifier = if (it != 1 || row == 0) Modifier
                                .padding(13.dp)
                                .width(250.dp)
                            else Modifier
                                .clickable {

                                }
                                .padding(13.dp)
                                .width(250.dp),
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