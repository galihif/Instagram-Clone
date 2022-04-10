package com.giftech.instagramclone.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityRegisterBinding
import com.giftech.instagramclone.ui.auth.login.LoginActivity
import com.giftech.instagramclone.ui.home.HomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        setupViewmodel()

        binding.btnLogin.setOnClickListener {
            moveToLogin()
        }

        binding.btnSignup.setOnClickListener {
            if(isFormValid()){
                registerUser()
            }
        }

        viewModel.isLogged.observe(this){
            moveToHome(it)
        }
    }

    private fun registerUser() {
        val user = User()
        with(binding){
            user.username = etUsername.text.toString()
            user.email = etEmail.text.toString()
            user.password = etPassword.text.toString()
        }
        viewModel.register(user)
    }

    private fun moveToHome(isLogged: Boolean) {
        if(isLogged){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[RegisterViewModel::class.java]
    }

    private fun isFormValid(): Boolean {
        with(binding){
            if (etUsername.text.isNullOrEmpty()){
                etUsername.error = "Username must be filled"
                return false
            }
            if(etPassword.text.isNullOrEmpty()){
                etPassword.error = "Password must be filled"
                return false
            }
        }
        return true
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}