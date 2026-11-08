# LearnHub – E-Learning Android App

Task 1 of the Sqrock IT Solutions Android internship (Project Phase 1).
Kotlin · XML layouts + ViewBinding · Material 3 · Firebase Auth · Cloud Firestore · Glide.

## Features (mapped to the brief)

| Requirement | Where |
|---|---|
| Register / Login / Logout | `LoginActivity`, `RegisterActivity`, `ProfileFragment` (Firebase Auth, email + password) |
| Home screen: featured, categories, popular, search, navigation menu | `HomeFragment` + bottom navigation in `MainActivity` |
| Course details: thumbnail, title, instructor, description, duration, Enroll button | `CourseDetailActivity` |
| Video/lesson section: watch, lesson list, mark completed, track progress | `LessonActivity` (VideoView + MediaController) |
| User dashboard: enrolled courses, progress, completed lessons, update profile | `MyLearningFragment`, `ProfileFragment` |

Not included (optional in the brief): Google sign-in, admin panel.

## Data model (Firestore)

```
courses/{courseId}                 title, instructor, description, category, thumbnailUrl,
                                   duration, featured, popularity, lessons[{title, videoUrl, duration}]
users/{uid}                        name, email
users/{uid}/enrollments/{courseId} courseId, completedLessons[lessonIndex], enrolledAt
```

The demo catalogue (8 courses across Programming, Design, Marketing, Data Science) is
uploaded automatically the first time the app runs (`SeedData.kt`).

## Setup

1. Open this folder in **Android Studio** (Koala or newer) and let Gradle sync.
2. Create a Firebase project at https://console.firebase.google.com
   - Add an Android app with package name **`com.example.elearning`**.
   - Download `google-services.json` and place it in **`app/`** (see `app/google-services.json.example`).
3. In Firebase Console enable **Authentication → Sign-in method → Email/Password**.
4. Create a **Firestore Database** and paste the rules from `firestore.rules`.
5. Run on an emulator or device (min SDK 24). Register an account – the courses appear on first launch.

## Project structure

```
app/src/main/java/com/example/elearning/
├── data/   Models.kt, SeedData.kt, Repository.kt   (Firebase access in one place)
└── ui/     Activities, Fragments, RecyclerView adapters
```

## Notes
- Lesson videos are public sample MP4s; replace the URLs in `SeedData.kt` (or in Firestore) with your own.
- Seeding needs write access to `courses`; with stricter rules, add the documents manually in the console.
