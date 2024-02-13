package navcontroller

import androidx.compose.runtime.Composable
import screen.buildOutpatientCard
import screen.markup

@Composable
fun customNavigationHost(
    navController: NavController,
    shareData: Int,
    onDataChange: (Int) -> Unit,
    onNicknameTabChange: (String) -> Unit,
    onTabsAdd: (String) -> Unit,
    onTabsSub: (String) -> Unit,
    onActiveTabChange: (String) -> Unit,
    isSearchChange: (Boolean) -> Unit,
    onSearchChange: (String) -> Unit,
    onSearchByChange: (String) -> Unit,
    onAddTextChange: (String) -> Unit,
    isSearch: Boolean,
    addText: String,
    search: String,
    searchBy: String,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    kind: String,
    onKindChange: (String) -> Unit,
    breed: String,
    onBreedChange: (String) -> Unit,
    male: String,
    onMaleChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    save: Boolean,
    onSaveChange: (Boolean) -> Unit
) {
    NavigationHost(navController) {

        composable(Screen.JournalScreen.name) {
            markup(
                navController,
                onDataChange,
                onTabsAdd,
                onActiveTabChange,
                onNicknameTabChange,
                isSearchChange,
                onSearchByChange,
                onSearchChange,
                onAddTextChange,
                isSearch,
                addText,
                search,
                searchBy
            )
        }

        composable(Screen.OutpatientCardScreen.name) {
            buildOutpatientCard(
                navController,
                shareData,
                onActiveTabChange,
                onTabsSub,
                onTabsAdd,
                nickname,
                onNicknameChange,
                kind,
                onKindChange,
                breed,
                onBreedChange,
                male,
                onMaleChange,
                age,
                onAgeChange,
                save,
                onSaveChange
            )
        }
    }.build()
}