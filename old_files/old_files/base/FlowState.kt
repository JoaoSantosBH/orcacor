package com.jomar.senhorpintor.base


data class FlowState<D>(val status: Status, val data: D? = null, val throwable: Throwable? = null) {
    companion object {
        fun <T> success(data: T? = null): FlowState<T> = FlowState(Status.SUCCESS, data)
        fun <T> error(throwable: Throwable): FlowState<T> =
                FlowState(Status.ERROR, throwable = throwable)
        fun <T> loading(): FlowState<T> = FlowState(Status.LOADING)
        fun <T> neutral(): FlowState<T> = FlowState(Status.NEUTRAL)
    }
    enum class Status { SUCCESS, ERROR, LOADING, NEUTRAL}
}