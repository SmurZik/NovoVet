package presentation.main

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.utils.Constant

@Stable
class ShareDataState {

    private var shareData by mutableStateOf(Pair(0, Constant.EMPTY))

    fun updateShareData(value: Pair<Int, String>) {
        shareData = value
    }

    fun shareData(): Pair<Int, String> = shareData
}