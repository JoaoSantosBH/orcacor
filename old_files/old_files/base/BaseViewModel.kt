package com.jomar.senhorpintor.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.jomar.senhorpintor.base.FlowState.Companion.loading
import com.jomar.senhorpintor.model.Response
import com.jomar.senhorpintor.util.ThreadContextProvider
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


open class BaseViewModel(app: Application) : AndroidViewModel(app), KoinComponent,
        LifecycleObserver {
    private val job = SupervisorJob()
    val contextProvider: ThreadContextProvider by inject()
    private val resources: ResourcesReferences by inject()

    val coroutineScope = CoroutineScope(contextProvider.io + job)

    inline fun <reified T> createFlowState() = MutableLiveData<FlowState<T>>()

    @Throws(RuntimeException::class)
    inline fun <reified B, reified T> launchRequest(
            flowState: MutableLiveData<FlowState<T>>,
            crossinline call: suspend () -> Response<B>,
            crossinline onFailure: (Throwable) -> Unit = { },
            crossinline onSuccess: (B) -> T? = {
                if (it is T) it else null
            }
    ): Job? {
        if (flowState.value?.status == FlowState.Status.LOADING) return null
        flowState.value = loading()
        return launchRequestAndTreatResponse(
                call = call,
                onSuccess = {
                    onSuccess(it)?.let { data -> flowState.postSuccess(data) }
                            ?: throw RuntimeException()
                },
                onFailure = { onFailure(it); flowState.postFailure(it) }
        )
    }

    inline fun <T> launchRequestAndTreatResponse(
            crossinline call: suspend () -> Response<T>,
            crossinline onFailure: (Throwable) -> Unit = {},
            crossinline onSuccess: (T) -> Unit
    ) =
            coroutineScope.launch(contextProvider.io) {
                val response = call.invoke()
                withContext(contextProvider.ui) {
                    when (response) {
                        is Response.Failure -> onFailure(response.throwable)
                        is Response.Success -> onSuccess(response.data)
                    }
                }
            }


    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun getString(@StringRes stringId: Int) = resources.getString(stringId)

}