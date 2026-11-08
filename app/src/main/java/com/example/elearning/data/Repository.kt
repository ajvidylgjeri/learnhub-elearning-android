package com.example.elearning.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/** Single access point for Firebase Auth + Firestore. */
object Repository {
    private val auth get() = FirebaseAuth.getInstance()
    private val db get() = FirebaseFirestore.getInstance()

    // ---------- Authentication ----------
    val currentUser: FirebaseUser? get() = auth.currentUser
    private val uid: String get() = auth.currentUser?.uid ?: error("Not logged in")

    suspend fun register(name: String, email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: return
        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
        db.collection("users").document(user.uid)
            .set(mapOf("name" to name, "email" to email)).await()
    }

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun logout() = auth.signOut()

    suspend fun updateName(name: String) {
        val user = currentUser ?: return
        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
        db.collection("users").document(user.uid).set(mapOf("name" to name, "email" to user.email)).await()
    }

    // ---------- Courses ----------
    suspend fun getCourses(): List<Course> {
        val snap = db.collection("courses").get().await()
        if (!snap.isEmpty) return snap.toObjects(Course::class.java)
        // First run: upload demo catalogue, then serve it.
        try {
            val batch = db.batch()
            SeedData.courses.forEach { batch.set(db.collection("courses").document(it.id), it) }
            batch.commit().await()
        } catch (_: Exception) {
            // Rules may block writes; the app still works with the local copy.
        }
        return SeedData.courses
    }

    // ---------- Enrollments & progress ----------
    private fun enrollments() = db.collection("users").document(uid).collection("enrollments")

    suspend fun getEnrollments(): List<Enrollment> =
        enrollments().get().await().toObjects(Enrollment::class.java)

    suspend fun getEnrollment(courseId: String): Enrollment? =
        enrollments().document(courseId).get().await().toObject(Enrollment::class.java)

    suspend fun enroll(courseId: String) {
        enrollments().document(courseId)
            .set(mapOf("courseId" to courseId, "completedLessons" to emptyList<Long>(),
                "enrolledAt" to FieldValue.serverTimestamp())).await()
    }

    suspend fun setLessonCompleted(courseId: String, lessonIndex: Int, completed: Boolean) {
        val op = if (completed) FieldValue.arrayUnion(lessonIndex.toLong())
        else FieldValue.arrayRemove(lessonIndex.toLong())
        enrollments().document(courseId).update("completedLessons", op).await()
    }
}
