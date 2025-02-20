package com.example.education.feature_home.domain

import com.example.education.data.network.models.Course


interface CoursesRepository {
    suspend fun getCourses(): List<Course>?
}