import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import navcontroller.*
import state.StateWrapper

@Composable
fun app() {
    var shareData by remember { mutableStateOf(Pair(0, "")) }
    var nicknameTab by remember { mutableStateOf("Новый") }
    val tabNaming = mapOf(
        Screen.JournalScreen.name to "Амбулаторные приемы",
        Screen.OutpatientCardScreen.name to "$nicknameTab - История болезни"
    )
    val navController by rememberNavController(Screen.JournalScreen.name)
    var tabs by remember { mutableStateOf(setOf(Screen.JournalScreen.name)) }
    var activeTab by remember { mutableStateOf(Screen.JournalScreen.name) }

    val outpatientScreenState = StateWrapper().initialOutpatientScreenState()
    val petInfoState = StateWrapper().initialPetInfoState()
    val illnessHistoryState = StateWrapper().initialIllnessHistoryState()

    Box(
        modifier = Modifier.fillMaxHeight().width(300.dp).background(color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.Cyan)
        ) {
            Text(
                text = "Лечебная деятельность",
                fontSize = 20.sp,
                modifier = Modifier.padding(all = 8.dp)
            )
            for (i in 0..2) {
                Row(
                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                ) {
                    Image(
                        painter = when (i) {
                            0 -> painterResource("/vacc.png")
                            1 -> painterResource("/pharm.png")
                            else -> painterResource("/warn.png")
                        },
                        contentDescription = "Buttons",
                        modifier = Modifier.size(25.dp, 25.dp)
                    )
                    Text(
                        text = when (i) {
                            0 -> "Вакцинации"
                            1 -> "Амбулаторные приемы"
                            else -> "Уведомить о вакцинациях"
                        },
                        fontSize = 16.sp,
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .padding(end = 8.dp, start = 8.dp)
                            .clickable {

                            }
                    )
                }
            }
            Text(
                text = "Учет лекарств",
                fontSize = 20.sp,
                modifier = Modifier.padding(all = 8.dp)
            )
            Row(
                modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource("/doc.png"),
                    contentDescription = "Doc",
                    modifier = Modifier.size(25.dp, 25.dp)
                )
                Text(
                    text = "Приходные документы",
                    fontSize = 16.sp,
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(end = 8.dp, start = 8.dp)
                        .clickable {

                        }
                )
            }
            Text(
                text = "Справочники",
                fontSize = 20.sp,
                modifier = Modifier.padding(all = 8.dp)
            )
            for (i in 0..3) {
                Row(
                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                ) {
                    Image(
                        painter = when (i) {
                            0 -> painterResource("/person.png")
                            1 -> painterResource("/animal.png")
                            2 -> painterResource("/pills.png")
                            else -> painterResource("/vet.png")
                        },
                        contentDescription = "Buttons",
                        modifier = Modifier.size(25.dp, 25.dp)
                    )
                    Text(
                        text = when (i) {
                            0 -> "Владельцы"
                            1 -> "Животные"
                            2 -> "Лекарства"
                            else -> "Услуги"
                        },
                        fontSize = 16.sp,
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .padding(end = 8.dp, start = 8.dp)
                            .clickable {

                            }
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.padding(start = 298.dp).fillMaxHeight().width(1.dp),
            color = Color.Gray
        )
    }
    Row(
        modifier = Modifier.padding(start = 300.dp).background(color = Color.LightGray).fillMaxWidth().height(40.dp)
    ) {
        tabs.forEach {
            Button(
                onClick = {
                    navController.navigate(it)
                    activeTab = it
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (activeTab == it) Color.Cyan else Color.LightGray),
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = tabNaming[it]!!,
                    letterSpacing = 0.sp
                )
                IconButton(
                    onClick = {
                        if (tabs.size != 1) tabs -= it
                        activeTab = tabs.last()
                        navController.navigate(activeTab)
                    },
                    modifier = Modifier.align(Alignment.Top).size(16.dp, 16.dp).padding(top = 2.dp, start = 4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(16.dp, 16.dp)
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.padding(start = 300.dp, top = 40.dp).fillMaxSize()) {
        customNavigationHost(
            navController = navController,
            shareData = shareData,
            onNicknameTabChange = { nicknameTab = it },
            onDataChange = { shareData = it },
            onTabsAdd = { tabs += it },
            onActiveTabChange = { activeTab = it },
            onTabsSub = { tabs -= it },
            outpatientScreenState = outpatientScreenState,
            petInfoState = petInfoState,
            illnessHistoryState = illnessHistoryState
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = WindowState(WindowPlacement.Maximized), title = "НовоВет") {
        app()
    }
}