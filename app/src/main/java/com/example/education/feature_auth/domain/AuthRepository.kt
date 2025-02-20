package com.example.education.feature_auth.domain

interface AuthRepository {
    suspend fun authenticate(): Boolean
}