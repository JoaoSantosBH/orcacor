package com.jomar.senhorpintor.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<FlowState<T>>.postSuccess(data: T) =
        postValue(FlowState(status = FlowState.Status.SUCCESS, data = data))

fun <T> MutableLiveData<FlowState<T>>.postFailure(throwable: Throwable) =
        postValue(FlowState(status = FlowState.Status.ERROR, throwable = throwable))


fun <T> LiveData<FlowState<T>>.handleWithFlow(
        lifecycleOwner: LifecycleOwner,
        onLoading: () -> Unit = {},
        onFailure: (Throwable) -> Unit = {},
        onComplete: (() -> Unit) = {},
        onSuccess: (T) -> Unit
) {
    this.removeObservers(lifecycleOwner)
    observe(lifecycleOwner, Observer { flowState ->
        when (flowState?.status) {
            FlowState.Status.LOADING -> onLoading()
            FlowState.Status.ERROR -> flowState.throwable?.let {
                onFailure(it)
                onComplete.invoke()
            }
            FlowState.Status.SUCCESS -> flowState.data?.let {
                onSuccess(it)
                onComplete.invoke()
            }
            FlowState.Status.NEUTRAL -> Unit
        }
    })
}