package com.example.feature_auth.domain

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.suspendCoroutine

class AuthInteract(private val auth: FirebaseAuth) {


    suspend fun login(email: String, password: String): AuthResult =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val result = if (task.isSuccessful) {
                        AuthResult(true, null)
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Неизвестная ошибка"
                        AuthResult(false, errorMessage)
                    }
                    continuation.resumeWith(Result.success(result))
                }
        }


    suspend fun signup(email: String, password: String) =
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val result = if (task.isSuccessful) {
                        AuthResult(true, null)
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Неизвестная ошибка"
                        AuthResult(false, errorMessage)
                    }
                    continuation.resumeWith(Result.success(result))
                }
        }

}

data class AuthResult(val isSuccessful: Boolean, val errorMessage: String?)