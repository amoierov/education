package com.example.feature_auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
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

                    }

                    is AuthStateUI.Loading -> {
                        // Handle error state
                    }

                    is AuthStateUI.Success -> {
                        // Handle authenticated state
                    }

                    is AuthStateUI.Login -> {
                        binding.layoutRegistration.root.visibility = View.GONE
                        binding.layoutLogin.root.visibility = View.VISIBLE
                        binding.layoutLogin.buttonLogin.isEnabled = state.enableButton
                    }

                }
            }
        }

    }


}