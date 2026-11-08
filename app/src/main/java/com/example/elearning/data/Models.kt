package com.example.elearning.data

import com.google.firebase.firestore.DocumentId

data class Lesson(
    val title: String = "",
    val videoUrl: String = "",
    val duration: String = ""
)

data class Course(
    @DocumentId val id: String = "",
    val title: String = "",
    val instructor: String = "",
    val description: String = "",
    val category: String = "",
    val thumbnailUrl: String = "",
    val duration: String = "",
    val featured: Boolean = false,
    val popularity: Long = 0,
    val lessons: List<Lesson> = emptyList()
)

/** Stored at users/{uid}/enrollments/{courseId}. completedLessons holds lesson indexes. */
data class Enrollment(
    val courseId: String = "",
    val completedLessons: List<Long> = emptyList()
) {
    fun percent(totalLessons: Int): Int =
        if (totalLessons == 0) 0 else (completedLessons.size * 100) / totalLessons
}
