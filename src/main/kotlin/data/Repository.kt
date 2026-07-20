package data

import presentation.outpatient.IllnessHistoryViewModel
import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat
import kotlin.math.ceil

object Repository {
    private val connection: Connection =
        DriverManager.getConnection("jdbc:mysql://localhost/novo_vet", "root", "camur2403")

    fun getOutpatientCard(
        search: String,
        searchBy: String,
        date: java.util.Date
    ): Pair<Pair<List<String>, List<Int>>, Pair<Int, List<Int>>> {
        val currentNote = mutableListOf("Дата", "Клиент", "Питомец", "Стоимость")
        val clientIds = mutableListOf<Int>()
        var countLines = 1
        val dateString = "01.01.1999"
        val formatDate = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatDate.parse(dateString)
        val petIds = mutableListOf<Int>()
        val getOutpatient =
            if (search.isNotEmpty()) "select visit.date, person.secondName, person.firstName, person.lastName, pet.nickname, visit.sum, pet.id, person.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id where $searchBy REGEXP '^$search' order by date DESC"
            else if (date != formattedDate) "select visit.date, person.secondName, person.firstName, person.lastName, pet.nickname, visit.sum, pet.id, person.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id order by date DESC"
            else "select visit.date, person.secondName, person.firstName, person.lastName, pet.nickname, visit.sum, pet.id, person.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id order by date DESC limit 100"
        val query = connection.prepareStatement(getOutpatient)
        val outpatient = query.executeQuery()
        val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
        while (outpatient.next()) {
            if (date != formattedDate && formatForDateNow.format(date) == formatForDateNow.format(outpatient.getDate(1))) {
                countLines++
                currentNote.add(formatForDateNow.format(outpatient.getDate(1)) + " " + outpatient.getTime(1).toString())
                currentNote.add(
                    outpatient.getString(2) + " " + outpatient.getString(3) + " " + outpatient.getString(
                        4
                    )
                )
                currentNote.add(outpatient.getString(5))
                currentNote.add(outpatient.getString(6))
                petIds.add(outpatient.getInt(7))
                clientIds.add(outpatient.getInt(8))
            } else if (date == formattedDate) {
                countLines++
                currentNote.add(formatForDateNow.format(outpatient.getDate(1)) + " " + outpatient.getTime(1).toString())
                currentNote.add(
                    outpatient.getString(2) + " " + outpatient.getString(3) + " " + outpatient.getString(
                        4
                    )
                )
                currentNote.add(outpatient.getString(5))
                currentNote.add(outpatient.getString(6))
                petIds.add(outpatient.getInt(7))
                clientIds.add(outpatient.getInt(8))
            }
        }
        return Pair(Pair(currentNote, clientIds), Pair(countLines, petIds))
    }

    fun getVaccineJournal(
        search: String,
        searchBy: String,
        date: java.util.Date
    ): Pair<List<String>, Pair<Int, List<Int>>> {
        val currentNote = mutableListOf("Дата", "Клиент", "Питомец", "Название вакцины")
        var countLines = 1
        val visitIds = mutableListOf<Int>()
        val dateString = "01.01.1999"
        val formatDate = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatDate.parse(dateString)
        val toSearch = if (searchBy == "secondName" || searchBy == "firstName") "client" else "pet"
        val getVaccine =
            if (search.isNotEmpty()) "select date, client, pet, vac, visitId from vaccine where $toSearch REGEXP '$search' order by date DESC"
            else if (date != formattedDate) "select date, client, pet, vac, visitId from vaccine order by date DESC"
            else "select date, client, pet, vac, visitId from vaccine order by date DESC limit 100"
        val query = connection.prepareStatement(getVaccine)
        val vaccine = query.executeQuery()
        val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
        while (vaccine.next()) {
            if (date != formattedDate && formatForDateNow.format(date) == formatForDateNow.format(vaccine.getDate(1))) {
                countLines++
                currentNote.add(formatForDateNow.format(vaccine.getDate(1)) + " " + vaccine.getTime(1).toString())
                currentNote.add(vaccine.getString(2))
                currentNote.add(vaccine.getString(3))
                currentNote.add(vaccine.getString(4))
                visitIds.add(vaccine.getInt(5))
            } else if (date == formattedDate) {
                countLines++
                currentNote.add(formatForDateNow.format(vaccine.getDate(1)) + " " + vaccine.getTime(1).toString())
                currentNote.add(vaccine.getString(2))
                currentNote.add(vaccine.getString(3))
                currentNote.add(vaccine.getString(4))
                visitIds.add(vaccine.getInt(5))
            }
        }
        return Pair(currentNote, Pair(countLines, visitIds))
    }

    fun updatePrice(price: Int, visitId: Int) {
        val updatePrice =
            "update visit set sum = $price where id = $visitId"
        val updatePriceQuery = connection.prepareStatement(updatePrice)
        updatePriceQuery.execute()
    }

    fun getClientInfo(clientId: Int): Pair<Pair<List<String>, List<String>>, Pair<Int, List<Int>>> {
        val getClientInfo =
            "select person.id, person.secondName, person.firstName, person.lastName, person.address, person.phone, person.email, pet.nickname, pet.kind, pet.breed, MAX(visit.date), pet.id from person join pet on person.id = pet.ownerId join visit on visit.petId = pet.id where person.id = $clientId group by pet.id "
        val getClientInfoQuery = connection.prepareStatement(getClientInfo)
        var info = mutableListOf("Кличка", "Вид", "Порода", "Последний визит")
        var currentNote = mutableListOf<String>()
        val clientInfo = getClientInfoQuery.executeQuery()
        val petIds = mutableListOf<Int>()
        var countLines = 1
        while (clientInfo.next()) {
            countLines++
            currentNote.add(clientInfo.getInt(1).toString())
            currentNote.add(clientInfo.getString(2))
            currentNote.add(clientInfo.getString(3))
            currentNote.add(clientInfo.getString(4))
            currentNote.add(clientInfo.getString(5))
            currentNote.add(clientInfo.getString(6))
            currentNote.add(clientInfo.getString(7))

            info.add(clientInfo.getString(8))
            info.add(clientInfo.getString(9))
            info.add(clientInfo.getString(10))
            val tempDate = clientInfo.getDate(11)
            val formatForDateNow = SimpleDateFormat("dd.MM.yyyy")
            info.add(formatForDateNow.format(tempDate) + " " + clientInfo.getTime(11).toString())
            petIds.add(clientInfo.getInt(12))
        }
        if (currentNote.isEmpty()) {
            val getClientInfoWithoutVisit =
                "select person.id, person.secondName, person.firstName, person.lastName, person.address, person.phone, person.email, pet.nickname, pet.kind, pet.breed from person join pet on person.id = pet.ownerId where person.id = $clientId limit 1"
            val getClientInfoWithoutVisitQuery = connection.prepareStatement(getClientInfoWithoutVisit)
            val infoWithoutVisit = mutableListOf("Кличка", "Вид", "Порода", "Последний визит")
            val currentNoteWithoutVisit = mutableListOf<String>()
            val clientInfoWithoutVisit = getClientInfoWithoutVisitQuery.executeQuery()
            var countLinesWithoutVisit = 1
            while (clientInfoWithoutVisit.next()) {
                countLinesWithoutVisit++

                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getInt(1).toString())
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(2))
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(3))
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(4))
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(5))
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(6))
                currentNoteWithoutVisit.add(clientInfoWithoutVisit.getString(7))

                infoWithoutVisit.add(clientInfoWithoutVisit.getString(8))
                infoWithoutVisit.add(clientInfoWithoutVisit.getString(9))
                infoWithoutVisit.add(clientInfoWithoutVisit.getString(10))
                infoWithoutVisit.add("-")
                infoWithoutVisit.add("")
//                petIds.add(clientInfoWithoutVisit.getInt(12))
            }
            currentNote = currentNoteWithoutVisit
            info = infoWithoutVisit
            countLines = countLinesWithoutVisit
        }
        return Pair(Pair(currentNote, info), Pair(countLines, petIds))
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
        val searchByNickname = "select nickname, kind, breed, male, age, vac, vacDate from pet where id = ${data.first}"
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
            note.add(result.getString(6))
            note.add(result.getString(7))
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
            visit.add(resultVisit.getString(4))
            visit.add(resultVisit.getString(16))
            visit.add(resultVisit.getString(5))
            visit.add(resultVisit.getString(6))
            visit.add(resultVisit.getString(17))
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

    fun getPetId(
        nickname: String,
        kind: String,
        breed: String,
        male: String,
        age: String,
        clientId: Int
    ): Int {
        var petId = 0
        val getPetId =
            "select id from pet where nickname = '$nickname' and breed = '$breed' and kind = '$kind' and male = '$male' and age = '$age' and ownerId = $clientId"
        val getPetIdQuery = connection.prepareStatement(getPetId)
        val result = getPetIdQuery.executeQuery()
        while (result.next()) {
            petId = result.getInt(1)
        }
        return petId
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
        email: String,
        clientId: Int
    ) {
        val setClientInfo =
            "update person set secondName = '$secondName', firstName = '$firstName', lastName = '$lastName', address = '$address', phone = '$phone', email = '$email' where id = $clientId;"
        val setClientInfoQuery = connection.prepareStatement(setClientInfo)
        setClientInfoQuery.execute()
    }

    fun setVacInfo(
        info: String,
        petId: Int,
        date: String,
        client: String,
        pet: String,
        visitId: Int
    ) {
        val day = date.split(" ").first()
        var dateBefore = ""
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        val formattedDate = dateFormat.format(formatDate.parse(date))
        val getDay =
            "select vacDate from pet where id = $petId"
        val getDayQuery = connection.prepareStatement(getDay)
        val result = getDayQuery.executeQuery()
        while (result.next()) {
            dateBefore = result.getString(1)
        }
        val setVacInfo =
            "update pet set vac = '$info', vacDate = '$day' where id = $petId"
        val setVacInfoQuery = connection.prepareStatement(setVacInfo)
        if (dateBefore != "") {
            val dateFormat1 = SimpleDateFormat("dd.MM.yyyy").parse(day)
            val dateBeforeFormat = SimpleDateFormat("dd.MM.yyyy").parse(dateBefore)
            if (dateFormat1.before(dateBeforeFormat)) {
                val setNewVaccine =
                    "INSERT INTO vaccine (date, client, pet, vac, visitId) VALUES ('$formattedDate', '$client', '$pet', '$info', $visitId);"
                connection.prepareStatement(setNewVaccine).execute()
            }
            if (dateFormat1.after(dateBeforeFormat) || dateFormat1.equals(dateBeforeFormat)) {
                setVacInfoQuery.execute()
                if (dateFormat1.equals(dateBeforeFormat)) {
                    val updateVaccine =
                        "update vaccine set date = '$formattedDate', client = '$client', pet = '$pet', vac = '$info' where visitId = $visitId"
                    connection.prepareStatement(updateVaccine).execute()
                } else {
                    val setNewVaccine =
                        "INSERT INTO vaccine (date, client, pet, vac, visitId) VALUES ('$formattedDate', '$client', '$pet', '$info', $visitId);"
                    connection.prepareStatement(setNewVaccine).execute()
                }
            }
        } else {
            setVacInfoQuery.execute()
            val setNewVaccine =
                "INSERT INTO vaccine (date, client, pet, vac, visitId) VALUES ('$formattedDate', '$client', '$pet', '$info', $visitId);"
            connection.prepareStatement(setNewVaccine).execute()
        }
    }

    fun getSchedule(time: Long, scheduleList: List<String>): List<String> {
        val scheduleResult = mutableListOf("Время", "Клиент", "Телефон", "Цель посещения", "Занято")
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("HH:mm")
        val formattedDate = dateFormat.format(java.util.Date(time))
        var count = 0
        val checkEmptySchedule =
            "SELECT date FROM appointment WHERE DATE_FORMAT(date, '%d.%m.%Y') = '$formattedDate'"
        val checkQuery = connection.prepareStatement(checkEmptySchedule)
        val checkResult = checkQuery.executeQuery()
        while (checkResult.next()) {
            count++
            break
        }
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val dateFormatToDB = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        if (count == 0) {
            scheduleList.drop(1).forEach {
                val formattedDateToDB = dateFormatToDB.format(formatDate.parse("$formattedDate $it"))
                val insertSchedule =
                    "INSERT INTO appointment (date, client, phoneNumber, goal) VALUES ('$formattedDateToDB', '', '', '')"
                val insertQuery = connection.prepareStatement(insertSchedule)
                insertQuery.execute()
            }
        }
        val getSchedule =
            "SELECT date, client, phoneNumber, goal, busy FROM appointment WHERE DATE_FORMAT(date, '%d.%m.%Y') = '$formattedDate'"
        val query = connection.prepareStatement(getSchedule)
        val result = query.executeQuery()
        while (result.next()) {
            scheduleResult.add(timeFormat.format(result.getTime(1)))
            scheduleResult.add(result.getString(2))
            scheduleResult.add(result.getString(3))
            scheduleResult.add(result.getString(4))
            scheduleResult.add(result.getString(5))
        }
        return scheduleResult
    }

    fun setVisitInfo(
        id: Int,
        isNew: Boolean,
        petId: Int,
        date: String,
        sum: Int,
        ownerWords: String,
        temperature: String,
        extra: String,
        diagnosis: String,
        completed: String,
        recommendations: String,
        illnessHistoryViewModel: IllnessHistoryViewModel,
        complete: String
    ) {
        val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
        val formattedDate = dateFormat.format(formatDate.parse(date))
        var doctors = ""
        if (illnessHistoryViewModel.checked1()) doctors += "Мурзина И.В."
        if (illnessHistoryViewModel.checked1() && (illnessHistoryViewModel.checked2() || illnessHistoryViewModel.checked3() || illnessHistoryViewModel.checked4())) doctors += ", "
        if (illnessHistoryViewModel.checked2()) doctors += "Кленкова С.В."
        if (illnessHistoryViewModel.checked2() && (illnessHistoryViewModel.checked3() || illnessHistoryViewModel.checked4())) doctors += ", "
        if (illnessHistoryViewModel.checked3()) doctors += "Камышенцева С.Вл."
        if (illnessHistoryViewModel.checked4() && illnessHistoryViewModel.checked3()) doctors += ", "
        if (illnessHistoryViewModel.checked4()) doctors += "Францкевич Э.Р."
        val commonFeeling =
            if (illnessHistoryViewModel.feelingNorm()) "Удовлетворительное"
            else if (illnessHistoryViewModel.feelingHard()) "Тяжелое"
            else if (illnessHistoryViewModel.feelingVeryHard()) "Крайне тяжелое"
            else ""
        val appetite =
            if (illnessHistoryViewModel.appetiteSave()) "Сохранен"
            else if (illnessHistoryViewModel.appetiteLack()) "Отсутствует"
            else if (illnessHistoryViewModel.appetiteRarely()) "Снижен"
            else ""
        val vomit =
            if (illnessHistoryViewModel.vomitNo()) "Нет"
            else if (illnessHistoryViewModel.vomitYesRarely()) "Да (редко)"
            else if (illnessHistoryViewModel.vomitYesOften()) "Да (часто)"
            else ""
        val defication =
            if (illnessHistoryViewModel.deficationNorm()) "Нормальная"
            else if (illnessHistoryViewModel.deficationRarely()) "Неоформленная (редко)"
            else if (illnessHistoryViewModel.deficationOften()) "Неоформленная (часто)"
            else if (illnessHistoryViewModel.deficationLack()) "Нет"
            else ""
        val urination =
            if (illnessHistoryViewModel.urinationNorm()) "Нормальное"
            else if (illnessHistoryViewModel.urinationLack()) "Отсутствует"
            else if (illnessHistoryViewModel.urinationOften()) "Учащенное"
            else ""
        if (isNew) {
            val setNewVisit =
                "INSERT INTO visit (petId, date, sum, ownerWords, commongFeeling, temperature, appetite, vomit, defication, urination, extra, diagnosis, completed, recommendations, doctors, weight, complete) VALUES ($petId, '$formattedDate', '$sum', '$ownerWords', '$commonFeeling', '$temperature', '$appetite', '$vomit', '$defication', '$urination', '$extra', '$diagnosis', '$completed', '$recommendations', '$doctors', '${illnessHistoryViewModel.weight()}', '$complete');"
            val setVisitQuery = connection.prepareStatement(setNewVisit)
            setVisitQuery.execute()
            var id = 0
            val getNewId =
                "select id from visit where petId = '$petId' and date = '$formattedDate'"
            val query = connection.prepareStatement(getNewId)
            val result = query.executeQuery()
            while (result.next()) {
                id = result.getInt(1)
            }
            illnessHistoryViewModel.updateVisitId(id)
        } else {
            val updateVisit =
                "update visit set petId = '$petId', date = '$formattedDate', sum = $sum, ownerWords = '$ownerWords', commongFeeling = '$commonFeeling', temperature = '$temperature', appetite = '$appetite', vomit = '$vomit', defication = '$defication', urination = '$urination', extra = '$extra', diagnosis = '$diagnosis', completed = '$completed', recommendations = '$recommendations', doctors = '$doctors', weight = '${illnessHistoryViewModel.weight()}', complete = '$complete'  where id = $id;"
            val setVisitQuery = connection.prepareStatement(updateVisit)
            setVisitQuery.execute()
        }
    }

    fun readableDateFormat(date: String): String {
        val template = date.split(":")
        val tempDate = template[0] + ":" + template[1]
        return tempDate
    }

    fun addPetInfo(
        nickname: String,
        kind: String,
        breed: String,
        male: String,
        age: String,
        clientId: Int
    ) {
        val setNewPet =
            "INSERT INTO pet (nickname, kind, breed, male, age, ownerId, vac, vacDate) VALUES ('$nickname', '$kind', '$breed', '$male', '$age', '$clientId', '', '');"
        val setNewClientQuery = connection.prepareStatement(setNewPet)
        setNewClientQuery.execute()
    }

    fun addClientInfo(
        secondName: String,
        firstName: String,
        lastName: String,
        address: String,
        phone: String,
        email: String
    ): Int {
        var clientId = 0
        val setNewClient =
            "INSERT INTO person (secondName, firstName, lastName, address, phone, email) VALUES ('$secondName', '$firstName', '$lastName', '$address', '$phone', '$email');"
        val setNewClientQuery = connection.prepareStatement(setNewClient)
        setNewClientQuery.execute()
        val getClientId =
            "SELECT id from person where secondName = '$secondName' and firstName = '$firstName' and lastName = '$lastName' and address = '$address' and phone = '$phone' and email = '$email';"
        val getClientIdQuery = connection.prepareStatement(getClientId)
        val result = getClientIdQuery.executeQuery()
        while (result.next()) {
            clientId = result.getInt(1)
        }
        return clientId
    }

    fun getServices(name: String): Pair<List<String>, Pair<Int, List<Int>>> {
        val currentNote = mutableListOf("Название услуги", "Стоимость")
        val serviceIds = mutableListOf<Int>()
        var countLines = 1
        val getServices = if (name.isBlank())
            "SELECT name, price, id from services order by name ASC;"
        else "SELECT name, price, id from services WHERE name REGEXP '$name' order by name ASC;"
        val query = connection.prepareStatement(getServices)
        val result = query.executeQuery()
        while (result.next()) {
            countLines++
            currentNote.add(result.getString(1))
            currentNote.add(result.getString(2))
            serviceIds.add(result.getInt(3))
        }
        return currentNote to Pair(countLines, serviceIds)
    }

    fun getDrugs(): Pair<List<String>, Pair<Int, List<Int>>> {
        val currentNote = mutableListOf("Название препарата", "Единицы измерения", "Стоимость")
        val drugIds = mutableListOf<Int>()
        var countLines = 1
        val getDrugs =
            "SELECT name, price, id, measure from drugs order by name ASC;"
        val query = connection.prepareStatement(getDrugs)
        val result = query.executeQuery()
        while (result.next()) {
            countLines++
            currentNote.add(result.getString(1))
            currentNote.add(result.getString(4))
            currentNote.add(result.getString(2))
            drugIds.add(result.getInt(3))
        }
        return currentNote to Pair(countLines, drugIds)
    }

    fun addService(name: String, price: String) {
        val setNewService =
            "insert into services (name, price) values ('$name', '$price');"
        val setNewServiceQuery = connection.prepareStatement(setNewService)
        setNewServiceQuery.execute()
    }

    fun deleteService(id: Int) {
        val deleteService = "DELETE FROM services WHERE id = $id;"
        val deleteServiceQuery = connection.prepareStatement(deleteService)
        deleteServiceQuery.execute()
    }

    fun addDrug(name: String, price: String, measure: String) {
        val setNewDrug =
            "insert into drugs (name, price, measure) values ('$name', '$price', '$measure');"
        val setNewDrugQuery = connection.prepareStatement(setNewDrug)
        setNewDrugQuery.execute()
    }

    fun deleteDrug(id: Int) {
        val deleteDrug = "DELETE FROM drugs WHERE id = $id;"
        val deleteDrugQuery = connection.prepareStatement(deleteDrug)
        deleteDrugQuery.execute()
    }

    fun editService(name: String, price: String, id: Int) {
        val editService =
            "update services set name = '$name', price = '$price' where id = $id"
        val editServiceQuery = connection.prepareStatement(editService)
        editServiceQuery.execute()
    }

    fun editDrug(name: String, price: String, measure: String, id: Int) {
        val editDrug =
            "update drugs set name = '$name', price = '$price', measure = '$measure' where id = $id"
        val editDrugQuery = connection.prepareStatement(editDrug)
        editDrugQuery.execute()
    }

    fun getMeasure(name: String): String {
        var measure = ""
        val getMeasure =
            "select measure from drugs where name = '$name'"
        val query = connection.prepareStatement(getMeasure)
        val result = query.executeQuery()
        while (result.next()) {
            measure = result.getString(1)
        }
        return measure
    }

    fun getServiceByFirstLetter(name: String, vaccine: Boolean): List<String> {
        val services = mutableListOf<String>()
        val getServices =
            "select name from services where name REGEXP '$name'"
        val query = connection.prepareStatement(getServices)
        val result = query.executeQuery()
        while (result.next()) {
            if (!vaccine) {
                services.add(result.getString(1))
            } else if (vaccine && result.getString(1).contains(Regex("акцинация"))) {
                services.add(result.getString(1))
            }
        }
        return services
    }

    fun getDrugByFirstLetter(name: String): List<String> {
        val drugs = mutableListOf<String>()
        val getDrugs =
            "select name from drugs where name REGEXP '$name'"
        val query = connection.prepareStatement(getDrugs)
        val result = query.executeQuery()
        while (result.next()) {
            drugs.add(result.getString(1))
        }
        return drugs
    }

    fun getClientsInfo(search: String, searchBy: String): Pair<Pair<List<Int>, List<String>>, Int> {
        val currentNote = mutableListOf("Клиент", "Питомцы")
        val clientIds = mutableListOf<Int>()
        var searchTo = ""
        searchTo = if (searchBy == "secondName" || searchBy == "firstName") "person.$searchBy" else "pet.nickname"
        var countLines = 1
        val getClients =
            if (search.isNotEmpty()) "select person.secondName, person.firstName, person.lastName, person.id, group_concat(pet.nickname separator ', ') from person join pet on person.id = pet.ownerId where $searchTo REGEXP '$search' group by person.secondName, person.firstName, person.lastName, person.id order by person.secondName ASC"
            else "select person.secondName, person.firstName, person.lastName, person.id, group_concat(pet.nickname separator ', ') from person join pet on person.id = pet.ownerId group by person.secondName, person.firstName, person.lastName, person.id order by person.secondName ASC"
        val query = connection.prepareStatement(getClients)
        val outpatient = query.executeQuery()
        while (outpatient.next()) {
            countLines++
            currentNote.add(
                outpatient.getString(1) + " " + outpatient.getString(2) + " " + outpatient.getString(
                    3
                )
            )
            currentNote.add(outpatient.getString(5))
            clientIds.add(outpatient.getInt(4))
        }
        return Pair(Pair(clientIds, currentNote), countLines)
    }

    fun addCompleted(
        service: String,
        drugs: List<String>,
        amounts: List<String>,
        visitId: Int,
        servicePrice: String,
        ownerDrug: List<String>
    ) {
        val setNewCompleted =
            "insert into completed (service, drugs, amounts, visitId, servicePrice, ownerDrug) values ('$service', '$drugs', '$amounts', $visitId, '$servicePrice', '$ownerDrug');"
        val setNewCompletedQuery = connection.prepareStatement(setNewCompleted)
        setNewCompletedQuery.execute()
    }

    fun editCompleted(
        service: String,
        drugs: List<String>,
        amounts: List<String>,
        id: Int,
        servicePrice: String,
        ownerDrug: List<String>
    ) {
        val editCompleted =
            "update completed set service = '$service', drugs = '$drugs', amounts = '$amounts', servicePrice = '$servicePrice', ownerDrug = '$ownerDrug' where id = $id"
        val editCompletedQuery = connection.prepareStatement(editCompleted)
        editCompletedQuery.execute()
    }

    fun getRealServicePrice(service: String, visitId: Int): String {
        var price = ""
        val getPrice =
            "select servicePrice from completed where service = '$service' and visitId = $visitId"
        val getPriceQuery = connection.prepareStatement(getPrice)
        val result = getPriceQuery.executeQuery()
        while (result.next()) {
            price = result.getString(1)
        }
        return price
    }

    fun getPrice(
        service: List<String>,
        drugs: List<List<String>>,
        amounts: List<List<String>>,
        visitId: Int,
        add: Boolean,
        priceSum: Int,
        ownerDrugs: List<List<String>>
    ): Int {
        var price = 0
        if (!add) {
            service.forEach {
                if (it != "Услуга") {
                    val getPrice =
                        "select servicePrice from completed where service = '$it' and visitId = $visitId limit 1"
                    val getPriceQuery = connection.prepareStatement(getPrice)
                    val result = getPriceQuery.executeQuery()
                    while (result.next()) {
                        if (result.getString(1) != "") {
                            price += result.getString(1).toInt()
                        }
                    }
                }
            }
        } else {
            println("here")
            val getPrice =
                "select servicePrice from completed where service = '${service.last()}' and visitId = $visitId limit 1"
            val getPriceQuery = connection.prepareStatement(getPrice)
            val result = getPriceQuery.executeQuery()
            while (result.next()) {
                if (result.getString(1) != "") {
                    price += result.getString(1).toInt()
                }
            }
            price += priceSum

        }
        drugs.forEachIndexed { index, strings ->
            strings.forEachIndexed { secondIndex, name ->
                if (ownerDrugs[index][secondIndex] != "true") {
                    val getPrice =
                        "select price from drugs where name = '$name' limit 1"
                    val getPriceQuery = connection.prepareStatement(getPrice)
                    val result = getPriceQuery.executeQuery()
                    while (result.next()) {

                        price += ceil(result.getString(1).toInt() * amounts[index][secondIndex].toDouble()).toInt()
                    }
                }
            }
        }
        when (price.toString().last()) {
            '1' -> price += 4
            '2' -> price += 3
            '3' -> price += 2
            '4' -> price += 1
            '6' -> price += 4
            '7' -> price += 3
            '8' -> price += 2
            '9' -> price += 1
        }
        return price
    }

    fun getServicePrice(service: String): String {
        var price = ""
        val getServicePrice =
            "select price from services where name = '$service'"
        val getServicePriceQuery = connection.prepareStatement(getServicePrice)
        val result = getServicePriceQuery.executeQuery()
        while (result.next()) {
            price = result.getString(1)
        }
        return price
    }

    fun getCompleted(visitId: Int): Pair<List<String>, Pair<Int, List<Int>>> {
        val completion = mutableListOf<String>()
        var countLines = 0
        val ids = mutableListOf<Int>()
        val getCompleted =
            "select service, drugs, amounts, ownerDrug, id from completed where visitId = $visitId"
        val getCompletedQuery = connection.prepareStatement(getCompleted)
        val result = getCompletedQuery.executeQuery()
        while (result.next()) {
            countLines++
            completion.add(result.getString(1))
            completion.add(result.getString(2))
            completion.add(result.getString(3))
            completion.add(result.getString(4))
            ids.add(result.getInt(5))
        }
        return completion to Pair(countLines, ids)
    }

    fun parseCompleted(
        completion: List<String>,
        count: Int
    ): Pair<List<String>, List<List<List<String>>>> {
        val services = mutableListOf<String>()
        val drugs = mutableListOf<List<String>>()
        val amounts = mutableListOf<List<String>>()
        val ownerDrugs = mutableListOf<List<String>>()
        services.add("Услуга")
        drugs.add(listOf("Препараты"))
        amounts.add(listOf("Количество"))
        ownerDrugs.add(listOf("Своё"))
        for (i in 1..count) {
            services.add(completion[0 + (i - 1) * 4])
            drugs.add(completion[1 + (i - 1) * 4].drop(1).dropLast(1).split(", "))
            amounts.add(completion[2 + (i - 1) * 4].drop(1).dropLast(1).split(", "))
            ownerDrugs.add(completion[3 + (i - 1) * 4].drop(1).dropLast(1).split(", "))
        }
        var tempOwnerDrugs = ownerDrugs.toMutableList()
        drugs.forEachIndexed { index, list ->
            if (list.size > ownerDrugs[index].size) {
                tempOwnerDrugs[index] = MutableList(list.size - ownerDrugs[index].size + 1) { "false" }
            }
        }
        return Pair(services, listOf(drugs, amounts, tempOwnerDrugs))
    }
}