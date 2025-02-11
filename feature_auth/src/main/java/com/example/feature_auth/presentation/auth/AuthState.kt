package com.example.feature_auth.presentation.auth


sealed interface AuthStateUI {
    val email: String
    val password: String
    val repeatPassword: String

    data class Loading(
        override val email: String = "",
        override val password: String = "",
        override val repeatPassword: String = "",
    ) : AuthStateUI

    //    data object Success : AuthStateUI
    data class Login(
        override val email: String,
        override val password: String,
        override val repeatPassword: String = "",
        val enableButton: Boolean
    ) :
        AuthStateUI

    data class SignUp(
        override val email: String,
        override val password: String,
        override val repeatPassword: String,
        val enableButton: Boolean
    ) : AuthStateUI

}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data object NavigateToRegisterScreen : AuthEffect
    data class ShowPopUpError(val message: String?) : AuthEffect
}

sealed interface AuthEvent {
    data object ClickRegister : AuthEvent
    data object ClickLogin : AuthEvent
    data object ClickVk : AuthEvent
    data object ClickOd : AuthEvent
    data object ClickToLogin : AuthEvent
    data object ClickToRegistration : AuthEvent
    data class InputEmail(val email: String) : AuthEvent
    data class InputPassword(val password: String) : AuthEvent
    data class InputRepeatPassword(val password: String) : AuthEvent
}