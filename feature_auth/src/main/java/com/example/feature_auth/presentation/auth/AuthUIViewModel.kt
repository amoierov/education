package com.example.feature_auth.presentation.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_auth.DI.ServiceLocator
import com.example.feature_auth.domain.AuthInteract
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthUIViewModel() : ViewModel() {
    //отправить в параметры
    private val authInteractror: AuthInteract = ServiceLocator.authInteract

    private val _authState = MutableStateFlow<AuthStateUI>(
        AuthStateUI.Login(
            email = "",
            password = "",
            enableButton = false
        )
    )
    val authState: StateFlow<AuthStateUI> = _authState

    private val _authEffects: MutableSharedFlow<AuthEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 1)

    val authEffects: SharedFlow<AuthEffect> = _authEffects

    fun updateEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.ClickRegister -> viewModelScope.launch {
                val authState = authState.value
                if (authState is AuthStateUI.SignUp) {
                    val email = authState.email
                    val password = authState.password
                    signup(email, password)
                }
            }

            AuthEvent.ClickLogin -> viewModelScope.launch {
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
                    AuthStateUI.Login(email, "", enableButton = false)
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


    private suspend fun login(email: String, password: String) {

        _authState.update {
            AuthStateUI.Loading(it.email, it.password)
        }
        delay(1000)
        val result = authInteractror.login(email, password)
        if (result.isSuccessful) {
            //Чтобы вернуться, удалить позже
            _authState.update {
                AuthStateUI.Login(
                    it.email,
                    it.password,
                    enableButton = isLoginValid(it.email, it.password)
                )
            }
            sendAuthEffect(AuthEffect.NavigateToHome)
        } else {
            _authState.update {
                AuthStateUI.Login(
                    it.email,
                    it.password,
                    enableButton = isLoginValid(it.email, it.password)
                )
            }
            sendAuthEffect(AuthEffect.ShowPopUpError(result.errorMessage))
        }
    }

    private suspend fun signup(email: String, password: String) {
        _authState.update {
            AuthStateUI.Loading(it.email, it.password)
        }
        delay(1000)
        val result = authInteractror.signup(email, password)
        if (result.isSuccessful) {
            //Чтобы вернуться, удалить позже
            _authState.update {
                AuthStateUI.SignUp(
                    it.email,
                    it.password,
                    enableButton = isLoginValid(it.email, it.password),
                    repeatPassword = it.repeatPassword
                )
            }
            sendAuthEffect(AuthEffect.NavigateToHome)
        } else {
            _authState.update {
                AuthStateUI.SignUp(
                    it.email,
                    it.password,
                    enableButton = isLoginValid(it.email, it.password),
                    repeatPassword = it.repeatPassword
                )
            }
            sendAuthEffect(AuthEffect.ShowPopUpError(result.errorMessage))
        }
    }


    private fun sendAuthEffect(effect: AuthEffect) {
        viewModelScope.launch {
            _authEffects.emit(effect)
        }

    }


    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
