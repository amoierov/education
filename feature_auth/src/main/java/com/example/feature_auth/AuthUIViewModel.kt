package com.example.feature_auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthUIViewModel() : ViewModel() {

    private val _authState = MutableStateFlow<AuthStateUI>(
        AuthStateUI.Login(
            email = "",
            password = "",
            enableButton = false
        )
    )
    val authState: StateFlow<AuthStateUI> = _authState

    fun updateEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.ClickRegister -> {
                val authState = authState.value
                if (authState is AuthStateUI.SignUp) {
                    val email = authState.email
                    val password = authState.password

                    if (authState.enableButton) {
                        signup(email, password)
                    }

                }
            }

            AuthEvent.ClickLogin -> {
                val authState = authState.value
                if (authState is AuthStateUI.Login) {
                    val email = authState.email
                    val password = authState.password
                    login(email, password)
                }
            }

            AuthEvent.ClickOd -> Unit
            AuthEvent.ClickVk -> Unit
            AuthEvent.ClickToLogin -> _authState.update {
                if (it is AuthStateUI.SignUp) {
                    val email = it.email
                    AuthStateUI.Login(email, "", false)
                } else
                    it
            }

            AuthEvent.ClickToRegistration -> _authState.update {
                if (it is AuthStateUI.Login) {
                    val email = it.email
                    AuthStateUI.SignUp(email, "", "", false)
                } else
                    it
            }

            is AuthEvent.InputEmail -> _authState.update {
                when (it) {
                    is AuthStateUI.SignUp -> {
                        val isValid = isSignUpValid(event.email, it.password, it.repeatPassword)
                        it.copy(email = event.email, enableButton = isValid)
                    }

                    is AuthStateUI.Login -> {
                        val isValid = isLoginValid(event.email, it.password)
                        it.copy(email = event.email, enableButton = isValid)
                    }

                    else -> it
                }
            }

            is AuthEvent.InputPassword -> _authState.update {
                when (it) {
                    is AuthStateUI.SignUp -> {
                        val isValid = isSignUpValid(it.email, event.password, it.repeatPassword)
                        it.copy(password = event.password, enableButton = isValid)
                    }

                    is AuthStateUI.Login -> {
                        val isValid = isLoginValid(it.email, event.password)
                        it.copy(password = event.password, enableButton = isValid)
                    }

                    else -> it
                }
            }

            is AuthEvent.InputRepeatPassword -> _authState.update {
                when (it) {
                    is AuthStateUI.SignUp -> {
                        val isValid = isSignUpValid(it.email, it.password, event.password)
                        it.copy(repeatPassword = event.password, enableButton = isValid)
                    }

                    else -> it
                }
            }

        }
    }

    private fun login(email: String, password: String): Boolean {
        return true
    }

    private fun signup(email: String, password: String): Boolean {
        return true
    }


    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun isSignUpValid(email: String, password: String, repeatPassword: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password) && password == repeatPassword
    }

    private fun isLoginValid(email: String, password: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password)
    }
}
