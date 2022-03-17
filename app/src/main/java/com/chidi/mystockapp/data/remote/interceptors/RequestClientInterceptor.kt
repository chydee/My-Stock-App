package com.chidi.mystockapp.data.remote.interceptors

import com.chidi.mystockapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("token", BuildConfig.IexKey)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}