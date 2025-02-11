package com.example.feature_auth.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentOnboardBinding


class OnboardFragment : Fragment(R.layout.fragment_onboard) {

    private val binding by viewBinding(FragmentOnboardBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }
    }
}