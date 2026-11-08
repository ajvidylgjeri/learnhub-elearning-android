package com.example.elearning.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elearning.R
import com.example.elearning.data.Lesson
import com.example.elearning.databinding.ItemLessonBinding

class LessonAdapter(
    private val onClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<LessonAdapter.VH>() {

    private var items: List<Lesson> = emptyList()
    private var completed: Set<Long> = emptySet()
    var current: Int = -1

    fun submit(list: List<Lesson>, done: List<Long>, currentIndex: Int = -1) {
        items = list
        completed = done.toSet()
        current = currentIndex
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemLessonBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val l = items[position]
        with(holder.b) {
            lessonTitle.text = "${position + 1}. ${l.title}"
            lessonDuration.text = l.duration
            icon.setImageResource(
                if (completed.contains(position.toLong())) R.drawable.ic_check_circle else R.drawable.ic_play_circle
            )
            root.alpha = if (position == current || current == -1) 1f else 0.75f
            root.setOnClickListener { onClick?.invoke(position) }
        }
    }

    override fun getItemCount() = items.size
}
