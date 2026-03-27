package com.jomar.senhorpintor.extensions


import com.google.gson.Gson
import com.jomar.senhorpintor.BuildConfig
import com.jomar.senhorpintor.model.ErrorResponse
import com.jomar.senhorpintor.model.Response
import com.jomar.senhorpintor.util.NoNetworkException
import com.jomar.senhorpintor.util.NoNetworkWithServer
import com.jomar.senhorpintor.util.RemoteDataException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.Request


suspend fun <T : Any> apiCall(
        call: suspend () -> T
): Response<T> =
        try {
            Response.Success(call())
        } catch (exception: Exception) {
            Response.Failure(
                    when (exception) {
                        is UnknownHostException, is SocketTimeoutException -> NoNetworkWithServer()
                        is SocketException -> NoNetworkException()
                        is HttpException -> exception.tryToParseError()
                        else -> exception
                    }
            )
        }

fun Request.Builder.addHeaderNotBlank(name: String, value: String?) =
        when (!value.isNullOrBlank()) {
            true -> header(name, value)
            else -> this
        }

fun HttpException.toRemoteDataException(message: String? = null): RemoteDataException {
    message?.let {
        android.util.Log.d("Token", message)
    }
    return RemoteDataException(
            code = this.code(),
            messageError = message ?: this.message()
    )
}

fun HttpException.tryToParseError() = try {
    if (this.response() != null) {
        val errorResponse = response()?.errorBody()?.string().toString().toObjectOf<ErrorResponse>()
        val message = errorResponse.errors?.firstOrNull() ?: ""
        toRemoteDataException(message)
    } else {
        if (BuildConfig.DEBUG) this.printStackTrace()
        RemoteDataException(code(), message())
    }
} catch (exception: Exception) {
    if (BuildConfig.DEBUG) exception.printStackTrace()
    toRemoteDataException()
}

inline fun <reified T> String.toObjectOf(): T {
    return Gson().fromJson(
            this,
            T::class.java
    )
}


