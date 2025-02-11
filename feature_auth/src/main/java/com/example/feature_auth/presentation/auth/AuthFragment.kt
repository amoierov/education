package com.example.feature_auth.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentAuthBinding
import kotlinx.coroutines.launch


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val authViewModel: AuthUIViewModel by viewModels()
    private val binding by viewBinding(FragmentAuthBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inputs ButtonLogin and ButtonRegister
        binding.layoutRegistration.buttonRegistration.setOnClickListener {
            authViewModel.updateEvent(AuthEvent.ClickRegister)
            Log.i("Button", "КНОПКА КЛИК")
        }

        binding.layoutLogin.buttonLogin.setOnClickListener {
            authViewModel.updateEvent(AuthEvent.ClickLogin)
            Log.i("Button", "КНОПКА КЛИК")
        }

        //Inputs toLogin and toRegister
        binding.layoutRegistration.toLogin.setOnClickListener {

            authViewModel.updateEvent(AuthEvent.ClickToLogin)
        }

        binding.layoutLogin.toRegistration.setOnClickListener {

            authViewModel.updateEvent(AuthEvent.ClickToRegistration)
        }

        //Inputs Login
        binding.layoutLogin.textInputEmail.addTextChangedListener(InputTextWatcher { text ->
            authViewModel.updateEvent(AuthEvent.InputEmail(text))
        })
        binding.layoutLogin.textInputPassword.addTextChangedListener(InputTextWatcher { text ->
            authViewModel.updateEvent(AuthEvent.InputPassword(text))
        })
        //Inputs Register
        binding.layoutRegistration.textInputEmail.addTextChangedListener(InputTextWatcher { text ->
            authViewModel.updateEvent(AuthEvent.InputEmail(text))
            val cursorPosition = binding.layoutRegistration.textInputEmail.selectionStart
            Log.d("CursorPosition", "Cursor position: $cursorPosition")
        })

        binding.layoutRegistration.textInputPassword.addTextChangedListener(InputTextWatcher { text ->
            authViewModel.updateEvent(AuthEvent.InputPassword(text))
        })

        binding.layoutRegistration.textInputRepeatPassword.addTextChangedListener(InputTextWatcher { text ->
            authViewModel.updateEvent(AuthEvent.InputRepeatPassword(text))
        })

        //Inputs VK and OD
        binding.vk.setOnClickListener {
            authViewModel.updateEvent(AuthEvent.ClickVk)
        }

        binding.od.setOnClickListener {
            authViewModel.updateEvent(AuthEvent.ClickOd)
        }


        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthStateUI.SignUp -> {
                        binding.layoutLogin.root.visibility = View.GONE
                        binding.layoutRegistration.root.visibility = View.VISIBLE
                        binding.layoutRegistration.buttonRegistration.isEnabled = state.enableButton
                        binding.layoutRegistration.textInputEmail.setText(state.email)
                        //setText перезаписывает поле, поэтому курсор нужно обновить
                        binding.layoutRegistration.textInputEmail.setSelection(state.email.length)

                        binding.includeLoading.root.visibility = View.GONE
                        binding.linearLayout.visibility = View.VISIBLE


                    }

                    is AuthStateUI.Loading -> {
                        binding.includeLoading.root.visibility = View.VISIBLE
                        binding.layoutRegistration.root.visibility = View.GONE
                        binding.layoutLogin.root.visibility = View.GONE
                        binding.linearLayout.visibility = View.GONE

                    }

                    is AuthStateUI.Login -> {
                        binding.layoutRegistration.root.visibility = View.GONE
                        binding.layoutLogin.root.visibility = View.VISIBLE
                        binding.layoutLogin.buttonLogin.isEnabled = state.enableButton

                        binding.includeLoading.root.visibility = View.GONE
                        binding.linearLayout.visibility = View.VISIBLE
                    }

                }
            }
        }

        lifecycleScope.launch {
            authViewModel.authEffects.collect { effect ->
                when (effect) {
                    is AuthEffect.NavigateToRegisterScreen -> {
                        // Navigate to register screen
                    }

                    is AuthEffect.ShowPopUpError -> {
                        Toast.makeText(this@AuthFragment.requireContext(), effect.message, Toast.LENGTH_LONG).show()
                    }

                    AuthEffect.NavigateToHome -> {
                        Toast.makeText(this@AuthFragment.requireContext(), "ok", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }

    }


}