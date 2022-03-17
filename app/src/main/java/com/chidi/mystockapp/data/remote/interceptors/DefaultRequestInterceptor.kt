package com.chidi.mystockapp.data.remote.interceptors

import com.chidi.mystockapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class DefaultRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("token", BuildConfig.FinnApiKey)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}