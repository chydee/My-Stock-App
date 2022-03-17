package com.chidi.mystockapp.core.di

import com.chidi.mystockapp.BuildConfig
import com.chidi.mystockapp.core.constants.StockAppConstants.CONNECT_TIMEOUT
import com.chidi.mystockapp.core.constants.StockAppConstants.READ_TIMEOUT
import com.chidi.mystockapp.core.constants.StockAppConstants.WRITE_TIMEOUT
import com.chidi.mystockapp.data.remote.CandleService
import com.chidi.mystockapp.data.remote.CompaniesService
import com.chidi.mystockapp.data.remote.QuoteService
import com.chidi.mystockapp.data.remote.interceptors.DefaultRequestInterceptor
import com.chidi.mystockapp.data.remote.interceptors.RequestClientInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @see[NetworkModule] acts as a object provider and creator
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(): DefaultRequestInterceptor = DefaultRequestInterceptor()

    @Singleton
    @Provides
    fun provideRequestClientInterceptor(): RequestClientInterceptor = RequestClientInterceptor()

    @Singleton
    @Provides
    fun provideCandleService(logger: HttpLoggingInterceptor, requestInterceptor: DefaultRequestInterceptor): CandleService {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(requestInterceptor)
            .addInterceptor(logger)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(CandleService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuoteService(logger: HttpLoggingInterceptor, requestClientInterceptor: RequestClientInterceptor): QuoteService {
        val okHttpQuoteClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(requestClientInterceptor)
            .addInterceptor(logger)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpQuoteClient)
            .baseUrl(BuildConfig.IEX_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(QuoteService::class.java)
    }

    @Singleton
    @Provides
    fun provideCompaniesService(): CompaniesService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.Github_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(CompaniesService::class.java)
    }
}