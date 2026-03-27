package com.jomar.senhorpintor.model

sealed class Response<out T> {
    class Success<out T>(val data: T) : Response<T>()
    class Failure(val throwable: Throwable) : Response<Nothing>()

    suspend fun <B> flatMap(
            onFailure: suspend (Throwable) -> Response<B> = { Failure(it) },
            onSuccess: suspend (T) -> Response<B>
    ): Response<B> =
            when (this) {
                is Success -> onSuccess(data)
                is Failure -> onFailure(throwable)
            }

    suspend fun <B> map(
            onFailure: suspend (Throwable) -> Throwable = { it },
            onSuccess: suspend (T) -> B
    ): Response<B> = when (this) {
        is Success -> Success(onSuccess(data))
        is Failure -> Failure(onFailure(throwable))
    }

}