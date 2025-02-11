package com.example.feature_auth.DI

import com.example.feature_auth.domain.AuthInteract
import com.google.firebase.auth.FirebaseAuth


object ServiceLocator {
     val auth: FirebaseAuth = FirebaseAuth.getInstance()

     val authInteract = AuthInteract(auth)
}