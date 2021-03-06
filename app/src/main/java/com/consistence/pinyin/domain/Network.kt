package com.consistence.pinyin.domain

import com.consistence.pinyin.domain.pinyin.api.PinyinApi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun schedules(): SchedulerProvider = object : SchedulerProvider {
        override fun main(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        override fun thread(): Scheduler {
            return Schedulers.io()
        }
    }

    @Singleton @Provides
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    @Singleton @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
                .build()
    }

    @Singleton @Provides
    fun converterFactory(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Singleton @Provides
    fun retrofit(httpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://pinyin.consistence.io/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .build()
    }
}

@Module
class ApiModule {

    @Singleton
    @Provides
    internal fun pinyinApi(retrofit: Retrofit): PinyinApi = retrofit.create(PinyinApi::class.java)
}