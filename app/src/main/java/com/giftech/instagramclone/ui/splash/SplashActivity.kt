package com.giftech.instagramclone.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.giftech.instagramclone.core.viewmodel.ViewModelFactory
import com.giftech.instagramclone.databinding.ActivitySplashBinding
import com.giftech.instagramclone.ui.auth.login.LoginActivity
import com.giftech.instagramclone.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        disableDarkMode()

        setupViewmodel()

        val delayTime = 3000L

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
            finish()
        }, delayTime)

    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun checkLogin() {
        if(viewModel.checkLogin()){
            moveToHome()
        } else{
            moveToLogin()
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupViewmodel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[SplashViewModel::class.java]
    }
}