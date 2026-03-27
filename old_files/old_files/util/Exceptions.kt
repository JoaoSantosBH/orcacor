package com.jomar.senhorpintor.util

class NoNetworkException(message: String? = "No Internet Connection") : Exception(message)

class NoNetworkWithServer(message: String? = "Connection Lost") : Exception(message)

class RemoteDataException(val code: Int? = null, val messageError: String? = null): Exception(messageError)

class InvalidUpdate(message: String? = "não é válido"): Exception(message)