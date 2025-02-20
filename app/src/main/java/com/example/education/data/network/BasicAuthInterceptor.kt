package com.example.education.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BasicAuthInterceptor @Inject constructor() : Interceptor {
    private val credentials = Credentials.basic(
        "Y4p19SSSqyHs4Inil27ACrMTNYxDEK2ei9wyF2nf",  // Ваш client_id
        "I7CvuxonmNIInkCA5Nhwhdc5MVCQhAS9gLxG9ETarT7T95SM8vEmjXKhgBpaX6rIvc5d1aBD6qY1NmH5vkeytP8qzBXglqcMd1aiGWJfxePRlgtHvTnruHnKUrmr9NZ6" // Ваш client_secret
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", credentials) // Добавляем Basic Auth
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
        return chain.proceed(request)
    }
}