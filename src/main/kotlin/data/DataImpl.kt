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
// здесь надо получать id клиента, а не petId. нужно взять где-нибудь выше и обновлять clientInfoState, и сюда потом просто передать его
    fun getClientInfo(petId: Int): Pair<Pair<List<String>, List<String>>, Int> {
        val getClientInfo = "select person.id, person.secondName, person.firstName, person.lastName, person.address, person.phone, pet.nickname, pet.kind, pet.breed, visit.date from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id order by date DESC limit 1"
        val getClientInfoQuery = connection.prepareStatement(getClientInfo)
        val info = mutableListOf("Кличка", "Вид", "Порода", "Последний визит")
        val currentNote = mutableListOf<String>()
        val clientInfo = getClientInfoQuery.executeQuery()
        var countLines = 1
        while (clientInfo.next()) {
            countLines++

            currentNote.add(clientInfo.getInt(1).toString())
            currentNote.add(clientInfo.getString(2))
            currentNote.add(clientInfo.getString(3))
            currentNote.add(clientInfo.getString(4))
            currentNote.add(clientInfo.getString(5))
            currentNote.add(clientInfo.getString(6))

            info.add(clientInfo.getString(7))
            info.add(clientInfo.getString(8))
            info.add(clientInfo.getString(9))
            // сделать дату нормально датой, а не строкой, чтобы получить привычный формат
            info.add(readableDateFormat(clientInfo.getString(10)))
        }
        return Pair(Pair(currentNote, info), countLines)
    }

    fun getVisitDates(id: Int): List<Pair<String, String>> {
        val dates = mutableListOf<Pair<String, String>>()

        val searchVisitDates = "select date from visit where petId = $id"
        val query = connection.prepareStatement(searchVisitDates)
        val result = query.executeQuery()
        while (result.next()) {
            val tempDate = result.getDate(1)
            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
            val tempTime = result.getTime(1).toString().split(":")
            val time = tempTime[0] + ":" + tempTime[1]
            dates.add(formatForDateNow.format(tempDate) to time)
        }
        return dates
    }

    fun getInfoByPetId(data: Pair<Int, String>): Pair<Pair<List<String>, List<String>>, Pair<Int, Int>> {
        if (data.first == 0) return Pair(Pair(listOf(), listOf()), 0 to 0)
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
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        val searchDate = dateFormat.format(formatDate.parse(data.second))
        val searchVisitInfo = "SELECT * FROM visit where petId = ${data.first} and date = '$searchDate'"
        val searchVisitInfoQuery = connection.prepareStatement(searchVisitInfo)
        val resultVisit = searchVisitInfoQuery.executeQuery()
        var visitId = 0
        while (resultVisit.next()) {
            visitId = resultVisit.getInt(1)
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
        return Pair(Pair(note, visit), data.first to visitId)
    }

    fun setPetInfo(
        nickname: String,
        breed: String,
        kind: String,
        male: String,
        age: String,
        id: Int
    ) {
        val setPerson =
            "update pet set breed = '$breed', nickname = '$nickname', kind = '$kind', male = '$male', age = '$age' where id = $id;"
        val setPersonQuery = connection.prepareStatement(setPerson)
        setPersonQuery.execute()
    }

    fun setClientInfo(
        secondName: String,
        firstName: String,
        lastName: String,
        address: String,
        phone: String,
        clientId: Int
    ) {
        val setClientInfo =
            "update person set secondName = '$secondName', firstName = '$firstName', lastName = '$lastName', address = '$address', phone = '$phone' where id = $clientId;"
        val setClientInfoQuery = connection.prepareStatement(setClientInfo)
        setClientInfoQuery.execute()
    }

    fun setVisitInfo(
        id: Int,
        isNew: Boolean,
        petId: Int,
        date: String,
        sum: String,
        ownerWords: String,
        commonFeeling: String,
        temperature: String,
        appetite: String,
        vomit: String,
        defication: String,
        urination: String,
        extra: String,
        diagnosis: String,
        completed: String,
        recommendations: String
    ) {
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        val formattedDate = dateFormat.format(formatDate.parse(date))
        if (isNew) {
            val setNewVisit =
                "INSERT INTO visit (petId, date, sum, ownerWords, commongFeeling, temperature, appetite, vomit, defication, urination, extra, diagnosis, completed, recommendations) VALUES ($petId, '$formattedDate', '$sum', '$ownerWords', '$commonFeeling', '$temperature', '$appetite', '$vomit', '$defication', '$urination', '$extra', '$diagnosis', '$completed', '$recommendations');"
            val setVisitQuery = connection.prepareStatement(setNewVisit)
            setVisitQuery.execute()
        } else {
            val updateVisit =
                "update visit set petId = '$petId', date = '$formattedDate', sum = '$sum', ownerWords = '$ownerWords', commongFeeling = '$commonFeeling', temperature = '$temperature', appetite = '$appetite', vomit = '$vomit', defication = '$defication', urination = '$urination', extra = '$extra', diagnosis = '$diagnosis', completed = '$completed', recommendations = '$recommendations'  where id = $id;"
            val setVisitQuery = connection.prepareStatement(updateVisit)
            setVisitQuery.execute()
        }
    }

    fun readableDateFormat(date: String): String {
        val template = date.split(":")
        val tempDate = template[0] + ":" + template[1]
        return tempDate
    }

    fun getClientsInfo(search: String, searchBy: String): Pair<List<String>, Int> {
        val currentNote = mutableListOf("Клиент", "Питомцы")
        var countLines = 1
        val getClients =
            if (search.isNotEmpty()) "select person.secondName, person.firstName, person.lastName, pet.nickname from person join pet on person.id = pet.ownerId where $searchBy REGEXP '^$search' order by nickname DESC"
            else "select person.secondName, person.firstName, person.lastName, pet.nickname from person join pet on person.id = pet.ownerId order by nickname DESC"
        val query = connection.prepareStatement(getClients)
        val outpatient = query.executeQuery()
        while (outpatient.next()) {
            countLines++
            currentNote.add(
                outpatient.getString(1) + " " + outpatient.getString(2) + " " + outpatient.getString(
                    3
                )
            )
            currentNote.add(outpatient.getString(4))
        }
        return Pair(currentNote, countLines)
    }
}