package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.elearning.data.Repository
import com.example.elearning.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Repository.currentUser != null) {
            openMain(); return
        }
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnLogin.setOnClickListener { login() }
        b.tvRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    private fun login() {
        val email = b.etEmail.text.toString().trim()
        val password = b.etPassword.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b.etEmail.error = "Enter a valid email"; return
        }
        if (password.length < 6) {
            b.etPassword.error = "Password must be at least 6 characters"; return
        }
        setLoading(true)
        lifecycleScope.launch {
            try {
                Repository.login(email, password)
                openMain()
            } catch (e: Exception) {
                setLoading(false)
                toast(e.localizedMessage ?: "Login failed")
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        b.progress.visibility = if (loading) View.VISIBLE else View.GONE
        b.btnLogin.isEnabled = !loading
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
