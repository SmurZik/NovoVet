package data

import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat

class DataImpl {
    private val connection: Connection =
        DriverManager.getConnection("jdbc:mysql://localhost/novo_vet", "root", "camur2403")

    fun getOutpatientCard(search: String, searchBy: String): Pair<List<String>, Pair<Int, Int>> {
        val currentNote = mutableListOf("Дата", "Клиент", "Питомец", "Стоимость")
        var countLines = 1
        var petId = 1
        val getOutpatient =
            if (search.isNotEmpty()) "select visit.date, person.secondName, person.firstName, person.lastName, pet.nickname, visit.sum, pet.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id where $searchBy REGEXP '^$search' order by date DESC"
            else "select visit.date, person.secondName, person.firstName, person.lastName, pet.nickname, visit.sum, pet.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id order by date DESC"
        val query = connection.prepareStatement(getOutpatient)
        val outpatient = query.executeQuery()
        val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
        while (outpatient.next()) {
            countLines++
            currentNote.add(formatForDateNow.format(outpatient.getDate(1)) + " " + outpatient.getTime(1).toString())
            currentNote.add(
                outpatient.getString(2) + " " + outpatient.getString(3) + " " + outpatient.getString(
                    4
                )
            )
            currentNote.add(outpatient.getString(5))
            currentNote.add(outpatient.getDouble(6).toString())
            petId = outpatient.getInt(7)
        }
        return Pair(currentNote, Pair(countLines, petId))
    }

    fun getPersons(search: String, searchBy: String): Pair<List<String>, Int> {
        val searchPerson =
            if (search.isEmpty()) "SELECT * FROM journal" else "SELECT * FROM journal where $searchBy REGEXP '^$search'"
        val searchPersonQuery = connection.prepareStatement(searchPerson)
        val currentNote = mutableListOf("Клиент", "Питомец")
        val persons = searchPersonQuery.executeQuery()
        var countLines = 1
        while (persons.next()) {
            countLines++
            currentNote.add(
                persons.getString(7) + " " + persons.getString(6) + " " + persons.getString(
                    8
                )
            )
            currentNote.add(persons.getString(3))
//            currentNote.add(persons.getString(9))
//            currentNote.add(persons.getString(2))
//            currentNote.add(persons.getString(4))
//            currentNote.add(persons.getString(5))
//                currentNote.add(persons.getDate(10).toString())
//                currentNote.add(persons.getTime(10).toString())
        }
        return Pair(currentNote, countLines)
    }

    fun getVisitDates(id: Int): List<String> {
        val dates = mutableListOf<String>()

        val searchVisitDates = "select date from visit where petId = $id"
        val query = connection.prepareStatement(searchVisitDates)
        val result = query.executeQuery()
        while (result.next()) {
            val tempDate = result.getDate(1)
            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
            dates.add(formatForDateNow.format(tempDate))
            dates.add(result.getTime(1).toString())
        }
        return dates
    }

    fun getInfoByPetId(data: Pair<Int, String>): Pair<Pair<List<String>, List<String>>, Int> {
        if (data.first == 0) return Pair(Pair(listOf(), listOf()), 0)
        val searchByNickname = "select nickname, kind, breed, male, age from pet where id = ${data.first}"
        val searchByNicknameQuery = connection.prepareStatement(searchByNickname)
        val result = searchByNicknameQuery.executeQuery()
        val note = mutableListOf<String>()
        val visit = mutableListOf<String>()
        while (result.next()) {
            note.add(result.getString(1))
            note.add(result.getString(2))
            note.add(result.getString(3))
            note.add(result.getString(4))
            note.add(result.getString(5))
        }
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        val searchDate = dateFormat.format(formatDate.parse(data.second))
        val searchVisitInfo = "SELECT * FROM visit where petId = ${data.first} and date = '$searchDate'"
        val searchVisitInfoQuery = connection.prepareStatement(searchVisitInfo)
        val resultVisit = searchVisitInfoQuery.executeQuery()
        while (resultVisit.next()) {
//            visit.add(resultVisit.getDouble(4).toString())
            visit.add(resultVisit.getString(5))
            visit.add(resultVisit.getString(6))
            visit.add(resultVisit.getString(7))
            visit.add(resultVisit.getString(8))
            visit.add(resultVisit.getString(9))
            visit.add(resultVisit.getString(10))
            visit.add(resultVisit.getString(11))
            visit.add(resultVisit.getString(12))
            visit.add(resultVisit.getString(13))
            visit.add(resultVisit.getString(14))
            visit.add(resultVisit.getString(15))
            val tempDate = resultVisit.getDate(3)
            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
            visit.add(formatForDateNow.format(tempDate))
            visit.add(resultVisit.getTime(3).toString())
        }
        return Pair(Pair(note, visit), data.first)
    }

    fun setPersonInfo(
        firstName: String,
        secondName: String,
        lastName: String,
        nickname: String,
        address: String,
        phoneNumber: String,
        breed: String,
        kind: String,
        male: String,
        age: String,
    ) {
        val setPerson =
            "insert into journal (secondName, firstName, lastName, phoneNumber, breed, nickname, adress, kind, male, age) values ('$secondName', '$firstName', '$lastName', '$phoneNumber', '$breed', '$nickname', '$address', '$kind', '$male', '$age');"
        val setPersonQuery = connection.prepareCall(setPerson)
        setPersonQuery.execute()
    }

    fun setVisitInfo() {

    }

    fun readableDateFormat(date: String): String {
        val template = date.split(":")
        val tempDate = template[0] + ":" + template[1]
        return tempDate
    }
}