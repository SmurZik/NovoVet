package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.search
import data.DataImpl
import navcontroller.NavController
import state.ClientsScreenState
import state.TabState

@Composable
fun buildClients(
    navController: NavController,
    clientsScreenState: ClientsScreenState,
    tabState: TabState
) {
    LazyRow(
        modifier = Modifier
//            .padding(start = 60.dp)
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
    ) {
        item {
            Text(
                text = "Амбулаторные приемы",
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
            )
        }
    }
    val textButton = listOf("-", "Найти")
    val stateHorizontal = rememberScrollState(0)
//    val scope = CoroutineScope(Dispatchers.Default)
    LazyRow(
        modifier = Modifier
            .padding(top = 30.dp)
            .background(color = Color.Cyan)
            .fillMaxWidth(1f)
            .height(100.dp)
    ) {
        items(2) {
            Button(
                onClick = {
                    if (it == 1) {
                        clientsScreenState.updateIsSearch(!clientsScreenState.getIsSearch())
                        clientsScreenState.updateSearchText("")
                        clientsScreenState.updateSearchBy("secondName")
                        clientsScreenState.updateAddText(" фамилии")
                    } else {

                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp, end = 8.dp)
            ) {
                val icon = if (it == 0) Icons.Filled.Add else Icons.Filled.Search
                Icon(
                    icon,
                    contentDescription = ""
                )
                Text(textButton[it], modifier = Modifier.padding(8.dp))
            }
        }
    }

    val info = DataImpl().getClientsInfo(clientsScreenState.searchText(), clientsScreenState.searchBy())
    val currentNote = info.first
    val countLines = info.second

    var expanded by remember { mutableStateOf(false) }
    if (clientsScreenState.getIsSearch()) {
        search(clientsScreenState, expanded, onExpandedChange = { expanded = it })
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
                        .width(1000.dp)
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
                    for (it in 0..1) {
                        Text(
                            text = currentNote[it + row * 2],
                            modifier = if (it != 1 || row == 0) Modifier
                                .padding(13.dp)
                                .width(if (it == 0) 400.dp else 600.dp)
                            else Modifier
                                .fillMaxHeight()
                                .clickable {

                                }
                                .padding(13.dp)
                                .width(if (it == 0) 400.dp else 600.dp),
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