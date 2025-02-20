package com.example.education.feature_home.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.education.MyApplication
import com.example.education.R
import com.example.education.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private val binding by viewBinding(FragmentHomeBinding::bind)

    private lateinit var courseAdapter: CourseAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()


        binding.textSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                homeViewModel.updateEvent(HomeEvent.InputSearchText(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        lifecycleScope.launch {
            homeViewModel.homeState.collect { state ->
                when (state) {
                    is HomeState.Content -> state.listCourses?.let { courseAdapter.updateCourses(it) }
                    HomeState.Loading -> TODO()
                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.homeEffects.collect { effect ->
                when (effect) {
                    HomeEffect.NavigateToSaved -> TODO()
                    is HomeEffect.OpenDetailCourse -> TODO()
                    is HomeEffect.ShowPopUpError -> TODO()
                }

            }
        }

    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(
            courses = emptyList(),
            onItemClick = {
                homeViewModel.updateEvent(HomeEvent.ClickToCourse)
            },
            onSaveClick = {
                homeViewModel.updateEvent(HomeEvent.ClickSaveCourse)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }
    }

}