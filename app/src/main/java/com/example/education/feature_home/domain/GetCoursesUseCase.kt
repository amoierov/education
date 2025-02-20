package com.example.education.feature_home.domain

import com.example.education.data.network.models.Course
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(private val coursesRepository: CoursesRepository) {
    suspend fun getCourses(): List<Course>? {
        return coursesRepository.getCourses()
    }
}