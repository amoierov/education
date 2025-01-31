package com.example.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feature_auth.RegistrationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.place_holder, RegistrationFragment()).commit()
    }
}



