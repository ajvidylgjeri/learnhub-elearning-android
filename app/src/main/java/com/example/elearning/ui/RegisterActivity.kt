package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.elearning.data.Repository
import com.example.elearning.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnRegister.setOnClickListener { register() }
        b.tvLogin.setOnClickListener { finish() }
    }

    private fun register() {
        val name = b.etName.text.toString().trim()
        val email = b.etEmail.text.toString().trim()
        val password = b.etPassword.text.toString()
        if (name.isEmpty()) { b.etName.error = "Enter your name"; return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { b.etEmail.error = "Enter a valid email"; return }
        if (password.length < 6) { b.etPassword.error = "Password must be at least 6 characters"; return }

        b.progress.visibility = View.VISIBLE
        b.btnRegister.isEnabled = false
        lifecycleScope.launch {
            try {
                Repository.register(name, email, password)
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            } catch (e: Exception) {
                b.progress.visibility = View.GONE
                b.btnRegister.isEnabled = true
                toast(e.localizedMessage ?: "Registration failed")
            }
        }
    }
}
