package com.example.education.di

import androidx.lifecycle.ViewModelProvider
import com.example.education.MainActivity
import com.example.education.feature_auth.presentation.auth.AuthFragment
import com.example.education.feature_home.presentation.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class, ViewModelModule::class,
    NetworkModule::class, RepositoryBindingModule::class])
interface AppComponent {
    fun firebaseAuth(): FirebaseAuth
    fun inject(activity: MainActivity)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: HomeFragment)
    fun viewModelFactory(): ViewModelProvider.Factory
}







