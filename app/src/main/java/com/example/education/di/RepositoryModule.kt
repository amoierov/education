package com.example.education.di

import com.example.education.data.network.RepositoryImpl
import com.example.education.feature_auth.domain.AuthRepository
import com.example.education.feature_home.domain.CoursesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class RepositoryBindingModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryCourses(repositoryImpl: RepositoryImpl): CoursesRepository

    @Binds
    @Singleton
    abstract fun bindRepositoryAuth(repositoryImpl: RepositoryImpl): AuthRepository
}