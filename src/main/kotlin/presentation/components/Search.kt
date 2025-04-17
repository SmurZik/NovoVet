package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.main.State

@Composable
fun search(state: State, expanded: Boolean, onExpandedChange: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 400.dp, top = 5.dp)
            .background(
                color = /*Color(218, 189, 171)*/ /*Color.Blue*/ Color(
                    127,
                    199,
                    255
                ),
                shape = CutCornerShape(5.dp)
            )
    ) {
        Row(
            modifier = Modifier.background(color = Color.Transparent)
        ) {
            Text(
                "Поиск по: ${state.addText()}",
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "More"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier
                        .background(color = Color.Cyan)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            state.updateSearchBy("secondName")
                            state.updateAddText(" фамилии")
                            onExpandedChange(false)
                        }
                    ) {
                        Text("Фамилия")
                    }
                    DropdownMenuItem(
                        onClick = {
                            state.updateSearchBy("firstName")
                            state.updateAddText(" имени")
                            onExpandedChange(false)
                        }
                    ) {
                        Text("Имя")
                    }
                    DropdownMenuItem(
                        onClick = {
                            state.updateAddText(" кличке")
                            state.updateSearchBy("nickname")
                            onExpandedChange(false)
                        }
                    ) {
                        Text("Кличка")
                    }
                }
            }
            IconButton(
                onClick = {
                    state.updateIsSearch(false)
                    state.updateSearchText("")
                    state.updateSearchBy("secondName")
                    state.updateAddText(" фамилии")
                },
                modifier = Modifier.padding(start = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        }
        val onlyRussianLetter = Regex("[а-яА-Я]*")
        val onlyEnglishLetter = Regex("[a-zA-Z]*")
        TextField(
            value = state.searchText(),
            onValueChange = {
                if (onlyRussianLetter.matches(it) || onlyEnglishLetter.matches(it)) state.updateSearchText(it)
            },
            label = { Text("Введите слово для поиска") },
            modifier = Modifier.width(290.dp).height(50.dp)
        )
    }
}