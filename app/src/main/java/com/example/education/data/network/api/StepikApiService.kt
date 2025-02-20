package com.example.education.data.network.api

import com.example.education.data.network.models.AuthResponse
import com.example.education.data.network.models.CourseResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StepikApiService {

    @FormUrlEncoded
    @POST("oauth2/token/")
    suspend fun authenticate(
        @Field("grant_type") grantType: String = "client_credentials"
    ): Response<AuthResponse>

    @GET("api/courses/")
    suspend fun getCourses(
        @Query("page_size") pageSize: Int = 10,
        @Query("is_paid") isPaid: Boolean = true,
        @Query("is_popular") isPopular: Boolean = true,
//        @Header("Authorization") authHeader: String
    ): Response<CourseResponse>

}







