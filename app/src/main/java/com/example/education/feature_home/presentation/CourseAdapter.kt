package com.example.education.feature_home.presentation

import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.RenderEffect
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.education.R
import com.example.education.data.network.models.Course
import com.example.education.databinding.ItemCourseBinding

class CourseAdapter(
    private var courses: List<Course>,
    private val onItemClick: (Course) -> Unit,
    private val onSaveClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    inner class CourseViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding by viewBinding(ItemCourseBinding::bind)

        fun bind(course: Course) {
//            val renderEffect = android.graphics.RenderEffect.createBlurEffect(15F, 0F, Shader.TileMode.MIRROR)
            with(binding) {
                // Устанавливаем текстовые значения
                rating.text = "4.9"
                date.text = course.date.toString()
                title.text = course.title
                description.text = course.edited_summary
                price.text = course.price

                // Загрузка изображения
                Glide.with(itemView.context)
                    .load(course.cover)
                    .placeholder(R.drawable.ic_launcher_background)  // Заглушка, пока изображение не загрузится
                    .into(image)

                // Изменение иконки "сохранить"
                save.setImageResource(
                    if (course.isSaved) R.drawable.ic_launcher_background else R.drawable.bookmark
                )

                // Установка обработчиков нажатий
                itemView.setOnClickListener { onItemClick(course) }
                save.setOnClickListener { onSaveClick(course) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int = courses.size


    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}