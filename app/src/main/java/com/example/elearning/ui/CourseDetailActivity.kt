package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearning.R
import com.example.elearning.data.Course
import com.example.elearning.data.Enrollment
import com.example.elearning.data.Repository
import com.example.elearning.databinding.ActivityCourseDetailBinding
import kotlinx.coroutines.launch

class CourseDetailActivity : AppCompatActivity() {
    companion object { const val EXTRA_COURSE_ID = "courseId" }

    private lateinit var b: ActivityCourseDetailBinding
    private lateinit var courseId: String
    private var course: Course? = null
    private var enrollment: Enrollment? = null
    private val lessonAdapter = LessonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        courseId = intent.getStringExtra(EXTRA_COURSE_ID) ?: run { finish(); return }

        b.toolbar.setNavigationOnClickListener { finish() }
        b.rvLessons.layoutManager = LinearLayoutManager(this)
        b.rvLessons.adapter = lessonAdapter
        b.btnEnroll.setOnClickListener { onEnrollClicked() }
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        b.progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                course = Repository.getCourses().firstOrNull { it.id == courseId }
                enrollment = Repository.getEnrollment(courseId)
                bind()
            } catch (e: Exception) {
                toast(e.localizedMessage ?: "Could not load course")
            } finally {
                b.progress.visibility = View.GONE
            }
        }
    }

    private fun bind() {
        val c = course ?: return
        b.image.loadImage(c.thumbnailUrl)
        b.title.text = c.title
        b.instructor.text = "By ${c.instructor}"
        b.duration.text = "⏱ ${c.duration}"
        b.lessonCount.text = "🎬 ${c.lessons.size} lessons"
        b.category.text = c.category
        b.description.text = c.description
        lessonAdapter.submit(c.lessons, enrollment?.completedLessons ?: emptyList())
        b.btnEnroll.setText(if (enrollment == null) R.string.enroll else R.string.start_learning)
    }

    private fun onEnrollClicked() {
        if (enrollment != null) { openLessons(); return }
        b.btnEnroll.isEnabled = false
        lifecycleScope.launch {
            try {
                Repository.enroll(courseId)
                enrollment = Enrollment(courseId)
                toast("Enrolled successfully!")
                bind()
                openLessons()
            } catch (e: Exception) {
                toast(e.localizedMessage ?: "Could not enroll")
            } finally {
                b.btnEnroll.isEnabled = true
            }
        }
    }

    private fun openLessons() {
        startActivity(Intent(this, LessonActivity::class.java).putExtra(EXTRA_COURSE_ID, courseId))
    }
}
