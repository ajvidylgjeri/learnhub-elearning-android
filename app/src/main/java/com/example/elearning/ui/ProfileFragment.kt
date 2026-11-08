package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.elearning.data.Repository
import com.example.elearning.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _b = FragmentProfileBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = Repository.currentUser
        b.etName.setText(user?.displayName.orEmpty())
        b.tvEmail.text = user?.email.orEmpty()

        b.btnUpdate.setOnClickListener {
            val name = b.etName.text.toString().trim()
            if (name.isEmpty()) { b.etName.error = "Name cannot be empty"; return@setOnClickListener }
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    Repository.updateName(name)
                    requireContext().toast("Profile updated")
                } catch (e: Exception) {
                    requireContext().toast(e.localizedMessage ?: "Update failed")
                }
            }
        }

        b.btnLogout.setOnClickListener {
            Repository.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
