package com.example.education.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.education.data.network.models.Course
import com.example.education.feature_home.domain.GetCoursesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase
) : ViewModel() {


    private val _homeState = MutableStateFlow<HomeState>(
        HomeState.Content("", listCourses = emptyList())
    )
    val homeState: StateFlow<HomeState> = _homeState

    private val _homeEffects: MutableSharedFlow<HomeEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 1)

    val homeEffects: SharedFlow<HomeEffect> = _homeEffects

    private var originalCourses: List<Course>? = emptyList()



    init {
        fetchCourses()
    }


    fun updateEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ClickFilter -> TODO()
            HomeEvent.ClickSorted -> _homeState.update {
                if (it is HomeState.Content) {
                    it.copy(listCourses = toggleSortOrder(it.listCourses))
                } else {
                    it
                }
            }
            HomeEvent.ClickToCourse -> TODO()
            is HomeEvent.InputSearchText -> _homeState.update {

                if (it is HomeState.Content) {
                    val filteredCourses = if (event.searchText.isEmpty()) {
                        originalCourses
                    } else {
                        searchCourse(originalCourses, event.searchText)
                    }
                    it.copy(
                        searchText = event.searchText,
                        listCourses = filteredCourses
                    )
                } else {
                    it
                }
            }

            HomeEvent.ClickSaveCourse -> TODO()
        }
    }


    private fun sendAuthEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _homeEffects.emit(effect)
        }

    }


    private fun fetchCourses() {
        viewModelScope.launch {
            val courses = getCoursesUseCase.getCourses()
            originalCourses = courses
            _homeState.value = HomeState.Content(listCourses = courses)
        }
    }

    private fun searchCourse(listCourse: List<Course>?, searchText: String): List<Course> {
        return listCourse?.filter { course ->
            course.title.contains(searchText, ignoreCase = true) ||
                    course.title.contains(searchText, ignoreCase = true) ||
                    course.summary.contains(searchText, ignoreCase = true)
        } ?: emptyList()
    }


    private fun toggleSortOrder(listCourses: List<Course>?): List<Course> {
//        val date = listCourses?.get(1)?.edited_date
//        val localDate = LocalDate.parse(date,DateTimeFormatter.ofPattern("YYYY-MM-DDThh:mm:ssZ"))
        return emptyList()
    }

}
