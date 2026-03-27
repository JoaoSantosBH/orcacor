package com.jomar.senhorpintor.service

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Interceptors", "Intercept")
        var request = chain.request()



        Log.d("Interceptors", "Intercept - Before proceed()")
        val response = chain.proceed(request)
        Log.d("Interceptors", "Intercept - After proceed()")



        return response
    }
}