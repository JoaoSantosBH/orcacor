package br.com.orcacor.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.safeLaunch(
    onError: (Throwable) -> Unit = { e ->
        println("ViewModel error: ${e::class.simpleName}: ${e.message}")
        println(e.stackTraceToString())
    },
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(
    context = CoroutineExceptionHandler { _, e -> onError(e) },
    block = block
)