package com.example.feature_auth.presentation.auth

import android.text.Editable
import android.text.TextWatcher

class InputTextWatcher(private val action: (string: String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        action(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {}
}