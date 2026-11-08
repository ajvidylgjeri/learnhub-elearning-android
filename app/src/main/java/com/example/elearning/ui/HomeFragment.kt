package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearning.R
import com.example.elearning.data.Course
import com.example.elearning.data.Repository
import com.example.elearning.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

    private var all: List<Course> = emptyList()
    private var category = "All"

    private val featuredAdapter = CourseAdapter(horizontal = true) { open(it) }
    private val popularAdapter = CourseAdapter(horizontal = false) { open(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.rvFeatured.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        b.rvFeatured.adapter = featuredAdapter
        b.rvPopular.layoutManager = LinearLayoutManager(requireContext())
        b.rvPopular.adapter = popularAdapter
        b.etSearch.doAfterTextChanged { applyFilter() }
        load()
    }

    private fun load() {
        b.progress.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                all = Repository.getCourses()
                buildChips()
                applyFilter()
            } catch (e: Exception) {
                requireContext().toast(e.localizedMessage ?: "Could not load courses")
            } finally {
                _b?.progress?.visibility = View.GONE
            }
        }
    }

    private fun buildChips() {
        b.chipGroup.removeAllViews()
        val names = listOf("All") + all.map { it.category }.distinct().sorted()
        names.forEach { name ->
            val chip = layoutInflater.inflate(R.layout.chip_category, b.chipGroup, false) as Chip
            chip.text = name
            chip.isChecked = name == category
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) { category = name; applyFilter() }
            }
            b.chipGroup.addView(chip)
        }
    }

    private fun applyFilter() {
        val q = _b?.etSearch?.text?.toString().orEmpty().trim().lowercase()
        val filtered = all.filter { c ->
            (category == "All" || c.category == category) &&
                (q.isEmpty() || c.title.lowercase().contains(q) ||
                    c.instructor.lowercase().contains(q) || c.category.lowercase().contains(q))
        }
        val featured = filtered.filter { it.featured }
        featuredAdapter.submit(featured)
        popularAdapter.submit(filtered.sortedByDescending { it.popularity })
        b.tvFeaturedLabel.visibility = if (featured.isEmpty()) View.GONE else View.VISIBLE
        b.rvFeatured.visibility = if (featured.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun open(course: Course) {
        startActivity(Intent(requireContext(), CourseDetailActivity::class.java)
            .putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
