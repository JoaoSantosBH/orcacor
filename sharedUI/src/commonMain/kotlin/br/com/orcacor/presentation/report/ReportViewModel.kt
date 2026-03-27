package br.com.orcacor.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcacor.domain.entity.Report
import br.com.orcacor.domain.usecase.report.GenerateReportUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class ReportState(
    val report: Report? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ReportIntent {
    data class Load(val budgetId: String) : ReportIntent
    object SharePdf : ReportIntent
}

sealed interface ReportEffect {
    data class ShareFile(val filePath: String) : ReportEffect
}

class ReportViewModel(
    private val generateReportUseCase: GenerateReportUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    private val _effects = Channel<ReportEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: ReportIntent) {
        when (intent) {
            is ReportIntent.Load -> load(intent.budgetId)
            ReportIntent.SharePdf -> sharePdf()
        }
    }

    private fun load(budgetId: String) {
        viewModelScope.launch {
            _state.value = ReportState(isLoading = true)
            generateReportUseCase.invoke(budgetId)
                .onSuccess { report ->
                    _state.value = ReportState(report = report)
                }
                .onFailure { error ->
                    _state.value = ReportState(error = error.message)
                }
        }
    }

    private fun sharePdf() {
        // PDF generation is platform-specific — will be handled via effect
        viewModelScope.launch {
            _effects.send(ReportEffect.ShareFile(""))
        }
    }
}
