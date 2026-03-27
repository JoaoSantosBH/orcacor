package com.jomar.senhorpintor.dto

import com.jomar.senhorpintor.model.entities.*

data class ReportDTO (
        var orcamento: Budget? =null,
        var user: User?=null,
        var header: BudgeReportHeader?=null,
        var rooms: ArrayList<Room>?= arrayListOf(),
        var acessories: BudgetReportAcessories?=null,
        var materials: List<BudgetReportMaterial>?= listOf(),
        var estimating: EstimateDTO?=null

)