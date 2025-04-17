package presentation.main

interface State {
    fun updateIsSearch(value: Boolean)

    fun getIsSearch(): Boolean

    fun updateSearchText(value: String)

    fun searchText(): String

    fun updateSearchBy(value: String)

    fun searchBy(): String

    fun updateAddText(value: String)

    fun addText(): String
}