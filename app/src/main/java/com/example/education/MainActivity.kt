package com.example.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.education.navigation.Navigator

class MainActivity : AppCompatActivity(), Navigator {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//        val navController = navHostFragment.navController


    }



    override fun navigateTo(destinationId: Int) {
        findNavController(viewId = R.id.nav_host_fragment_container).navigate(destinationId)
    }
}



