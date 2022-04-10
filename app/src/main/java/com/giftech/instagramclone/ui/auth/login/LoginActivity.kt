package com.giftech.instagramclone.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giftech.instagramclone.databinding.ActivityLoginBinding
import com.giftech.instagramclone.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        binding.btnSignup.setOnClickListener {
            moveToSignUp()
        }
    }

    private fun moveToSignUp() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}