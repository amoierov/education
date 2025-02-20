package com.example.education.feature_home.presentation

import com.example.education.data.network.models.Course


sealed interface HomeState {


    data class Content(
        val searchText: String= "",
        val sortedCourses: Boolean = false,
        val sortedTypes: Boolean = false,
        val listCourses: List<Course>? = emptyList()
    ) : HomeState
    data object Loading : HomeState

}

sealed interface HomeEffect {
    data class OpenDetailCourse(val id: Int) : HomeEffect
    data object NavigateToSaved : HomeEffect
    data class ShowPopUpError(val message: String?) : HomeEffect
}

sealed interface HomeEvent {

    data class InputSearchText(val searchText: String) : HomeEvent
    data object ClickFilter: HomeEvent
    data object ClickSorted: HomeEvent
    data object ClickToCourse: HomeEvent
    data object ClickSaveCourse: HomeEvent

}