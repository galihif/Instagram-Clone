package com.giftech.instagramclone.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.data.model.User
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivityLoginBinding
import com.giftech.instagramclone.ui.auth.register.RegisterActivity
import com.giftech.instagramclone.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()

        setupViewmodel()

        binding.btnSignup.setOnClickListener {
            moveToSignUp()
        }

        binding.btnLogin.setOnClickListener {
            if(isFormValid()){
                loginUser()
            }
        }
    }

    private fun loginUser() {
        val user = User()
        user.email = binding.etEmail.text.toString()
        user.password = binding.etPassword.text.toString()
        viewModel.login(user).observe(this, {success ->
            if(success){
                moveToHome()
            }
        })
    }

    private fun isFormValid(): Boolean {
        if(binding.etEmail.text.isNullOrEmpty()){
            return false
        }
        if(binding.etPassword.text.isNullOrEmpty()){
            return false
        }
        return true
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[LoginViewModel::class.java]
    }


    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun moveToSignUp() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}