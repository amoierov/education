package com.example.education.feature_auth.domain

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthInteract @Inject constructor(private val auth: FirebaseAuth, private val authRepository: AuthRepository) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult(true,null)
        } catch (e: Exception) {
            AuthResult(false, e.localizedMessage ?: "Неизвестная ошибка")
        }
    }
//        suspendCoroutine { continuation ->
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    val result = if (task.isSuccessful) {
//                        AuthResult(true, null)
//                    } else {
//                        val errorMessage = task.exception?.localizedMessage ?: "Неизвестная ошибка"
//                        AuthResult(false, errorMessage)
//                    }
//                    continuation.resumeWith(Result.success(result))
//                }
//        }


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


    suspend fun authenticate(): Boolean {
        return authRepository.authenticate()
    }
}

data class AuthResult(val isSuccessful: Boolean, val errorMessage: String?)