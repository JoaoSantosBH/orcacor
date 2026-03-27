package com.jomar.senhorpintor.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


object ServiceClientFactory {

    inline fun <reified T> createClient(
            url: String, okHttpClient: OkHttpClient?, coroutineAdapter: CoroutineCallAdapterFactory
    ): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addClient(okHttpClient)
                .addConverterFactory(UnitConverterFactory)
                .addCallAdapterFactory(coroutineAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(T::class.java)
    }

    fun Retrofit.Builder.addClient(okHttpClient: OkHttpClient?): Retrofit.Builder =
            when (okHttpClient != null) {
                true -> client(okHttpClient)
                else -> this
            }

    fun createOkHttpClient(
            requestInterceptor: RequestInterceptor
    ): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
        addAuthorizationHeaders(clientBuilder)

        with(clientBuilder) {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
        }
        return clientBuilder.build()
    }

    fun createHttpLoggingInterceptor() = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.HEADERS
        level = HttpLoggingInterceptor.Level.BODY
        this
    }

    object UnitConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
                type: Type, annotations: Array<out Annotation>,
                retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            return if (type == Unit::class.java) UnitConverter else null
        }

        private object UnitConverter : Converter<ResponseBody, Unit> {
            override fun convert(value: ResponseBody) {
                value.close()
            }
        }
    }

    private fun addAuthorizationHeaders(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            val original = chain.request()

            val request = original
                    .newBuilder()
                    .method(original.method, original.body)
            fillAuthHeaders(request)

            val response = chain.proceed(request.build())

            updateAuthHeaders(response.headers)

            response
        }
    }

    private fun fillAuthHeaders(builder: Request.Builder) {

    }

    private fun updateAuthHeaders(headers: Headers) {

    }

}