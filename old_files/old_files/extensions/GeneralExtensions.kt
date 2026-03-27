package com.jomar.senhorpintor.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jomar.senhorpintor.util.NoNetworkException
import com.jomar.senhorpintor.util.NoNetworkWithServer
import com.jomar.senhorpintor.util.RemoteDataException

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun handleErrors(throwable: Throwable) {
    throwable.printStackTrace()
    when (throwable) {
        is OutOfMemoryError -> null
        is RemoteDataException -> null
        is NoNetworkException -> null
        is NoNetworkWithServer -> null
        else -> when (!throwable.message.isNullOrBlank()) {
            true -> null
            false -> null
        }
    }
}


