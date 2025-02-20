package com.example.education

import android.app.Application
import com.example.education.di.AppComponent
import com.example.education.di.DaggerAppComponent
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this) // Инициализация Firebase
        appComponent = DaggerAppComponent.create()

    }

}


//val Context.appComponent: AppComponent get() = when (this) {
//    is MyApplication -> appComponent
//    else -> this.applicationContext.appComponent
//}

