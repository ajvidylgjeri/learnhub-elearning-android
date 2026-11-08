package com.example.elearning.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearning.R
import com.example.elearning.data.Course
import com.example.elearning.data.Enrollment
import com.example.elearning.data.Repository
import com.example.elearning.databinding.ActivityLessonBinding
import kotlinx.coroutines.launch

class LessonActivity : AppCompatActivity() {
    private lateinit var b: ActivityLessonBinding
    private lateinit var courseId: String
    private lateinit var course: Course
    private var completed: MutableList<Long> = mutableListOf()
    private var current = 0
    private val adapter = LessonAdapter { play(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(b.root)
        courseId = intent.getStringExtra(CourseDetailActivity.EXTRA_COURSE_ID) ?: run { finish(); return }

        b.toolbar.setNavigationOnClickListener { finish() }
        b.rvLessons.layoutManager = LinearLayoutManager(this)
        b.rvLessons.adapter = adapter
        MediaController(this).also {
            it.setAnchorView(b.videoContainer)
            b.videoView.setMediaController(it)
        }
        b.videoView.setOnPreparedListener { b.videoLoading.visibility = View.GONE; it.start() }
        b.videoView.setOnCompletionListener { if (!isDone(current)) toggleCompleted() }
        b.videoView.setOnErrorListener { _, _, _ ->
            b.videoLoading.visibility = View.GONE
            toast("Could not play this video. Check your connection.")
            true
        }
        b.btnComplete.setOnClickListener { toggleCompleted() }
        load()
    }

    private fun load() {
        lifecycleScope.launch {
            try {
                course = Repository.getCourses().first { it.id == courseId }
                var e = Repository.getEnrollment(courseId)
                if (e == null) { Repository.enroll(courseId); e = Enrollment(courseId) }
                completed = e.completedLessons.toMutableList()
                b.toolbar.title = course.title
                // Resume at the first lesson that is not completed yet.
                val first = course.lessons.indices.firstOrNull { !isDone(it) } ?: 0
                play(first)
            } catch (ex: Exception) {
                toast(ex.localizedMessage ?: "Could not load lessons")
                finish()
            }
        }
    }

    private fun isDone(i: Int) = completed.contains(i.toLong())

    private fun play(index: Int) {
        if (index !in course.lessons.indices) return
        current = index
        val lesson = course.lessons[index]
        b.videoLoading.visibility = View.VISIBLE
        b.videoView.setVideoURI(Uri.parse(lesson.videoUrl))
        b.videoView.requestFocus()
        refreshUi()
    }

    private fun toggleCompleted() {
        val nowDone = !isDone(current)
        lifecycleScope.launch {
            try {
                Repository.setLessonCompleted(courseId, current, nowDone)
                if (nowDone) completed.add(current.toLong()) else completed.remove(current.toLong())
                refreshUi()
            } catch (e: Exception) {
                toast(e.localizedMessage ?: "Could not save progress")
            }
        }
    }

    private fun refreshUi() {
        val total = course.lessons.size
        val percent = if (total == 0) 0 else completed.size * 100 / total
        b.tvLessonTitle.text = course.lessons[current].title
        b.progressBar.progress = percent
        b.tvProgress.text = "${completed.size}/$total lessons completed • $percent%"
        b.btnComplete.setText(if (isDone(current)) R.string.mark_incomplete else R.string.mark_completed)
        adapter.submit(course.lessons, completed, current)
    }
}
