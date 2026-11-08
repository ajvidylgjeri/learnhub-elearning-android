package com.example.elearning.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.Course
import com.example.elearning.data.Enrollment
import com.example.elearning.databinding.ItemEnrolledBinding

data class EnrolledItem(val course: Course, val enrollment: Enrollment) {
    val percent get() = enrollment.percent(course.lessons.size)
}

class EnrolledAdapter(
    private val onClick: (Course) -> Unit
) : RecyclerView.Adapter<EnrolledAdapter.VH>() {

    private var items: List<EnrolledItem> = emptyList()

    fun submit(list: List<EnrolledItem>) {
        items = list
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemEnrolledBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemEnrolledBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        with(holder.b) {
            image.loadImage(item.course.thumbnailUrl)
            title.text = item.course.title
            instructor.text = item.course.instructor
            progress.progress = item.percent
            progressText.text =
                "${item.enrollment.completedLessons.size}/${item.course.lessons.size} lessons • ${item.percent}%"
            root.setOnClickListener { onClick(item.course) }
        }
    }

    override fun getItemCount() = items.size
}
