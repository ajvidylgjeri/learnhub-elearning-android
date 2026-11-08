package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearning.data.Repository
import com.example.elearning.databinding.FragmentMyLearningBinding
import kotlinx.coroutines.launch

/** User dashboard: enrolled courses, progress and completed lessons. */
class MyLearningFragment : Fragment() {
    private var _b: FragmentMyLearningBinding? = null
    private val b get() = _b!!

    private val adapter = EnrolledAdapter { course ->
        startActivity(Intent(requireContext(), LessonActivity::class.java)
            .putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.id))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _b = FragmentMyLearningBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.rvEnrolled.layoutManager = LinearLayoutManager(requireContext())
        b.rvEnrolled.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        b.progress.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val courses = Repository.getCourses().associateBy { it.id }
                val items = Repository.getEnrollments().mapNotNull { e ->
                    courses[e.courseId]?.let { EnrolledItem(it, e) }
                }
                adapter.submit(items)
                val lessonsDone = items.sumOf { it.enrollment.completedLessons.size }
                b.tvSummary.text = "${items.size} enrolled courses • $lessonsDone lessons completed"
                b.tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                requireContext().toast(e.localizedMessage ?: "Could not load your courses")
            } finally {
                _b?.progress?.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
