package com.example.education.data.network

import android.graphics.RenderNode
import android.util.Log
import com.example.education.data.network.api.StepikApiService
import com.example.education.data.network.models.Course
import com.example.education.feature_auth.domain.AuthRepository
import com.example.education.feature_home.domain.CoursesRepository
import javax.inject.Inject
import javax.inject.Named

class RepositoryImpl @Inject constructor(
    @Named("auth") private val authService: StepikApiService,
    @Named("api") private val apiService: StepikApiService,
    private val tokenProvider: TokenProvider
) : AuthRepository, CoursesRepository {
    override suspend fun authenticate(): Boolean {
        val response = authService.authenticate()
        var token: String? = null
        return if (response.isSuccessful) {
            val authResponse = response.body()
            token = authResponse?.accessToken
            token?.let {
                tokenProvider.saveToken(it)  // Сохраняем токен
            }
            true
        } else {
            false
        }
    }



    override suspend fun getCourses(): List<Course>? {
//        val response = apiService.getCourses(authHeader = authHeader)
        val response = apiService.getCourses()
        val ls :List<Course>? = response.body()?.courses
        if (response.isSuccessful) {
            Log.d("API Response", "Courses: ${response.body()?.courses}")
            return response.body()?.courses
        } else {
            Log.e("API Error", "Error: ${response.errorBody()?.string()}")
            return null
        }
    }
}
