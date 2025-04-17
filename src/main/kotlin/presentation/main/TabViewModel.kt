package presentation.main

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import presentation.main.navcontroller.Screen

@Stable
class TabViewModel {
    private var nicknameTab by mutableStateOf("Новый")
    private var tabs by mutableStateOf(setOf(Screen.JournalScreen.name))
    private var activeTab by mutableStateOf(Screen.JournalScreen.name)

    fun updateNicknameTab(value: String) {
        nicknameTab = value
    }

    fun nicknameTab(): String = nicknameTab

    fun addTab(value: String) {
        tabs += value
    }

    fun subTab(value: String) {
        tabs -= value
    }

    fun tabs(): Set<String> = tabs

    fun updateActiveTab(value: String) {
        activeTab = value
    }

    fun activeTab(): String = activeTab
}