package com.example.education.di

import com.example.education.data.network.AuthInterceptor
import com.example.education.data.network.BasicAuthInterceptor
import com.example.education.data.network.TokenProvider
import com.example.education.data.network.api.StepikApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(BasicAuthInterceptor()) // Basic Auth только для запроса токена
            .build()
    }

    @Provides
    @Singleton
    @Named("api")
    fun provideApiHttpClient(tokenProvider: TokenProvider): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(tokenProvider)) // Bearer Token для API-запросов
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofitForAuth(@Named("auth") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://stepik.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("api")
    fun provideRetrofitForApi(@Named("api") client: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://stepik.org/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthApiService(@Named("auth") retrofit: Retrofit): StepikApiService {
        return retrofit.create(StepikApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("api")
    fun provideApiService(@Named("api") retrofit: Retrofit): StepikApiService {
        return retrofit.create(StepikApiService::class.java)
    }
}