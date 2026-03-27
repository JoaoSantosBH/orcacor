package com.jomar.senhorpintor.presentation.report

import android.app.Application
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import com.jomar.senhorpintor.base.BaseViewModel
import com.jomar.senhorpintor.base.CONSTANTS
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getConstantes
import com.jomar.senhorpintor.data.local.interactor.acessory.AcessoryInteractor
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserInteractor
import com.jomar.senhorpintor.data.local.interactor.header.HeaderInteractor
import com.jomar.senhorpintor.data.local.interactor.material.MaterialInteractor
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractor
import com.jomar.senhorpintor.dto.AmountDTO
import com.jomar.senhorpintor.dto.EstimateDTO
import com.jomar.senhorpintor.dto.ReportDTO
import com.jomar.senhorpintor.model.entities.*
import com.jomar.senhorpintor.util.CalculateMaterials.calculateAmountMaterialLiters
import com.jomar.senhorpintor.util.PrintInfo
import com.jomar.senhorpintor.util.PrintInfo.imprimeListagemAcessorios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ReportViewModel(
        app: Application,
        private val interactoA: AcessoryInteractor,
        private val interactorH: HeaderInteractor,
        private val interactorM: MaterialInteractor,
        private val interactor: RoomInteractor,
        private val userInteractor: UserInteractor
) : BaseViewModel(app) {
    lateinit var roomList: List<Room>
    var report = ReportDTO()
    var headerForSHow = BudgeReportHeader()
    var user = User()

    suspend fun saveBudgetReport(budget: Budget?) = withContext(Dispatchers.IO) {
        var seladora: AmountDTO?
        var tinta: AmountDTO?
        var amount: AmountDTO?
        var numComodos = 0
        var areaTinta: Float
        var areaTintaResult = 0f
        var areaMassaResult = 0f
        var areatotal = 0f
        var areaSeladora: Float
        var areaSeladoraResult = 0f
        val cons = getConstantes(App.instance, CONSTANTS)
        val constantes = cons[0]

        getRoomList(budget?.badgeNumber!!)
        var list: ArrayList<Room> = arrayListOf()
        for (room in roomList) {
            numComodos++
            if (room.wallIsNew!!){
                areaTinta = room.totalSquareMETER!! * room.demaos!!
            } else {
                areaTinta = room.totalSquareMETER!!
            }
            areaTintaResult += areaTinta
            if (room.wallIsNew!!) {
                areaSeladora = room.totalSquareMETER!! * room.demaos!!
                areaSeladoraResult += areaSeladora
                areaMassaResult = areaSeladoraResult
            }
            areatotal += room.totalSquareMETER!!
            list.add(room)
        }

        seladora = calculateAmountMaterialLiters(areaSeladoraResult, constantes.seladoraLitrom2)
        val seladoraInfo = PrintInfo.printAmountInfo(areaSeladoraResult, seladora, App.instance)
        val reportSeladora = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.seladora_cabecalho),
                areaSeladoraResult,
                constantes.seladoraLitrom2,
                seladoraInfo,
                seladora.dif,
                seladora.litros
        )
        tinta = calculateAmountMaterialLiters(areaTintaResult, constantes.tintaLitrom2)
        val tintaInfo = PrintInfo.printAmountInfo(areaTintaResult, tinta, App.instance)
        val reportTinta = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.tinta_cabecalho),
                areaTintaResult,
                constantes.tintaLitrom2,
                tintaInfo,
                tinta.dif,
                tinta.litros
        )
        amount = calculateAmountMaterialLiters(areaMassaResult, constantes.massaLitrom2)
        val massaInfo = PrintInfo.printAmountInfo(areaMassaResult, amount, App.instance)
        val reportMassa = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.massa_cabecalho),
                areaMassaResult,
                constantes.massaLitrom2,
                massaInfo,
                amount.dif,
                amount.litros
        )

        saveMaterial(reportSeladora)
        saveMaterial(reportTinta)
        saveMaterial(reportMassa)

        var header = BudgeReportHeader()
        header.badgeNumber = budget.badgeNumber
        header.date = budget.badgeDate
        header.totalArea = areatotal
        header.clientName = budget.badgeDestName
        header.clientEmail = budget.badgeDestEmail
        saveHeader(header)

        var acessories = BudgetReportAcessories()
        acessories.budgetNumber = budget.badgeNumber
        acessories.info = imprimeListagemAcessorios(areatotal, constantes, App.instance)

        saveAcessory(acessories)
    }

    suspend fun saveAcessory(acessories: BudgetReportAcessories) = withContext(Dispatchers.IO) {
        interactoA.insertAcessory(acessories)
    }

    suspend fun saveHeader(header: BudgeReportHeader) = withContext(Dispatchers.IO) {
        interactorH.insertHeader(header)
    }

    suspend fun saveMaterial(material: BudgetReportMaterial) = withContext(Dispatchers.IO) {
        interactorM.insertMaterial(material)
    }

    suspend fun getRoomList(number: String) = withContext(Dispatchers.IO) {
        roomList = interactor.getBudgetRooms(number)
    }

    suspend fun getHeader(number: String) = withContext((Dispatchers.IO)) {
        headerForSHow = interactorH.getHeaderFromBudgetNumber(number)
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        user = userInteractor.getUser()
    }

    suspend fun deleteRooms(number: String) = withContext(Dispatchers.IO){
        interactor.deleteBudgetRooms(number)
    }

    suspend fun showReport(budget: Budget?): ReportDTO = withContext(Dispatchers.IO) {

        var seladora: AmountDTO?
        var tinta: AmountDTO?
        var massa: AmountDTO?
        var numComodos = 0
        var totalDemao = 0
        var totalRoomsNumber = 0
        var areaTinta: Float
        var areaTintaResult = 0f
        var areaMassaResult = 0f
        var areatotal = 0f
        var areaSeladora: Float
        var areaSeladoraResult = 0f

        val cons = getConstantes(App.instance, CONSTANTS)
        val constantes = cons[0]
        getRoomList(budget?.badgeNumber!!)
        var list: ArrayList<Room> = arrayListOf()
        for (room in roomList) {
            totalDemao += room.demaos!!
            numComodos++
                areaTinta = room.totalSquareMETER!! * room.demaos!!
            areaTintaResult += areaTinta
            if (room.wallIsNew!!) {
                areaSeladora = room.totalSquareMETER!! * room.demaos
                areaSeladoraResult += areaSeladora
                areaMassaResult = areaSeladoraResult
            }
            areatotal += room.totalSquareMETER!!

            list.add(room)
        }
        totalRoomsNumber = numComodos
        report.rooms = list

        seladora = calculateAmountMaterialLiters(areaSeladoraResult, constantes.seladoraLitrom2)
        val seladoraInfo = PrintInfo.printAmountInfo(areaSeladoraResult, seladora, App.instance)
        val reportSeladora = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.seladora_cabecalho),
                areaSeladoraResult,
                constantes.seladoraLitrom2,
                seladoraInfo,
                seladora.dif,
                seladora.litros
        )
        tinta = calculateAmountMaterialLiters(areaTintaResult, constantes.tintaLitrom2)
        val tintaInfo = PrintInfo.printAmountInfo(areaTintaResult, tinta, App.instance)
        val reportTinta = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.tinta_cabecalho),
                areaTintaResult,
                constantes.tintaLitrom2,
                tintaInfo,
                tinta.dif,
                tinta.litros
        )
        massa = calculateAmountMaterialLiters(areaMassaResult, constantes.massaLitrom2)
        val massaInfo = PrintInfo.printAmountInfo(areaMassaResult, massa, App.instance)
        val reportMassa = BudgetReportMaterial(
                null,
                budget.badgeNumber,
                getString(R.string.massa_cabecalho),
                areaMassaResult,
                constantes.massaLitrom2,
                massaInfo,
                massa.dif,
                massa.litros
        )

        report?.orcamento = budget

        val materials = listOf(reportSeladora, reportTinta, reportMassa)
        report?.materials = materials
        getUser()
        getHeader(budget.badgeNumber)
        headerForSHow.badgeNumber = budget.badgeNumber
        headerForSHow.date = budget.badgeDate
        headerForSHow.totalArea = areatotal
        headerForSHow.painterName = user.nome
        headerForSHow.painterCelPhone = user.zapUsuario
        report?.header = headerForSHow

        var acessories = BudgetReportAcessories()
        acessories.budgetNumber = budget.badgeNumber
        acessories.info = imprimeListagemAcessorios(areatotal, constantes, App.instance)

        saveAcessory(acessories)
        report?.acessories = acessories

        val estimatedTime = 3 * totalRoomsNumber
        val suggestedPrize = areatotal * constantes.valorHoraPinturam2
        var estimateReport = getString(R.string.area_total) + " " + String.format("%.2f",areatotal) + "\n"

        estimateReport += getString(R.string.estimated_time) + " " + estimatedTime.toString() + " dias\n"
        estimateReport += getString(R.string.estimated_value) + " " + String.format("%.2f",suggestedPrize) + "\n"
        estimateReport += getString(R.string.hour_value) + " " + constantes.valorHoraPinturam2.toString()

        report.estimating = EstimateDTO(
                getString(R.string.estimated_title),
                estimateReport
        )

        return@withContext report
    }
}