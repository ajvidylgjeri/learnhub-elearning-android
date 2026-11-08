package com.example.elearning.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.data.Course
import com.example.elearning.databinding.ItemCourseBinding

class CourseAdapter(
    private val horizontal: Boolean,
    private val onClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    private var items: List<Course> = emptyList()

    fun submit(list: List<Course>) {
        items = list
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemCourseBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (horizontal) {
            val width = (240 * parent.resources.displayMetrics.density).toInt()
            b.root.layoutParams = RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        with(holder.b) {
            image.loadImage(c.thumbnailUrl)
            title.text = c.title
            instructor.text = c.instructor
            meta.text = "${c.category} • ${c.duration}"
            root.setOnClickListener { onClick(c) }
        }
    }

    override fun getItemCount() = items.size
}
