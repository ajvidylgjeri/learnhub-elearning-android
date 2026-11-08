package com.example.elearning.data

/** Demo catalogue, uploaded to Firestore automatically the first time the app runs. */
object SeedData {
    private const val V1 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private const val V2 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private const val V3 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    private const val V4 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"

    private fun lessons(vararg titles: String) = titles.mapIndexed { i, t ->
        Lesson(t, listOf(V1, V2, V3, V4)[i % 4], "${8 + i * 3} min")
    }

    private fun thumb(seed: String) = "https://picsum.photos/seed/$seed/640/400"

    val courses: List<Course> = listOf(
        Course("kotlin-android", "Android Development with Kotlin", "Sarah Miller",
            "Build modern Android apps from scratch: activities, fragments, RecyclerView, Firebase and more.",
            "Programming", thumb("android"), "6 hours", true, 980,
            lessons("Setting up Android Studio", "Kotlin crash course", "Layouts and Views", "RecyclerView in depth", "Firebase integration")),
        Course("python-basics", "Python for Beginners", "David Chen",
            "Learn Python syntax, data structures and write your first real programs.",
            "Programming", thumb("python"), "5 hours", true, 1200,
            lessons("Installing Python", "Variables and types", "Control flow", "Functions", "Working with files")),
        Course("ui-ux", "UI/UX Design Fundamentals", "Elena Rossi",
            "Learn user research, wireframing and prototyping to design products people love.",
            "Design", thumb("design"), "4 hours", true, 760,
            lessons("What is UX?", "User research", "Wireframes", "Prototyping in Figma")),
        Course("graphic-design", "Graphic Design Masterclass", "Marco Alvarez",
            "Colour theory, typography and layout for posters, logos and social media.",
            "Design", thumb("graphic"), "7 hours", false, 540,
            lessons("Colour theory", "Typography", "Layout and grids", "Logo design")),
        Course("digital-marketing", "Digital Marketing Essentials", "Priya Nair",
            "SEO, social media, email and paid ads explained with practical campaigns.",
            "Marketing", thumb("marketing"), "5 hours", true, 890,
            lessons("Marketing funnel", "SEO basics", "Social media strategy", "Paid advertising")),
        Course("content-marketing", "Content Marketing Strategy", "James Walker",
            "Plan, create and distribute content that grows your audience.",
            "Marketing", thumb("content"), "3 hours", false, 410,
            lessons("Audience research", "Content calendar", "Measuring results")),
        Course("data-science", "Data Science with Python", "Dr. Aisha Khan",
            "Pandas, visualisation and machine learning basics on real datasets.",
            "Data Science", thumb("datascience"), "8 hours", true, 1100,
            lessons("Intro to data science", "Pandas essentials", "Data visualisation", "Your first ML model", "Project walkthrough")),
        Course("sql-analytics", "SQL for Data Analysis", "Tom Becker",
            "Query, join and aggregate data confidently with SQL.",
            "Data Science", thumb("sql"), "4 hours", false, 650,
            lessons("SELECT basics", "Joins", "Aggregations", "Window functions"))
    )
}
